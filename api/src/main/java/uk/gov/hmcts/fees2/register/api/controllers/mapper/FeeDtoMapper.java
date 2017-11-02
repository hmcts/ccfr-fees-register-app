package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.advice.exception.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.model.amount.PercentageAmount;
import uk.gov.hmcts.fees2.register.data.repository.*;

import java.util.Arrays;

@Component
public class FeeDtoMapper {

    private Jurisdiction1Repository jurisdiction1Repository;

    private Jurisdiction2Repository jurisdiction2Repository;
    private FeeRepository feeRepository;
    private ServiceTypeRepository serviceTypeRepository;
    private ChannelTypeRepository channelTypeRepository;
    private EventTypeRepository eventTypeRepository;


    @Autowired
    public FeeDtoMapper(
        Jurisdiction1Repository jurisdiction1Repository,
        Jurisdiction2Repository jurisdiction2Repository,
        FeeRepository feeRepository,
        ServiceTypeRepository serviceTypeRepository,
        ChannelTypeRepository channelTypeRepository,
        EventTypeRepository eventTypeRepository) {

        this.jurisdiction1Repository = jurisdiction1Repository;
        this.jurisdiction2Repository = jurisdiction2Repository;
        this.feeRepository = feeRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.channelTypeRepository = channelTypeRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    public Fee toFee(RangedFeeDto request) {

        RangedFee fee = new RangedFee();

        fillCode(fee, request.getCode());

        fee.setMaxRange(request.getMaxRange());
        fee.setMinRange(request.getMinRange());

        fillJuridistiction1(fee, request.getJurisdiction1());
        fillJuridistiction2(fee, request.getJurisdiction2());

        fillServiceType(fee, request.getService());
        fillEventType(fee, request.getEvent());
        fillChannelType(fee, request.getChannel());
        fee.setMemoLine(request.getMemoLine());

        FeeVersion version = toFeeVersion(request.getVersion());
        version.setFee(fee);
        fee.setFeeVersions(Arrays.asList(version));

        return fee;
    }

    public FeeVersion toFeeVersion(FeeVersionDto versionDto) {

        if(versionDto == null){
            throw new BadRequestException("Version is required");
        }

        FeeVersion version = new FeeVersion();

        version.setValidFrom(versionDto.getValidFrom());
        version.setValidTo(versionDto.getValidTo());

        fillVersionStatus(version, versionDto.getStatus());
        fillVersionVersion(version, versionDto.getVersion());

        version.setDescription(versionDto.getDescription());

        if(versionDto.getFlatAmount() != null) {
            version.setAmount(toFlatAmount(versionDto.getFlatAmount()));
        }else if(versionDto.getPercentageAmount() != null) {
            version.setAmount(toPercentageAmount(versionDto.getPercentageAmount()));
        }

        return version;
    }

    private Amount toPercentageAmount(PercentageAmountDto percentageAmount) {
        PercentageAmount amount = new PercentageAmount();
        amount.setPercentage(percentageAmount.getPercentage());
        return amount;
    }

    private FlatAmount toFlatAmount(FlatAmountDto dto) {
        FlatAmount amount = new FlatAmount();
        amount.setAmount(dto.getAmount());
        amount.setUnit(Unit.valueOf(dto.getUnit()));
        return amount;
    }

    private void fillVersionVersion(FeeVersion version, Integer versionNumber) {

        if(versionNumber == null) {
            version.setVersion(1);
        }else{
            version.setVersion(versionNumber);
        }
    }

    private void fillVersionStatus(FeeVersion version, FeeVersionStatus status) {

        if(status == null) {
            status = FeeVersionStatus.draft;
        }

        version.setStatus(status);
    }

    private void fillChannelType(RangedFee fee, String channel) {

        if(channel == null){
            return;
        }

        ChannelType channelType = channelTypeRepository.findOne(channel);

        if(channelType == null){
            throw new BadRequestException("Unknow channel type " + channel);
        }

        fee.setChannelType(channelType);

    }

    private void fillServiceType(RangedFee fee, String service) {

        if(service == null) {
            return;
        }

        ServiceType serviceType = serviceTypeRepository.findOne(service);

        if(serviceType == null) {
            throw new BadRequestException("Unknown service " + service);
        }

        fee.setService(serviceType);
    }

    private void fillEventType(RangedFee fee, String event) {

        if(event == null) {
            return;
        }

        EventType eventType = eventTypeRepository.findOne(event);

        if(eventType == null) {
            throw new BadRequestException("Unknown event type " + event);
        }

        fee.setEventType(eventType);
    }

    /* --- */

    private void fillCode(Fee fee, String code) {
        if(code == null) {
            throw new BadRequestException("Code is required");
        }

        if(feeRepository.findByCode(code) != null) {
            throw new BadRequestException("Code is already in use");
        }

        fee.setCode(code);
    }

    private void fillJuridistiction1(Fee fee, String jurisdiction1) {

        if(jurisdiction1 != null) {
            Jurisdiction1 jur1 = jurisdiction1Repository.findOne(jurisdiction1);

            if(jur1 == null) {
                throw new BadRequestException("Unknown jurisdisction1 " + jurisdiction1);
            }

            fee.setJurisdiction1(jur1);

        }
    }

    private void fillJuridistiction2(Fee fee, String jurisdiction2) {

        if(jurisdiction2 != null) {
            Jurisdiction2 jur2 = jurisdiction2Repository.findOne(jurisdiction2);

            if(jur2 == null) {
                throw new BadRequestException("Unknown jurisdisction1 " + jurisdiction2);
            }

            fee.setJurisdiction2(jur2);

        }
    }


}
