package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class FeeDtoMapper {

    private Jurisdiction1Repository jurisdiction1Repository;
    private Jurisdiction2Repository jurisdiction2Repository;
    private Fee2Repository fee2Repository;
    private ServiceTypeRepository serviceTypeRepository;
    private ChannelTypeRepository channelTypeRepository;
    private EventTypeRepository eventTypeRepository;
    private DirectionTypeRepository directionTypeRepository;

    @Autowired
    public FeeDtoMapper(
        Jurisdiction1Repository jurisdiction1Repository,
        Jurisdiction2Repository jurisdiction2Repository,
        DirectionTypeRepository directionTypeRepository,
        Fee2Repository fee2Repository,
        ServiceTypeRepository serviceTypeRepository,
        ChannelTypeRepository channelTypeRepository,
        EventTypeRepository eventTypeRepository) {

        this.jurisdiction1Repository = jurisdiction1Repository;
        this.jurisdiction2Repository = jurisdiction2Repository;
        this.fee2Repository = fee2Repository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.channelTypeRepository = channelTypeRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.directionTypeRepository = directionTypeRepository;
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
        fillDirectionType(fee, request.getDirection());

        fee.setMemoLine(request.getMemoLine());
        fee.setFeeOrderName(request.getFeeOrderName());
        fee.setNaturalAccountCode(request.getNaturalAccountCode());

        FeeVersion version = toFeeVersion(request.getVersion());
        version.setFee(fee);
        fee.setFeeVersions(Arrays.asList(version));

        return fee;
    }

    public RangedFeeDto toFeeDto(Fee fee) {
        RangedFeeDto rangedFeeDto = new RangedFeeDto();

        rangedFeeDto.setCode(fee.getCode());
//        rangedFeeDto.setMinRange(fee.getMinRange());
//        rangedFeeDto.setMaxRange(rangedFee.getMaxRange());

        rangedFeeDto.setChannel(fee.getChannelType().getName());
        rangedFeeDto.setDirection(fee.getDirectionType().getName());
        rangedFeeDto.setEvent(fee.getEventType().getName());
        rangedFeeDto.setJurisdiction1(fee.getJurisdiction1().getName());
        rangedFeeDto.setJurisdiction2(fee.getJurisdiction2().getName());
        rangedFeeDto.setService(fee.getService().getName());

        rangedFeeDto.setMemoLine(fee.getMemoLine());
        rangedFeeDto.setFeeOrderName(fee.getFeeOrderName());
        rangedFeeDto.setNaturalAccountCode(fee.getNaturalAccountCode());

        List<FeeVersionDto> feeVersionsDtos = fee.getFeeVersions().stream().map(v -> toFeeVersionDto(v)).collect(Collectors.toList());
        rangedFeeDto.setFeeVersionDtos(feeVersionsDtos);

        return rangedFeeDto;

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

    public FeeVersionDto toFeeVersionDto(FeeVersion feeVersion) {

        FeeVersionDto feeVersionDto = new FeeVersionDto();

        feeVersionDto.setValidFrom(feeVersion.getValidFrom());
        feeVersionDto.setValidTo(feeVersion.getValidTo());

        feeVersionDto.setVersion(feeVersion.getVersion());
        feeVersionDto.setStatus(feeVersion.getStatus());
        feeVersionDto.setDescription(feeVersion.getDescription());

        // how map the amount


        return feeVersionDto;

    }

    private Amount toPercentageAmount(PercentageAmountDto percentageAmount) {
        PercentageAmount amount = new PercentageAmount();
        amount.setPercentage(percentageAmount.getPercentage());
        return amount;
    }

    private FlatAmount toFlatAmount(FlatAmountDto dto) {
        FlatAmount amount = new FlatAmount();
        amount.setAmount(dto.getAmount());
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

        ChannelType channelType = channel == null ?
            channelTypeRepository.findOne(ChannelType.DEFAULT) :
            channelTypeRepository.findOne(channel);

        if(channelType == null){
            throw new BadRequestException("Unknow channel type " + channel);
        }

        fee.setChannelType(channelType);

    }

    private void fillDirectionType(RangedFee fee, String direction) {

        if(direction == null) {
            return;
        }

        DirectionType directionType = directionTypeRepository.findOne(direction);

        if(directionType == null) {
            throw new BadRequestException("Unknown directionType " + direction);
        }

        fee.setDirectionType(directionType);

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

        if(fee2Repository.findByCode(code).isPresent()) {
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
