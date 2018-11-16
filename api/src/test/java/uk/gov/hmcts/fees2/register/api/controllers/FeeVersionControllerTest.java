package uk.gov.hmcts.fees2.register.api.controllers;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sun.security.acl.PrincipalImpl;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeVersionControllerTest extends BaseIntegrationTest {

    private static final String CONTENT_TYPE = "application/vnd.uk.gov.hmcts.cc.fr.v2+json";

    @Autowired
    private FeeController feeController;

    @Autowired
    private FeeVersionController feeVersionController;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setup(){


    }

    @Test(expected = FeeNotFoundException.class)
    public synchronized void testDeleteFeeAndVersion() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");

        try {

            feeVersionController.deleteFeeVersion(arr[3], 1);


            feeController.getFee(arr[3], response);

        } finally {

            forceDeleteFee(arr[3]);
        }

    }

    @Test(expected = BadRequestException.class)
    public synchronized void testDeleteApprovedVersionFails() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");

        try {

            feeVersionController.approve(arr[3], 1, new PrincipalImpl(AUTHOR));

            feeVersionController.deleteFeeVersion(arr[3], 1);

        } finally {
            forceDeleteFee(arr[3]);
        }

    }

    @Test
    public synchronized void testDeleteVersionDoesNotDeleteFee() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");


        try {

            feeVersionController.approve(arr[3], 1, new PrincipalImpl(AUTHOR));

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(arr[3], feeVersionDto2, new PrincipalImpl(AUTHOR));

            assertThat(feeController.getFee(arr[3], response).getFeeVersionDtos().size()).isEqualTo(2);

            feeVersionController.deleteFeeVersion(arr[3], 2);

            assertThat(feeController.getFee(arr[3],response).getFeeVersionDtos().size()).isEqualTo(1);

        } finally {
            forceDeleteFee(arr[3]);
        }

    }

    @Test
    public synchronized void createFeeWithMultipleVersions() throws Exception {
        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.approved, "memoLine1", "fee order name1",
            "natural account code1", "SI_1", "siRefId1", DirectionType.directionWith().name("enhanced").build()));

        FeeVersionDto version2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine2", "fee order name2",
            "natural account code2", "SI_2", "siRefId2", DirectionType.directionWith().name("enhanced").build());
        version2.setVersion(2);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");


        Fee2Dto feeDto = feeController.getFee(arr[3], response);
        assertNotNull(feeDto);
        assertEquals(feeDto.getFeeVersionDtos().size(), 1);

        forceDeleteFee(arr[3]);
    }

    @Test
    public synchronized void createFeeAndUpdateApprovedVersionShouldFail() throws Exception {
        Fee2Dto feeDto = createFeeAndReturnFeeDto(getFee());
        FeeVersionDto newVersionDto = feeDto.getCurrentVersion();
        newVersionDto.setFlatAmount(new FlatAmountDto(new BigDecimal("3500")));
        newVersionDto.setSiRefId("5.5");

        restActions
            .withUser("admin")
            .put("/fees/" + feeDto.getCode() + "/versions/1", newVersionDto)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.cause", Matchers.is("The fee version with approved status cannot be updated.")));

        forceDeleteFee(feeDto.getCode());
    }

    @Test
    public synchronized void createFeeAndUpdateIncorrectVersionShouldFail() throws Exception {
        Fee2Dto feeDto = createFeeAndReturnFeeDto(getFee());
        FeeVersionDto newVersionDto = feeDto.getCurrentVersion();
        newVersionDto.setFlatAmount(new FlatAmountDto(new BigDecimal("3500")));
        newVersionDto.setSiRefId("5.5");

        restActions
            .withUser("admin")
            .put("/fees/" + feeDto.getCode() + "/versions/10", newVersionDto)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.is("fee-version for code=" + feeDto.getCode() + " was not found")));

        forceDeleteFee(feeDto.getCode());
    }

    @Test
    public synchronized void createFeeAndCreateAndThenUpdateNewDraftVersionShouldPass() throws Exception {
        Fee2Dto feeDto = createFeeAndReturnFeeDto(getFee());
        assertNotNull(feeDto);
        assertThat(feeDto.getCurrentVersion().getStatus()).isEqualTo(FeeVersionStatus.approved);

        FeeVersionDto newVersionDto = feeDto.getCurrentVersion();
        newVersionDto.setStatus(FeeVersionStatus.draft);
        newVersionDto.setFlatAmount(new FlatAmountDto(new BigDecimal("3500")));
        newVersionDto.setSiRefId("5.5");

        restActions
            .withUser("admin")
            .post("/fees/" +feeDto.getCode() + "/versions", newVersionDto)
            .andExpect(status().isCreated());
        feeDto = feeController.getFee(feeDto.getCode(), response);

        Optional<FeeVersionDto> opt = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.draft)).findAny();
        FeeVersionDto draftVersionDto = opt.get();
        assertNotNull(draftVersionDto);
        assertThat(draftVersionDto.getVersion()).isEqualTo(2);

        draftVersionDto.setFlatAmount(new FlatAmountDto(new BigDecimal("4100")));
        draftVersionDto.setDirection("cost recovery");
        draftVersionDto.setMemoLine("memo line 1113");
        draftVersionDto.setSiRefId("5.45");
        restActions
            .withUser("admin")
            .put("/fees/" + feeDto.getCode() + "/versions/2", draftVersionDto)
            .andExpect(status().isNoContent());
        opt = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.draft)).findAny();
        FeeVersionDto updatedDraftVersion = opt.get();
        assertThat(updatedDraftVersion.getAmount()).isEqualTo(new BigDecimal("4100"));
        assertThat(updatedDraftVersion.getDirection()).isEqualTo("cost recovery");
        assertThat(updatedDraftVersion.getMemoLine()).isEqualTo("memo line 1113");
        assertThat(updatedDraftVersion.getSiRefId()).isEqualTo("5.45");

        forceDeleteFee(feeDto.getCode());
    }

    private FixedFeeDto getFee() {

        FixedFeeDto dto = new FixedFeeDto();

        dto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        dto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        dto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        dto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        dto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return dto;

    }

    private Fee2Dto createFeeAndReturnFeeDto(FixedFeeDto feeDto) throws Exception {
        feeDto.setVersion(getFeeVersionDto(FeeVersionStatus.approved, "memo line 1111", "fee order name 1111",
            "natural account code 1111", "si 1111", "5.4", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(feeDto);

        MvcResult result = restActions
            .get(loc)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), Fee2Dto.class);
    }

}
