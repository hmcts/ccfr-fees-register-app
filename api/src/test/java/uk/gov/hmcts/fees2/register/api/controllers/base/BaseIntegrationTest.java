package uk.gov.hmcts.fees2.register.api.controllers.base;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.util.URIUtils;
import uk.gov.hmcts.reform.auth.checker.core.user.UserRequestAuthorizer;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseIntegrationTest extends BaseTest {

    @Autowired
    FeeService feeService;

    @Autowired
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(9190);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    /* --- API CALLS --- */

    protected ResultActions getFeeAndExpectStatusIsOk(String code) throws Exception {
        return restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "getFee"), code)
            .andExpect(status().isOk());
    }

    protected ResultActions deleteFee(String code) throws Exception {
        return restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), code)
            .andExpect(status().isNoContent());
    }

    protected void forceDeleteFee(String code) {
        feeService.delete(code);
    }

    protected String saveFeeAndCheckStatusIsCreated(FeeDto dto) throws Exception {

        String methodName = getMethodName(dto);

        return restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, methodName),
                dto
            )
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
    }



    protected String createFee(FeeDto dto) throws Exception {
        String methodName = getMethodName(dto);
        return restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, methodName),
                dto
            )
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location").split("/")[3];
    }

    protected ResultActions saveFee(FeeDto dto) throws Exception {

        String methodName = getMethodName(dto);

        return restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, methodName),
                dto
            );

    }

    private String getMethodName(FeeDto dto) {
        String methodName = null;
        if (dto instanceof FixedFeeDto) {
            methodName = "createFixedFee";
        }
        if (dto instanceof RangedFeeDto) {
            methodName = "createRangedFee";
        }
        if (dto instanceof RateableFeeDto) {
            methodName = "createRateableFee";
        }
        if (dto instanceof RelationalFeeDto) {
            methodName = "createRelationalFee";
        }
        if (dto instanceof BandedFeeDto) {
            methodName = "createBandedFee";
        }
        return methodName;
    }


    protected ResultActions lookup(LookupFeeDto lookupFeeDto) throws Exception {

        String method = lookupFeeDto.getUnspecifiedClaimAmount() != null &&
            lookupFeeDto.getUnspecifiedClaimAmount() ? "lookupUnspecified" : "lookup";

        HttpHeaders httpHeaders = new HttpHeaders();

        String token = UUID.randomUUID().toString();
        userRequestAuthorizer.registerToken(token, "admin");
        httpHeaders.add(UserRequestAuthorizer.AUTHORISATION, token);

        MockHttpServletRequestBuilder lookup = MockMvcRequestBuilders
            .get(URIUtils.getUrlForGetMethod(FeeController.class, method))
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .headers(httpHeaders);

        if (lookupFeeDto.getChannel() != null) {
            lookup = lookup.param("channel", lookupFeeDto.getChannel());
        }

        if (lookupFeeDto.getService() != null) {
            lookup = lookup.param("service", lookupFeeDto.getService());
        }

        if (lookupFeeDto.getJurisdiction1() != null) {
            lookup = lookup.param("jurisdiction1", lookupFeeDto.getJurisdiction1());
        }

        if (lookupFeeDto.getJurisdiction2() != null) {
            lookup = lookup.param("jurisdiction2", lookupFeeDto.getJurisdiction2());
        }

        if (lookupFeeDto.getEvent() != null) {
            lookup = lookup.param("event", lookupFeeDto.getEvent());
        }

        if (lookupFeeDto.getApplicantType() != null) {
            lookup = lookup.param("applicant_type", lookupFeeDto.getApplicantType());
        }

        if (lookupFeeDto.getAmountOrVolume() != null) {
            lookup = lookup.param("amount_or_volume", lookupFeeDto.getAmountOrVolume().toString());
        }

        return mvc.perform(lookup);
    }


    protected ResultActions lookupUsingUsingReferenceDataFrom(FeeDto createDto, BigDecimal claimValue) throws Exception {

        String method = createDto.getUnspecifiedClaimAmount() != null &&
            createDto.getUnspecifiedClaimAmount() ? "lookupUnspecified" : "lookup";

        HttpHeaders httpHeaders = new HttpHeaders();

        String token = UUID.randomUUID().toString();
        userRequestAuthorizer.registerToken(token, "admin");
        httpHeaders.add(UserRequestAuthorizer.AUTHORISATION, token);

        MockHttpServletRequestBuilder lookup = MockMvcRequestBuilders
            .get(URIUtils.getUrlForGetMethod(FeeController.class, method))
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .headers(httpHeaders);

        if (createDto.getUnspecifiedClaimAmount() == null || !createDto.getUnspecifiedClaimAmount()) {
            lookup = lookup.param("amount_or_volume", claimValue.toString());
        }

        if (createDto.getChannel() != null) {
            lookup = lookup.param("channel", createDto.getChannel());
        }

        if (createDto.getService() != null) {
            lookup = lookup.param("service", createDto.getService());
        }

        if (createDto.getJurisdiction1() != null) {
            lookup = lookup.param("jurisdiction1", createDto.getJurisdiction1());
        }

        if (createDto.getJurisdiction2() != null) {
            lookup = lookup.param("jurisdiction2", createDto.getJurisdiction2());
        }

        if (createDto.getEvent() != null) {
            lookup = lookup.param("event", createDto.getEvent());
        }

        if (createDto.getApplicantType() != null) {
            lookup = lookup.param("applicant_type", createDto.getApplicantType());
        }

        return mvc.perform(lookup);

    }

    /* --- RESULT MATCHERS --- */

    protected ResultMatcher versionIsOneAndStatusIsDraft() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            FeeVersionDto v = feeDto.getFeeVersionDtos().get(0);
            assertTrue(v.getVersion().equals(new Integer(1)));
            assertTrue(v.getStatus() == FeeVersionStatusDto.draft);
        });
    }

    protected ResultMatcher channelIsDefault() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            assertTrue(feeDto.getChannelTypeDto().getName().equals(ChannelType.DEFAULT));
        });
    }

    protected ResultMatcher lookupResultMatchesFee(FeeDto feeDto) {
        return body().as(FeeLookupResponseDto.class, (res) -> {
            assertTrue(feeDto.getCode().equalsIgnoreCase(res.getCode()));
            assertTrue(res.getVersion() != null);
        });
    }

    protected ResultMatcher lookupResultMatchesExpectedFeeAmount(BigDecimal feeAmount) {
        return body().as(FeeLookupResponseDto.class, (res) -> {
            assertTrue(feeAmount.compareTo(res.getFeeAmount()) == 0);
        });
    }

    protected ResultMatcher isUnspecifiedAmountFee() {
        return body().as(Fee2Dto.class, (res) -> {
            assertTrue(res.isUnspecifiedClaimAmount());
        });
    }

    /* --- DTO BUILDERS --- */

    protected FixedFeeDto createCMCIssueCivilCountyFixedFee() {
        return new FixedFeeDto()
            .setService("civil money claims")
            .setEvent("issue")
            .setJurisdiction1("civil")
            .setJurisdiction2("family court")
            .setApplicantType("all");
    }

    protected FixedFeeDto createDivorceIssueFamilyFixedFee() {
        return new FixedFeeDto()
            .setService("divorce")
            .setEvent("issue")
            .setJurisdiction1("family")
            .setJurisdiction2("family court")
            .setApplicantType("all");
    }

    protected void mockIdamAPI() {
        stubFor(get(urlPathMatching("/api/v1/users"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(contentsOf("idam-responses/userinfo-response.json"))));
    }

    @SneakyThrows
    protected String contentsOf(String fileName) {
        String content = new String(Files.readAllBytes(Paths.get(ResourceUtils.getURL("classpath:" + fileName).toURI())));
        return resolvePlaceholders(content);
    }

    protected String resolvePlaceholders(String content) {
        return configurableListableBeanFactory.resolveEmbeddedValue(content);
    }

}
