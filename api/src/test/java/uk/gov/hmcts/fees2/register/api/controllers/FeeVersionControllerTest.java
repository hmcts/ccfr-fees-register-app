package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.IdamUtil;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles({"idam-test"})
public class FeeVersionControllerTest extends BaseIntegrationTest {

    private static final String CONTENT_TYPE = "application/vnd.uk.gov.hmcts.cc.fr.v2+json";

    @Autowired
    private FeeController feeController;

    @Autowired
    private FeeVersionController feeVersionController;

    @Mock
    private HttpServletResponse response;

    @MockBean
    private IdamUtil idamUtil;

    @Before
    public void setup(){


    }

    @Test(expected = FeeNotFoundException.class)
    public synchronized void testDeleteFeeAndVersion() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
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
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");

        try {

            feeVersionController.approve(arr[3], 1);

            feeVersionController.deleteFeeVersion(arr[3], 1);

        } finally {
            forceDeleteFee(arr[3]);
        }

    }

    @Test
    public synchronized void testDeleteVersionDoesNotDeleteFee() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name","consolidated fee order name",  "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");


        try {

            feeVersionController.approve(arr[3], 1);

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "consolidated fee order name",  "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(arr[3], feeVersionDto2, new Principal() {
                @Override
                public String getName() {
                    return "AUTHOR";
                }
            });

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
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.approved, "memoLine1", "fee order name1", "consolidated fee order name",
            "natural account code1", "SI_1", "siRefId1", DirectionType.directionWith().name("enhanced").build()));

        FeeVersionDto version2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine2", "fee order name2", "consolidated fee order name",
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
    public synchronized void testFlatAmountEditFeeVersion() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "consolidated fee order name",  "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");
        try {
            feeVersionController.approve(arr[3], 1);

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "consolidated fee order name",  "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(arr[3], feeVersionDto2, new Principal() {
                @Override
                public String getName() {
                    return "AUTHOR";
                }
            });

            feeVersionController.editFeeVersion(arr[3], 2,feeVersionDto2);
            assertThat(feeController.getFee(arr[3], response).getFeeVersionDtos().get(1).getFlatAmount().getAmount()).hasToString("2500.00");
            feeVersionController.deleteFeeVersion(arr[3], 2);
        } finally {
            forceDeleteFee(arr[3]);
        }

    }


    @Test
    public synchronized void testPercentageAmountEditFeeVersion() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");
        try {
            feeVersionController.approve(arr[3], 1);

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);
            feeVersionDto2.setFlatAmount(null);
            feeVersionDto2.setPercentageAmount(getPercentageAmountDto());

            feeVersionController.createVersion(arr[3], feeVersionDto2, new Principal() {
                @Override
                public String getName() {
                    return "AUTHOR";
                }
            });

            feeVersionController.editFeeVersion(arr[3], 2,feeVersionDto2);
            assertThat(feeController.getFee(arr[3], response).getFeeVersionDtos().get(1).getPercentageAmount().getPercentage()).hasToString("4.50");
            feeVersionController.deleteFeeVersion(arr[3], 2);
        } finally {
            forceDeleteFee(arr[3]);
        }
    }

    @Test
    public synchronized void testVolumeAmountEditFeeVersion() throws Exception {

        FixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
            "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] arr = loc.split("/");
        try {
            feeVersionController.approve(arr[3], 1);

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, "memoLine", "fee order name", "consolidated fee order name", "natural account code",
                "SI", "siRefId", DirectionType.directionWith().name("enhanced").build());
            feeVersionDto2.setVersion(2);
            feeVersionDto2.setFlatAmount(null);
            feeVersionDto2.setVolumeAmount(getVolumeAmountDto());

            feeVersionController.createVersion(arr[3], feeVersionDto2, new Principal() {
                @Override
                public String getName() {
                    return "AUTHOR";
                }
            });

            feeVersionController.editFeeVersion(arr[3], 2,feeVersionDto2);
            assertThat(feeController.getFee(arr[3], response).getFeeVersionDtos().get(1).getVolumeAmount().getAmount()).hasToString("250.00");
            feeVersionController.deleteFeeVersion(arr[3], 2);
        } finally {
            forceDeleteFee(arr[3]);
        }

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

}
