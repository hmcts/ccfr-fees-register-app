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

    public RangedFeeDto toRangedFeeDto(Fee fee) {
        RangedFeeDto rangedFeeDto = new RangedFeeDto();

        rangedFeeDto.setCode(fee.getCode());

        rangedFeeDto.setChannel(fee.getChannelType().getName());
        rangedFeeDto.setDirection(fee.getDirectionType().getName());
        rangedFeeDto.setEvent(fee.getEventType().getName());
        rangedFeeDto.setJurisdiction1(fee.getJurisdiction1().getName());
        rangedFeeDto.setJurisdiction2(fee.getJurisdiction2().getName());
        rangedFeeDto.setService(fee.getService().getName());

        rangedFeeDto.setMemoLine(fee.getMemoLine());
        rangedFeeDto.setFeeOrderName(fee.getFeeOrderName());
        rangedFeeDto.setNaturalAccountCode(fee.getNaturalAccountCode());

        //List<FeeVersionDto> feeVersionsDtos = fee.getFeeVersions().stream().map(v -> toFeeVersionDto(v)).collect(Collectors.toList());
        //rangedFeeDto.setFeeVersionDtos(feeVersionsDtos);

        return rangedFeeDto;

    }

    public Fee2Dto toFeeDto(Fee fee) {
        Fee2Dto fee2Dto = new Fee2Dto();

        fee2Dto.setCode(fee.getCode());
        fee2Dto.setMemoLine(fee.getMemoLine());

        fee2Dto.setChannelTypeDto(fee.getChannelType());
        fee2Dto.setDirectionTypeDto(fee.getDirectionType());
        fee2Dto.setEventTypeDto(fee.getEventType());
        fee2Dto.setJurisdiction1Dto(fee.getJurisdiction1());
        fee2Dto.setJurisdiction2Dto(fee.getJurisdiction2());
        fee2Dto.setServiceTypeDto(fee.getService());

        fee2Dto.setNaturalAccountCode(fee.getNaturalAccountCode());
        fee2Dto.setFeeOrderName(fee.getFeeOrderName());


        if (fee instanceof RangedFee) {
            fee2Dto.setMinRange(((RangedFee) fee).getMinRange());
            fee2Dto.setMaxRange(((RangedFee) fee).getMaxRange());
        }

        List<FeeVersionDto> feeVersionDtos = fee.getFeeVersions().stream().map(v -> toFeeVersionDto(v)).collect(Collectors.toList());
        fee2Dto.setFeeVersionDtos(feeVersionDtos);

        return fee2Dto;
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

        // map the amount
        if (feeVersion.getAmount() instanceof FlatAmount) {
            FlatAmountDto flatAmountDto = new FlatAmountDto();
            flatAmountDto.setAmount(((FlatAmount) feeVersion.getAmount()).getAmount());
            feeVersionDto.setFlatAmount(flatAmountDto);
        }

        if (feeVersion.getAmount() instanceof PercentageAmount) {
            PercentageAmountDto percentageAmountDto = new PercentageAmountDto();
            percentageAmountDto.setPercentage(((PercentageAmount) feeVersion.getAmount()).getPercentage());
            feeVersionDto.setPercentageAmount(percentageAmountDto);
        }

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
            channelTypeRepository.findByNameOrThrow(ChannelType.DEFAULT) :
            channelTypeRepository.findByNameOrThrow(channel);

        fee.setChannelType(channelType);

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
            fee.setJurisdiction1(jurisdiction1Repository.findByNameOrThrow(jurisdiction1));
        }
    }

    private void fillJuridistiction2(Fee fee, String jurisdiction2) {
        if(jurisdiction2 != null) {
            fee.setJurisdiction2(jurisdiction2Repository.findByNameOrThrow(jurisdiction2));
        }
    }

    private void fillEventType(RangedFee fee, String event) {

        if(event == null) {
            return;
        }

        fee.setEventType(eventTypeRepository.findByNameOrThrow(event));
    }

    private void fillDirectionType(RangedFee fee, String direction) {

        if(direction == null) {
            return;
        }

        fee.setDirectionType(directionTypeRepository.findByNameOrThrow(direction));

    }

    private void fillServiceType(RangedFee fee, String service) {

        if(service == null) {
            return;
        }

        fee.setService(serviceTypeRepository.findByNameOrThrow(service));
    }
}
