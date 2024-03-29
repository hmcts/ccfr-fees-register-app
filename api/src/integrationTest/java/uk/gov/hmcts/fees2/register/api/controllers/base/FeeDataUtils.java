package uk.gov.hmcts.fees2.register.api.controllers.base;

import org.joda.time.DateTime;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto.feeVersionDtoWith;
import static uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto.rangedFeeDtoWith;

public class FeeDataUtils {

    public static FixedFeeDto getCreateProbateCopiesFeeRequest() {
        return fixedFeeDtoWith()
            .channel("default")
            .event("copies")
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .service("probate")
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.511Z").toDate())
                .description("Additional copies of the grant representation")
                .status(FeeVersionStatusDto.approved)
                .memoLine("Additional sealed copy of grant")
                .direction("enhanced")
                .naturalAccountCode("4481102171")
                .siRefId("8b")
                .statutoryInstrument("2014 No 876(L19)")
                .lastAmendingSi("Non-Contentious Probate Fees")
                .consolidatedFeeOrderName("Non-Contentious Probate Fees")
                .volumeAmount(new VolumeAmountDto(new BigDecimal("0.5")))
                .build())
            .build();
    }

    public static RangedFeeDto getRangedCreateProbateCopiesFeeRequest() {
        return rangedFeeDtoWith()
            .channel("default")
            .event("copies")
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .service("probate")
            .applicantType("all")
            .minRange(BigDecimal.ONE)
            .maxRange(BigDecimal.TEN)
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.511Z").toDate())
                .description("Additional copies of the grant representation")
                .status(FeeVersionStatusDto.approved)
                .memoLine("Additional sealed copy of grant")
                .direction("enhanced")
                .naturalAccountCode("4481102171")
                .siRefId("8b")
                .statutoryInstrument("2014 No 876(L19)")
                .lastAmendingSi("Non-Contentious Probate Fees")
                .consolidatedFeeOrderName("Non-Contentious Probate Fees")
                .volumeAmount(new VolumeAmountDto(new BigDecimal("0.5")))
                .build())
            .build();
    }

    public static RangedFeeDto getCreateRangedFeeRequest() {
        return rangedFeeDtoWith()
            .minRange(new BigDecimal("5000.01"))
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .service("probate")
            .channel("default")
            .event("issue")
            .applicantType("personal")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2011-04-04T00:00:00.000Z").toDate())
                .description("Personal Application for grant of Probate")
                .status(FeeVersionStatusDto.approved)
                .memoLine("Personal Application for grant of Probate")
                .naturalAccountCode("4481102158")
                .direction("enhanced")
                .lastAmendingSi("Non-Contentious Probate Fees")
                .consolidatedFeeOrderName("Non-Contentious Probate Fees")
                .statutoryInstrument("2011 No. 588 (L. 4)")
                .siRefId("2")
                .flatAmount(new FlatAmountDto(new BigDecimal("215.00")))
                .build())
            .build();
    }

    public static FixedFeeDto getCreateFixedFeeRequest() {
        return fixedFeeDtoWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .service("civil money claims")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("Civil Court fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatusDto.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .flatAmount(new FlatAmountDto(new BigDecimal("10000.00")))
                .build())
            .build();
    }

    public static List<FixedFeeDto> getCreateFixedFeesWithKeywordRequest() {
        List<FixedFeeDto> fixedFeeDtos = new ArrayList<>();

        fixedFeeDtos.add(fixedFeeDtoWith()
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("general")
            .keyword("financial-order")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("General fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatusDto.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .reasonForUpdate("Revised Prices for the First Time")
                .flatAmount(new FlatAmountDto(new BigDecimal("199.99")))
                .build())
            .build());

        fixedFeeDtos.add(fixedFeeDtoWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("general")
            .keyword("without-notice")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2016-03-21T00:00:00.000Z").toDate())
                .description("Filing an application for a divorce, nullity or civil partnership dissolution – fees order 1.2.")
                .status(FeeVersionStatusDto.approved)
                .memoLine("GOV - App for divorce/nullity of marriage or CP")
                .direction("enhanced")
                .naturalAccountCode("4481102159")
                .reasonForUpdate("Revised Prices for the Second Time")
                .flatAmount(new FlatAmountDto(new BigDecimal("550.00")))
                .build())
            .build());


        return fixedFeeDtos;
    }

    public static FixedFeeDto getCmcUnspecifiedFee() throws Exception {
        return fixedFeeDtoWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .service("civil money claims")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("Civil Court fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatusDto.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .lastAmendingSi("Non-Contentious Probate Fees")
                .consolidatedFeeOrderName("Non-Contentious Probate Fees")
                .flatAmount(new FlatAmountDto(new BigDecimal("10000.00")))
                .build())
            .build();
    }

    public static List<FixedFeeDto> getIncorrectFixedFeesDto() throws IOException {
        List<FixedFeeDto> fixedFeeDtos = new ArrayList<>();

        fixedFeeDtos.add(fixedFeeDtoWith()
            .jurisdiction1("family")
            .jurisdiction2("court of protection")
            .service("divorce")
            .channel("default")
            .event("issue")
            .version(FeeVersionDto.feeVersionDtoWith()
                .validFrom(DateTime.parse("2017-11-06T16:33:37.040Z").toDate())
                .validTo(DateTime.parse("2020-11-06T16:33:37.040Z").toDate())
                .description("Testing")
                .status(FeeVersionStatusDto.draft)
                .direction("enhanced1")
                .memoLine("Test memo line")
                .lastAmendingSi("CMC online fee order name")
                .consolidatedFeeOrderName("CMC consolidated fee order name")
                .naturalAccountCode("Natural code 001")
                .flatAmount(new FlatAmountDto(new BigDecimal("150.00")))
                .build())
            .build());

        fixedFeeDtos.add(fixedFeeDtoWith()
            .jurisdiction1("family")
            .jurisdiction2("court of protection")
            .service("civil money claims")
            .channel("default")
            .event("issue")
            .version(FeeVersionDto.feeVersionDtoWith()
                .validFrom(DateTime.parse("2017-11-06T16:33:37.040Z").toDate())
                .validTo(DateTime.parse("2020-11-06T16:33:37.040Z").toDate())
                .description("Testing2")
                .status(FeeVersionStatusDto.approved)
                .direction("enhanced")
                .memoLine("Test memo line")
                .lastAmendingSi("CMC online fee order name")
                .consolidatedFeeOrderName("CMC online fee order name")
                .naturalAccountCode("Natural code 002")
                .flatAmount(new FlatAmountDto(new BigDecimal("300.00")))
                .build())
            .build());

        return fixedFeeDtos;
    }
}
