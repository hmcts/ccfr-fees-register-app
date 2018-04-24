package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.acl.PrincipalImpl;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
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

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        //feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));
        String loc = restActions
                        .withUser("admin")
                        .post("/fees-register/fixed-fees", dto)
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        try {

            feeVersionController.deleteFeeVersion(arr[3], 1);


            feeController.getFee(arr[3], response);

        } finally {
            feeController.deleteFee(arr[3]);
        }

    }

    @Test(expected = BadRequestException.class)
    public synchronized void testDeleteApprovedVersionFails() throws Exception {

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        //feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));
        String loc = restActions
                        .withUser("admin")
                        .post("/fees-register/fixed-fees", dto)
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        try {

            feeVersionController.changeVersionStatus(arr[3], 1, FeeVersionStatus.approved, new PrincipalImpl(AUTHOR));

            feeVersionController.deleteFeeVersion(arr[3], 1);

        } finally {
            feeController.deleteFee(arr[3]);
        }

    }

    @Test
    public synchronized void testDeleteVersionDoesNotDeleteFee() throws Exception {

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        //feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));
        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");


        try {

            feeVersionController.changeVersionStatus(arr[3], 1, FeeVersionStatus.approved, new PrincipalImpl(AUTHOR));

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(arr[3], feeVersionDto2, new PrincipalImpl(AUTHOR));

            assertThat(feeController.getFee(arr[3], response).getFeeVersionDtos().size()).isEqualTo(2);

            feeVersionController.deleteFeeVersion(arr[3], 2);

            assertThat(feeController.getFee(arr[3],response)).isNotNull();

        } finally {
            feeController.deleteFee(arr[3]);
        }

    }

    @Test
    public synchronized void createFeeWithMultipleVersions() throws Exception {
        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.approved, "memoLine1", "fee order name1",
            "natural account code1", "SI_1", "siRefId1", DirectionType.directionWith().name("enhanced").build()));

        //feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));

        FeeVersionDto version2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine2", "fee order name2",
            "natural account code2", "SI_2", "siRefId2", DirectionType.directionWith().name("enhanced").build());
        version2.setVersion(2);

        //feeVersionController.createVersion(feeCode, version2, new PrincipalImpl(AUTHOR));
        String loc = restActions
                        .withUser("admin")
                        .post("/fees-register/fixed-fees", dto)
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");


        Fee2Dto feeDto = feeController.getFee(arr[3], response);
        assertNotNull(feeDto);
        assertEquals(feeDto.getFeeVersionDtos().size(), 1);
    }

    private CreateFixedFeeDto getFee() {

        CreateFixedFeeDto dto = new CreateFixedFeeDto();

        dto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        dto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        dto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        dto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        dto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return dto;

    }

}
