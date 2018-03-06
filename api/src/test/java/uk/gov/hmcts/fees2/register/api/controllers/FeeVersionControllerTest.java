package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.acl.PrincipalImpl;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
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
    public synchronized void testDeleteFeeAndVersion() {

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.draft, null, null, null, null, null, null));

        feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));

        try {

            feeVersionController.deleteFeeVersion(dto.getCode(), 1);


            feeController.getFee(dto.getCode(), response);

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }

    @Test(expected = BadRequestException.class)
    public synchronized void testDeleteApprovedVersionFails() {

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, null, null, null, null, null, null));

        feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));

        try {

            feeVersionController.changeVersionStatus(dto.getCode(), 1, FeeVersionStatus.approved, new PrincipalImpl(AUTHOR));

            feeVersionController.deleteFeeVersion(dto.getCode(), 1);

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }

    @Test
    public synchronized void testDeleteVersionDoesNotDeleteFee() {

        CreateFixedFeeDto dto = getFee();
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.pending_approval, null, null, null, null, null, null));

        feeController.createFixedFee(dto, null, new PrincipalImpl(AUTHOR));


        try {

            feeVersionController.changeVersionStatus(dto.getCode(), 1, FeeVersionStatus.approved, new PrincipalImpl(AUTHOR));

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft, null, null, null, null, null, null);
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(dto.getCode(), feeVersionDto2, new PrincipalImpl(AUTHOR));

            assertThat(feeController.getFee(dto.getCode(), response).getFeeVersionDtos().size()).isEqualTo(2);

            feeVersionController.deleteFeeVersion(dto.getCode(), 2);

            assertThat(feeController.getFee(dto.getCode(),response)).isNotNull();

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }

    private CreateFixedFeeDto getFee() {

        CreateFixedFeeDto dto = new CreateFixedFeeDto();

        dto.setCode("PEPITO");

        dto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        dto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        dto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        dto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        dto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return dto;

    }

}
