package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class FeeVersionControllerTest extends BaseIntegrationTest {

    @Autowired
    private FeeController feeController;

    @Autowired
    private FeeVersionController feeVersionController;

    @Test(expected = FeeNotFoundException.class)
    public synchronized void testDeleteFeeAndVersion() {

        CreateFixedFeeDto dto = getFee();

        feeController.createFixedFee(getFee(), null);

        try {

            feeVersionController.deleteFeeVersion(dto.getCode(), 1);

            feeController.getFee(dto.getCode());

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }

    @Test(expected = BadRequestException.class)
    public synchronized void testDeleteApprovedVersionFails() {

        CreateFixedFeeDto dto = getFee();

        feeController.createFixedFee(getFee(), null);

        try {

            ApproveFeeDto approveFeeDto = new ApproveFeeDto();
            approveFeeDto.setFeeCode(dto.getCode());
            approveFeeDto.setFeeVersion(1);

            feeController.approve(approveFeeDto);

            feeVersionController.deleteFeeVersion(dto.getCode(), 1);

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }

    @Test
    public synchronized void testDeleteVersionDoesNotDeleteFee() {

        CreateFixedFeeDto dto = getFee();

        feeController.createFixedFee(getFee(), null);

        try {

            ApproveFeeDto approveFeeDto = new ApproveFeeDto();
            approveFeeDto.setFeeCode(dto.getCode());
            approveFeeDto.setFeeVersion(1);

            feeController.approve(approveFeeDto);

            FeeVersionDto feeVersionDto2 = getFeeVersionDto(FeeVersionStatus.draft);
            feeVersionDto2.setVersion(2);

            feeVersionController.createVersion(dto.getCode(), feeVersionDto2);

            assertThat(feeController.getFee(dto.getCode()).getFeeVersionDtos().size()).isEqualTo(2);

            feeVersionController.deleteFeeVersion(dto.getCode(), 2);

            assertThat(feeController.getFee(dto.getCode())).isNotNull();

        } finally {
            feeController.deleteFee(dto.getCode());
        }

    }


    private CreateFixedFeeDto getFee() {

        CreateFixedFeeDto dto = new CreateFixedFeeDto();

        dto.setCode("PEPITO");
        dto.setVersion(getFeeVersionDto(FeeVersionStatus.draft));
        dto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        dto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        dto.setDirection(directionTypeService.findByNameOrThrow("enhanced").getName());
        dto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        dto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        dto.setChannel(channelTypeService.findByNameOrThrow("online").getName());
        dto.setMemoLine("Test memo line");
        dto.setFeeOrderName("CMC online fee order name");
        dto.setNaturalAccountCode("Natural code 001");

        return dto;

    }

}
