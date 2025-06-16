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
@PactBroker(scheme = "${PACT_BROKER_SCHEME:http}", host = "${PACT_BROKER_URL:localhost}", port = "${PACT_BROKER_PORT:80}")
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
    public void requestForProbateAndDivorceFees() {

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
        LookupFeeDto divorceAmendPetitionLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("other")
            .keyword("DivorceAmendPetition")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto divorceAmendPetitionFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0233",
            "Amendment of application for matrimonial/civil partnership orde",
            1,
            new BigDecimal("95.00"));

        when(feeService.lookup(divorceAmendPetitionLookupFeeDto))
            .thenReturn(divorceAmendPetitionFeeLookupResponseDto);

        LookupFeeDto generalAppWithoutNoticeLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .keyword("GeneralAppWithoutNotice")
            .service("other")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto generalAppWithoutNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0228",
            "Application (without notice)",
            1,
            new BigDecimal("50.00"));

        when(feeService.lookup(generalAppWithoutNoticeLookupFeeDto))
            .thenReturn(generalAppWithoutNoticeFeeLookupResponseDto);

        LookupFeeDto appnPrivateOtherLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("other")
            .keyword("AppnPrivateOther")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto appnPrivateOtherFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0388",
            "Originating proceedings where no other fee is specified",
            1,
            new BigDecimal("245.00"));

        when(feeService.lookup(appnPrivateOtherLookupFeeDto))
            .thenReturn(appnPrivateOtherFeeLookupResponseDto);

        LookupFeeDto bailiffServeDocLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("enforcement")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("other")
            .keyword("BailiffServeDoc")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto bailiffServeDocFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0392",
            "Request for service by a bailiff of document (see order for exceptions)",
            2,
            new BigDecimal("45.00"));

        when(feeService.lookup(bailiffServeDocLookupFeeDto))
            .thenReturn(bailiffServeDocFeeLookupResponseDto);

        LookupFeeDto financialOrderOnNoticeLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("other")
            .keyword("FinancialOrderOnNotice")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto financialOrderOnNoticeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0229",
            "Application for a financial orde",
            1,
            new BigDecimal("255.00"));

        when(feeService.lookup(financialOrderOnNoticeLookupFeeDto))
            .thenReturn(financialOrderOnNoticeFeeLookupResponseDto);

        LookupFeeDto gaContestedOrderLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("other")
            .keyword("GAContestedOrder")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto gaContestedOrderFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0271",
            "Application for decree nisi, conditional order, separation order (no fee if undefended",
            1,
            new BigDecimal("50.00"));

        when(feeService.lookup(gaContestedOrderLookupFeeDto))
            .thenReturn(gaContestedOrderFeeLookupResponseDto);

        LookupFeeDto divorceCivPartLookupFeeDto = LookupFeeDto.lookupWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("divorce")
            .keyword("DivorceCivPart")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto divorceCivPartFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0002",
            "Filing an application for a divorce, nullity or civil partnership dissolution – fees order 1.2.",
            4,
            new BigDecimal("550.00"));

        when(feeService.lookup(divorceCivPartLookupFeeDto))
            .thenReturn(divorceCivPartFeeLookupResponseDto);

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
    public void requestForHearingPaper() {

        FeeLookupResponseDto hearingPaperFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0372",
            "Appeal determined without a hearing",
            2,
            new BigDecimal("80.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(hearingPaperFeeLookupResponseDto);
    }

    @State("Fees exist for CCD")
    public void requestForCCD() {

        LookupFeeDto careOrderLookupFeeDto = LookupFeeDto.lookupWith()
            .service("public law")
            .channel("default")
            .jurisdiction2("family court")
            .event("issue")
            .keyword("CareOrder")
            .jurisdiction1("family")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto careOrderFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0314",
            "Application for proceedings under Section 31 of Act",
            1,
            new BigDecimal("2055.00"));

        when(feeService.lookup(careOrderLookupFeeDto))
            .thenReturn(careOrderFeeLookupResponseDto);

        LookupFeeDto epoLookupFeeDto = LookupFeeDto.lookupWith()
            .service("private law")
            .channel("default")
            .jurisdiction2("family court")
            .event("miscellaneous")
            .keyword("EPO")
            .jurisdiction1("family")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto epoFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0326",
            "Emergency protection orders (sections 44, 45 and 46)",
            1,
            new BigDecimal("215.00"));

        when(feeService.lookup(epoLookupFeeDto))
            .thenReturn(epoFeeLookupResponseDto);

        LookupFeeDto placementLookupFeeDto = LookupFeeDto.lookupWith()
            .service("adoption")
            .channel("default")
            .jurisdiction2("family court")
            .event("miscellaneous")
            .keyword("Placement")
            .jurisdiction1("family")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto placementFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0310",
            "Application for a placement order (under Section 22)",
            1,
            new BigDecimal("455.00"));

        when(feeService.lookup(placementLookupFeeDto))
            .thenReturn(placementFeeLookupResponseDto);

        LookupFeeDto variationDischargeLookupFeeDto = LookupFeeDto.lookupWith()
            .service("private law")
            .channel("default")
            .jurisdiction2("family court")
            .event("miscellaneous")
            .keyword("VariationDischarge")
            .jurisdiction1("family")
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .build();

        FeeLookupResponseDto variationDisChargeFeeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0328",
            "Variation or discharge etc of care and supervision orders (section 39)",
            1,
            new BigDecimal("215.00"));

        when(feeService.lookup(variationDischargeLookupFeeDto))
            .thenReturn(variationDisChargeFeeLookupResponseDto);
    }

    @State("Copies fee exist for Probate")
    public void requestsForCopiesProbate() {

        FeeLookupResponseDto feeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0544",
            "Copy of a document (for each copy)",
            7,
            new BigDecimal("0.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(feeLookupResponseDto);
    }

    @State("Fees exist for Probate")
    public void requestsForFeesProbate() {

        FeeLookupResponseDto feeLookupResponseDto = new FeeLookupResponseDto(
            "FEE0123",
            "description",
            1,
            new BigDecimal("200.00"));

        when(feeService.lookup(ArgumentMatchers.any(LookupFeeDto.class)))
            .thenReturn(feeLookupResponseDto);
    }
}
