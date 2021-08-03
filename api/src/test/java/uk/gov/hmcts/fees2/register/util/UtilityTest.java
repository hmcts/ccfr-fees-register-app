package uk.gov.hmcts.fees2.register.util;

import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.data.model.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UtilityTest {

    public static FeeVersionDto buildFeeVersionDto() throws ParseException {
        final FeeVersionDto feeVersionDto = new FeeVersionDto();
        feeVersionDto.setVersion(1);
        feeVersionDto.setApprovedBy("EEE");
        feeVersionDto.setAuthor("FFF");
        feeVersionDto.setDescription("GGG");
        feeVersionDto.setDirection("HHH");
        feeVersionDto.setLastAmendingSi("III");
        feeVersionDto.setConsolidatedFeeOrderName("KKK");
        final FlatAmountDto flatAmountDto = new FlatAmountDto();
        flatAmountDto.setAmount(new BigDecimal("111"));
        feeVersionDto.setFlatAmount(flatAmountDto);

        feeVersionDto.setMemoLine("JJJ");
        feeVersionDto.setNaturalAccountCode("KKK");

        final PercentageAmountDto percentageAmountDto = new PercentageAmountDto();
        percentageAmountDto.setPercentage(new BigDecimal("222"));
        feeVersionDto.setPercentageAmount(percentageAmountDto);

        feeVersionDto.setReasonForReject("LLL");
        feeVersionDto.setReasonForUpdate("MMM");
        feeVersionDto.setSiRefId("NNN");

        feeVersionDto.setStatus(FeeVersionStatusDto.approved);
        feeVersionDto.setStatutoryInstrument("OOO");
        feeVersionDto.setValidFrom(new SimpleDateFormat("dd MMMM yyyy").parse("05 May 2021"));
        feeVersionDto.setValidTo(new SimpleDateFormat("dd MMMM yyyy").parse("06 June 2021"));

        final VolumeAmountDto volumeAmountDto = new VolumeAmountDto();
        volumeAmountDto.setAmount(new BigDecimal("333"));
        feeVersionDto.setVolumeAmount(volumeAmountDto);

        return feeVersionDto;
    }

    public static FeeVersion buildFeeVersion() throws ParseException {
        final FeeVersion feeVersion = new FeeVersion();
        feeVersion.setVersion(1);
        feeVersion.setApprovedBy("EEE");
        feeVersion.setAuthor("FFF");
        feeVersion.setDescription("GGG");

        final DirectionType directionType = new DirectionType();
        directionType.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("06 June 2021"));
        directionType.setName("XX");
        directionType.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("06 June 2021"));
        feeVersion.setDirectionType(directionType);

        feeVersion.setLastAmendingSi("III");
        feeVersion.setConsolidatedFeeOrderName("KKK");

        final FlatAmountDto flatAmountDto = new FlatAmountDto();
        flatAmountDto.setAmount(new BigDecimal("111"));

        feeVersion.setMemoLine("JJJ");
        feeVersion.setNaturalAccountCode("KKK");

        final PercentageAmountDto percentageAmountDto = new PercentageAmountDto();
        percentageAmountDto.setPercentage(new BigDecimal("222"));

        feeVersion.setReasonForReject("LLL");
        feeVersion.setReasonForUpdate("MMM");
        feeVersion.setSiRefId("NNN");

        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setStatutoryInstrument("OOO");
        feeVersion.setValidFrom(new SimpleDateFormat("dd MMMM yyyy").parse("05 May 2021"));
        feeVersion.setValidTo(new SimpleDateFormat("dd MMMM yyyy").parse("06 June 2021"));

        final VolumeAmountDto volumeAmountDto = new VolumeAmountDto();
        volumeAmountDto.setAmount(new BigDecimal("333"));

        return feeVersion;
    }

    public static Fee2Dto buildFee2Dto() throws ParseException {

        final Fee2Dto fee2Dto = new Fee2Dto();

        fee2Dto.setCode("AAA");

        final ApplicantTypeDto applicantTypeDto = new ApplicantTypeDto();

        applicantTypeDto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("01 January 2021"));
        applicantTypeDto.setName("BBB");
        applicantTypeDto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("02 February 2021"));
        fee2Dto.setApplicantTypeDto(applicantTypeDto);

        final ChannelTypeDto channelTypeDto = new ChannelTypeDto();
        channelTypeDto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("03 March 2021"));
        channelTypeDto.setName("CCC");
        channelTypeDto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("04 April 2021"));
        fee2Dto.setChannelTypeDto(channelTypeDto);

        fee2Dto.setFeeType("DDD");

        final List<FeeVersionDto> feeVersionDtoList = new ArrayList<>();
        feeVersionDtoList.add(buildFeeVersionDto());
        fee2Dto.setFeeVersionDtos(feeVersionDtoList);

        final EventTypeDto eventTypeDto = new EventTypeDto();
        eventTypeDto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("07 July 2021"));
        eventTypeDto.setName("PPP");
        eventTypeDto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("08 August 2021"));
        fee2Dto.setEventTypeDto(eventTypeDto);

        final Jurisdiction1Dto jurisdiction1Dto = new Jurisdiction1Dto();
        jurisdiction1Dto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("09 September 2021"));
        jurisdiction1Dto.setName("QQQ");
        jurisdiction1Dto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("10 October 2021"));
        fee2Dto.setJurisdiction1Dto(jurisdiction1Dto);

        final Jurisdiction2Dto jurisdiction2Dto = new Jurisdiction2Dto();
        jurisdiction2Dto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("11 November 2021"));
        jurisdiction2Dto.setName("RRR");
        jurisdiction2Dto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("12 December 2021"));
        fee2Dto.setJurisdiction2Dto(jurisdiction2Dto);

        fee2Dto.setCurrentVersion(buildFeeVersionDto());
        fee2Dto.setKeyword("SSS");
        fee2Dto.setMatchingVersion(buildFeeVersionDto());
        fee2Dto.setMaxRange(new BigDecimal("444"));
        fee2Dto.setMinRange(new BigDecimal("555"));
        fee2Dto.setRangeUnit("TTT");

        final ServiceTypeDto serviceTypeDto = new ServiceTypeDto();
        serviceTypeDto.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("01 January 2020"));
        serviceTypeDto.setName("UUU");
        serviceTypeDto.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("02 February 2020"));
        fee2Dto.setServiceTypeDto(serviceTypeDto);

        fee2Dto.setUnspecifiedClaimAmount(true);

        return fee2Dto;
    }

    public static Fee buildFixedFee() throws ParseException {

        final FixedFee fee = new FixedFee();

        fee.setCode("AAA");

        final ApplicantType applicantType = new ApplicantType();
        applicantType.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("01 January 2021"));
        applicantType.setName("BBB");
        applicantType.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("02 February 2021"));
        fee.setApplicantType(applicantType);

        final ChannelType channelType = new ChannelType();
        channelType.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("03 March 2021"));
        channelType.setName("CCC");
        channelType.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("04 April 2021"));
        fee.setChannelType(channelType);

        final List<FeeVersion> feeVersionDtoList = new ArrayList<>();
        feeVersionDtoList.add(buildFeeVersion());
        fee.setFeeVersions(feeVersionDtoList);

        final EventType eventType = new EventType();
        eventType.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("07 July 2021"));
        eventType.setName("PPP");
        eventType.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("08 August 2021"));
        fee.setEventType(eventType);

        final Jurisdiction1 jurisdiction1 = new Jurisdiction1();
        jurisdiction1.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("09 September 2021"));
        jurisdiction1.setName("QQQ");
        jurisdiction1.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("10 October 2021"));
        fee.setJurisdiction1(jurisdiction1);

        final Jurisdiction2 jurisdiction2 = new Jurisdiction2();
        jurisdiction2.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("11 November 2021"));
        jurisdiction2.setName("RRR");
        jurisdiction2.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("12 December 2021"));
        fee.setJurisdiction2(jurisdiction2);

        fee.setKeyword("SSS");

        final ServiceType serviceType = new ServiceType();
        serviceType.setCreationTime(new SimpleDateFormat("dd MMMM yyyy").parse("01 January 2020"));
        serviceType.setName("UUU");
        serviceType.setLastUpdated(new SimpleDateFormat("dd MMMM yyyy").parse("02 February 2020"));
        fee.setService(serviceType);

        fee.setUnspecifiedClaimAmount(true);

        return fee;
    }

}
