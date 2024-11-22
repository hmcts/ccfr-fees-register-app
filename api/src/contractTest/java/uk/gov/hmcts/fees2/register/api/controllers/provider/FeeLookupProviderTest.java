package uk.gov.hmcts.fees2.register.api.controllers.provider;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("feeRegister_lookUp")
@PactBroker(scheme = "http", host = "localhost", port = "${PACT_BROKER_PORT:80}")
@Import(FeeLookupProviderTestConfiguration.class)
public class FeeLookupProviderTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FeeService feeService;

    @MockBean
    FeeDtoMapper feeDtoMapper;

    @MockBean
    FeeSearchService feeSearchService;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setPrintRequestResponse(true);
        FeeController feeController = new FeeController(feeService, feeDtoMapper, feeSearchService);
        //feeController.setApplicationContext(applicationContext);
        testTarget.setControllers(feeController);
        if (context != null) {
            context.setTarget(testTarget);
        }

        LookupFeeDto fastTrackLookupFeeDto = LookupFeeDto.lookupWith()
            .service("civil money claims")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .channel("default")
            .event("hearing")
            .applicantType(null)
            .amountOrVolume(new BigDecimal("1000"))
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .keyword("FastTrackHrg")
            .build();

        FeeLookupResponseDto fastTrackFeeLookupResponseDto =
            new FeeLookupResponseDto("FEE0441", "Fee Description", 1, new BigDecimal("60.00"));

        when(feeService.lookup(fastTrackLookupFeeDto))
            .thenReturn(fastTrackFeeLookupResponseDto);

        LookupFeeDto hearingSmallClaimsLookupFeeDto = LookupFeeDto.lookupWith()
            .amountOrVolume(new BigDecimal("1000"))
            .channel("default")
            .event("hearing")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .keyword("HearingSmallClaims")
            .service("civil money claims")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto hearingSmallClaimsLookupResponseDto =
            new FeeLookupResponseDto("FEE0443",
            "Fee Description",
            1,
            new BigDecimal("80.00"));

        when(feeService.lookup(hearingSmallClaimsLookupFeeDto))
            .thenReturn(hearingSmallClaimsLookupResponseDto);

        LookupFeeDto multiTrackLookupFeeDto = LookupFeeDto.lookupWith()
            .amountOrVolume(new BigDecimal("1000"))
            .channel("default")
            .event("hearing")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .keyword("MultiTrackHrg")
            .service("civil money claims")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto multiTrackFeeLookupResponseDto =
            new FeeLookupResponseDto("FEE02", "Fee Description", 1, new BigDecimal("70.00"));

        when(feeService.lookup(multiTrackLookupFeeDto))
            .thenReturn(multiTrackFeeLookupResponseDto);

        LookupFeeDto appnToVaryOrSuspendlookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("AppnToVaryOrSuspend")
            .service("other")
            .direction(null)
            .applicantType(null)
            .amountOrVolume(null)
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .author(null)
            .build();

        FeeLookupResponseDto appnToVaryOrSuspendFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0447",
            "Fee Description",
            1,
            new BigDecimal("120.00"));

        when(feeService.lookup(appnToVaryOrSuspendlookupFeeDto))
            .thenReturn(appnToVaryOrSuspendFeeLookupResponseDto);

        LookupFeeDto generalAppWithoutNoticeLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("GeneralAppWithoutNotice")
            .service("general")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto generalAppWithoutNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0012",
            "Fee Description",
            1,
            new BigDecimal("20.00"));

        when(feeService.lookup(generalAppWithoutNoticeLookupFeeDto))
            .thenReturn(generalAppWithoutNoticeFeeLookupResponseDto);

        LookupFeeDto gaOnNoticeLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("GAOnNotice")
            .service("general")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto gaOnNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0445",
            "Fee Description",
            1,
            new BigDecimal("100.00"));

        when(feeService.lookup(gaOnNoticeLookupFeeDto))
            .thenReturn(gaOnNoticeFeeLookupResponseDto);

        LookupFeeDto moneyClaimLookupFeeDto = LookupFeeDto.lookupWith()
            .amountOrVolume(new BigDecimal("1000.00"))
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .keyword("MoneyClaim")
            .service("civil money claims")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto moneyClaimFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0443",
            "Fee Description",
            1,
            new BigDecimal("80.00"));

        when(feeService.lookup(moneyClaimLookupFeeDto))
            .thenReturn(moneyClaimFeeLookupResponseDto);

        LookupFeeDto moneyClaimWithoutKeywordLookupFeeDto = LookupFeeDto.lookupWith()
            .amountOrVolume(new BigDecimal("1000.00"))
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .service("civil money claims")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto moneyClaimWithoutKeywordFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0033",
            "Fee Description",
            1,
            new BigDecimal("00.00"));

        when(feeService.lookup(moneyClaimWithoutKeywordLookupFeeDto))
            .thenReturn(moneyClaimWithoutKeywordFeeLookupResponseDto);

        LookupFeeDto multiTrackHrgLookupFeeDto = LookupFeeDto.lookupWith()
            .amountOrVolume(new BigDecimal("1000"))
            .channel("default")
            .event("hearing")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("MultiTrackHrg")
            .service("civil money claims")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto multiTrackHrgFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0442",
            "Fee Description",
            1,
            new BigDecimal("70.00"));

        when(feeService.lookup(multiTrackHrgLookupFeeDto))
            .thenReturn(multiTrackHrgFeeLookupResponseDto);
    }

    @State("Fees exist for PRL")
    public void requestForPRLFees() {

        LookupFeeDto lookupFeeDto = LookupFeeDto.lookupWith()
            .service("private law")
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .keyword("ChildArrangement")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto multiTrackHrgFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0336",
            "Section 8 orders (section 10(1) or (2))",
            2,
            new BigDecimal("232.00"));

        when(feeService.lookup(lookupFeeDto))
            .thenReturn(multiTrackHrgFeeLookupResponseDto);
    }

    @State("service is registered in Fee registry")
    public void requestForProbateFees() {

        LookupFeeDto probateFeeDto = LookupFeeDto.lookupWith()
            .service("probate")
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .keyword("Caveat")
            .applicantType("all")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto probateFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0336",
            "Section 8 orders (section 10(1) or (2))",
            2,
            new BigDecimal("232.00"));

        when(feeService.lookup(probateFeeDto))
            .thenReturn(probateFeeLookupResponseDto);
    }

    @State("General Application fees exist")
    public void requestForCivilGAVaryOrSuspend() {

        LookupFeeDto appnToVaryOrSuspendLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("AppnToVaryOrSuspend")
            .service("other")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto appnToVaryOrSuspendFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0013",
            "Fee Description",
            1,
            new BigDecimal("30.00"));

        when(feeService.lookup(appnToVaryOrSuspendLookupFeeDto))
            .thenReturn(appnToVaryOrSuspendFeeLookupResponseDto);

        LookupFeeDto consentLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("GeneralAppWithoutNotice")
            .service("general")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto consentFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0012",
            "Fee Description",
            1,
            new BigDecimal("20.00"));

        when(feeService.lookup(consentLookupFeeDto))
            .thenReturn(consentFeeLookupResponseDto);

        LookupFeeDto hacfoLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("HACFOOnNotice")
            .service("general")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto hacfoLookupResponseDto = new FeeLookupResponseDto(
            "FEE0011",
            "Fee Description",
            1,
            new BigDecimal("10.00"));

        when(feeService.lookup(hacfoLookupFeeDto))
            .thenReturn(hacfoLookupResponseDto);

        LookupFeeDto gaLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("civil")
            .jurisdiction2("civil")
            .keyword("GAOnNotice")
            .service("general")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto gaLookupResponseDto = new FeeLookupResponseDto(
            "FEE0012",
            "Fee Description",
            1,
            new BigDecimal("20.00"));

        when(feeService.lookup(gaLookupFeeDto))
            .thenReturn(gaLookupResponseDto);
    }

    @State("Consented Fees exist for Financial Remedy")
    public void requestForGeneralAppWithoutNotice() {

        FeeLookupResponseDto generalAppWithoutNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0640",
            "Fee Description",
            1,
            new BigDecimal("200.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(generalAppWithoutNoticeFeeLookupResponseDto);
    }

    @State("Contested Fees exist for Financial Remedy")
    public void requestForFinancialOrderOnNotice() {

        FeeLookupResponseDto financialOrderOnNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0229",
            "Fee Description",
            1,
            new BigDecimal("200.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(financialOrderOnNoticeFeeLookupResponseDto);
    }

    @State("Fees exist for IA")
    public void requestForHearingPaper(){

        FeeLookupResponseDto hearingPaperFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0372",
            "Appeal determined without a hearing",
            2,
            new BigDecimal("80.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(hearingPaperFeeLookupResponseDto);
    }
}
