package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.model.amount.PercentageAmount;
import uk.gov.hmcts.fees2.register.data.model.amount.VolumeAmount;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.util.FeeFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeeDtoMapper {

    private Jurisdiction1Repository jurisdiction1Repository;
    private Jurisdiction2Repository jurisdiction2Repository;
    private ServiceTypeRepository serviceTypeRepository;
    private ChannelTypeRepository channelTypeRepository;
    private EventTypeRepository eventTypeRepository;
    private DirectionTypeRepository directionTypeRepository;
    private ApplicantTypeRepository applicantTypeRepository;

    @Autowired
    public FeeDtoMapper(
        Jurisdiction1Repository jurisdiction1Repository,
        Jurisdiction2Repository jurisdiction2Repository,
        DirectionTypeRepository directionTypeRepository,
        ServiceTypeRepository serviceTypeRepository,
        ChannelTypeRepository channelTypeRepository,
        EventTypeRepository eventTypeRepository,
        ApplicantTypeRepository applicantTypeRepository) {

        this.jurisdiction1Repository = jurisdiction1Repository;
        this.jurisdiction2Repository = jurisdiction2Repository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.channelTypeRepository = channelTypeRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.directionTypeRepository = directionTypeRepository;
        this.applicantTypeRepository = applicantTypeRepository;
    }

    private void fillFee(FeeDto request, Fee fee, String author) {
        updateFeeDetails(request, fee);

        FeeVersion version = toFeeVersion(request.getVersion(), author);
        version.setFee(fee);
        fee.setFeeVersions(Arrays.asList(version));
        fee.setKeyword(request.getKeyword());
    }

    private void updateFeeDetails(FeeDto request, Fee fee) {
        fillJuridistiction1(fee, request.getJurisdiction1());
        fillJuridistiction2(fee, request.getJurisdiction2());
        fillServiceType(fee, request.getService());
        fillEventType(fee, request.getEvent());
        fillChannelType(fee, request.getChannel());
        fillApplicationType(fee, request.getApplicantType());
    }

    public Fee toFee(FeeDto request, String author) {
        Fee fee = FeeFactory.getFee(request);
        fillFee(request, fee, author);

        if (fee instanceof FixedFee){
            fee.setUnspecifiedClaimAmount(
                request.getUnspecifiedClaimAmount() != null && request.getUnspecifiedClaimAmount()
            );
        }

        return fee;
    }

    public void updateRangedFee(RangedFeeDto request, RangedFee fee, String author) {
        updateFeeDetails(request, fee);

        FeeVersion currentVersion = fee.getCurrentVersion(true);
        fillFeeVersionDetails(request.getVersion(), currentVersion, author);
    }

    public void updateFixedFee(FixedFeeDto request, FixedFee fee, String author) {
        updateFeeDetails(request, fee);

        FeeVersion currentVersion = fee.getCurrentVersion(true);
        fillFeeVersionDetails(request.getVersion(), currentVersion, author);
    }

    public Fee toFee(RangedFeeDto request, String author) {
        RangedFee fee = new RangedFee();
        fillFee(request, fee, author);

        fee.setUnspecifiedClaimAmount(false);
        fee.setMaxRange(request.getMaxRange());
        fee.setMinRange(request.getMinRange());

        if(request.getRangeUnit() == null) {
            request.setRangeUnit("GBP");
        }

        fee.setRangeUnit(new RangeUnit(request.getRangeUnit()));

        return fee;
    }

    public Fee2Dto toFeeDto(FeeVersion version) {
        Fee2Dto fee2Dto = toFeeDto(version.getFee());
        FeeVersionDto feeVersionDto = toFeeVersionDto(version);
        fee2Dto.setCurrentVersion(feeVersionDto);
        fee2Dto.setMatchingVersion(feeVersionDto);
        return fee2Dto;
    }

    public Fee2Dto toFeeDto(Fee fee) {
        Fee2Dto fee2Dto = new Fee2Dto();

        fee2Dto.setCode(fee.getCode());

        fee2Dto.setFeeType(fee.getTypeCode());

        fee2Dto.setChannelTypeDto(fee.getChannelType());

        fee2Dto.setEventTypeDto(fee.getEventType());
        fee2Dto.setJurisdiction1Dto(fee.getJurisdiction1());
        fee2Dto.setJurisdiction2Dto(fee.getJurisdiction2());
        fee2Dto.setServiceTypeDto(fee.getService());
        fee2Dto.setApplicantTypeDto(fee.getApplicantType());

        fee2Dto.setUnspecifiedClaimAmount(fee.isUnspecifiedClaimAmount());

        if (fee instanceof RangedFee) {

            RangedFee rangedFee = (RangedFee) fee;

            fee2Dto.setMinRange(rangedFee.getMinRange());
            fee2Dto.setMaxRange(rangedFee.getMaxRange());

            if(rangedFee.getRangeUnit() != null) {
                fee2Dto.setRangeUnit(rangedFee.getRangeUnit().getName());
            }

        }

        List<FeeVersionDto> feeVersionDtos = fee.getFeeVersions().stream().map(this::toFeeVersionDto).collect(Collectors.toList());
        fee2Dto.setFeeVersionDtos(feeVersionDtos);

        FeeVersion currentVersion = fee.getCurrentVersion(false);

        if(currentVersion != null) {
            fee2Dto.setCurrentVersion(toFeeVersionDto(currentVersion));
        }

        return fee2Dto;
    }

    public FeeVersion toFeeVersion(FeeVersionDto versionDto, String author) {

        if(versionDto == null){
            throw new BadRequestException("Version is required");
        }

        FeeVersion version = new FeeVersion();
        fillFeeVersionDetails(versionDto, version, author);

        return version;
    }

    private void fillFeeVersionDetails(FeeVersionDto versionDto, FeeVersion version, String author) {
        version.setValidFrom(versionDto.getValidFrom());
        version.setValidTo(versionDto.getValidTo());

        version.setMemoLine(versionDto.getMemoLine());
        version.setFeeOrderName(versionDto.getFeeOrderName());
        version.setNaturalAccountCode(versionDto.getNaturalAccountCode());
        version.setStatutoryInstrument(versionDto.getStatutoryInstrument());
        version.setSiRefId(versionDto.getSiRefId());
        fillDirectionType(version, versionDto.getDirection());


        fillVersionStatus(version, versionDto.getStatus());
        fillVersionVersion(version, versionDto.getVersion());

        version.setDescription(versionDto.getDescription());

        if(versionDto.getFlatAmount() != null) {
            version.setAmount(toFlatAmount(versionDto.getFlatAmount()));
        }else if(versionDto.getPercentageAmount() != null) {
            version.setAmount(toPercentageAmount(versionDto.getPercentageAmount()));
        }else if(versionDto.getVolumeAmount() != null) {
            version.setAmount(toVolumeAmount(versionDto.getVolumeAmount()));
        }

        version.setAuthor(author);

        if(version.getStatus() == FeeVersionStatus.approved){
            version.setApprovedBy(author);
        }
    }

    public Fee toFee(LoaderRangedFeeDto request, String author) {
        RangedFee fee = new RangedFee();
        fillFee(request, fee, author);

        fee.setUnspecifiedClaimAmount(false);
        fee.setMaxRange(request.getMaxRange());
        fee.setMinRange(request.getMinRange());

        if(request.getRangeUnit() == null) {
            request.setRangeUnit("GBP");
        }

        fee.setRangeUnit(new RangeUnit(request.getRangeUnit()));

        return fee;
    }

    public FeeVersionDto toFeeVersionDto(FeeVersion feeVersion) {

        FeeVersionDto feeVersionDto = new FeeVersionDto();

        feeVersionDto.setValidFrom(feeVersion.getValidFrom());
        feeVersionDto.setValidTo(feeVersion.getValidTo());

        feeVersionDto.setVersion(feeVersion.getVersion());
        feeVersionDto.setStatus(feeVersion.getStatus());
        feeVersionDto.setDescription(feeVersion.getDescription());

        feeVersionDto.setMemoLine(feeVersion.getMemoLine());
        if (feeVersion.getDirectionType() != null) {
            feeVersionDto.setDirection(feeVersion.getDirectionType().getName());
        }

        feeVersionDto.setNaturalAccountCode(feeVersion.getNaturalAccountCode());
        feeVersionDto.setFeeOrderName(feeVersion.getFeeOrderName());
        feeVersionDto.setStatutoryInstrument(feeVersion.getStatutoryInstrument());
        feeVersionDto.setSiRefId(feeVersion.getSiRefId());

        // map the amount
        if (feeVersion.getAmount() instanceof FlatAmount) {
            FlatAmountDto flatAmountDto = new FlatAmountDto();
            flatAmountDto.setAmount(((FlatAmount) feeVersion.getAmount()).getAmount());
            feeVersionDto.setFlatAmount(flatAmountDto);
        }else

        if (feeVersion.getAmount() instanceof PercentageAmount) {
            PercentageAmountDto percentageAmountDto = new PercentageAmountDto();
            percentageAmountDto.setPercentage(((PercentageAmount) feeVersion.getAmount()).getPercentage());
            feeVersionDto.setPercentageAmount(percentageAmountDto);
        }else

        if(feeVersion.getAmount() instanceof  VolumeAmount) {
            VolumeAmountDto volumeAmountDto = new VolumeAmountDto(((VolumeAmount) feeVersion.getAmount()).getAmount());
            feeVersionDto.setVolumeAmount(volumeAmountDto);
        }

        feeVersionDto.setAuthor(feeVersion.getAuthor());
        feeVersionDto.setApprovedBy(feeVersion.getApprovedBy());

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

    private VolumeAmount toVolumeAmount(VolumeAmountDto dto) {
        VolumeAmount amount = new VolumeAmount();
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
            version.setStatus(FeeVersionStatus.draft);
        }else{
            version.setStatus(status);
        }
    }

    private void fillChannelType(Fee fee, String channel) {

        ChannelType channelType = channel == null ?
            channelTypeRepository.findByNameOrThrow(ChannelType.DEFAULT) :
            channelTypeRepository.findByNameOrThrow(channel.toLowerCase());

        fee.setChannelType(channelType);

    }

    /* --- */
    private void fillJuridistiction1(Fee fee, String jurisdiction1) {
        if(jurisdiction1 != null) {
            fee.setJurisdiction1(jurisdiction1Repository.findByNameOrThrow(jurisdiction1.toLowerCase()));
        }
    }

    private void fillJuridistiction2(Fee fee, String jurisdiction2) {
        if(jurisdiction2 != null) {
            fee.setJurisdiction2(jurisdiction2Repository.findByNameOrThrow(jurisdiction2.toLowerCase()));
        }
    }

    private void fillEventType(Fee fee, String event) {

        if(event == null) {
            return;
        }

        fee.setEventType(eventTypeRepository.findByNameOrThrow(event.toLowerCase()));
    }

    private void fillDirectionType(FeeVersion feeVersion, String direction) {

        if(direction == null) {
            return;
        }

        feeVersion.setDirectionType(directionTypeRepository.findByNameOrThrow(direction.toLowerCase()));

    }

    private void fillServiceType(Fee fee, String service) {

        if(service == null) {
            return;
        }

        fee.setService(serviceTypeRepository.findByNameOrThrow(service.toLowerCase()));
    }

    private void fillApplicationType(Fee fee, String application) {

        if(application == null) {
            return;
        }

        fee.setApplicantType(applicantTypeRepository.findByNameOrThrow(application.toLowerCase()));
    }

}
