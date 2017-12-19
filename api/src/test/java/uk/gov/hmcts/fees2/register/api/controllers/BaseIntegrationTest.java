package uk.gov.hmcts.fees2.register.api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.hmcts.auth.checker.user.UserRequestAuthorizer;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseIntegrationTest extends BaseTest{

    /* --- API CALLS --- */

    protected ResultActions getFeeAndExpectStatusIsOk(String code) throws Exception {
        return restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "getFee"), code)
            .andExpect(status().isOk());
    }

    protected ResultActions deleteFee(String code) throws Exception {
        return restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), code);
    }

    protected void saveFeeAndCheckStatusIsCreated(CreateFeeDto dto) throws Exception {

        String methodName = dto instanceof CreateRangedFeeDto ? "createRangedFee" : "createFixedFee";

        restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, methodName),
                dto
            )
            .andExpect(status().isCreated());
    }

    protected ResultActions saveFee(CreateFeeDto dto) throws Exception {

        String methodName = dto instanceof CreateRangedFeeDto ? "createRangedFee" : "createFixedFee";

        return restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, methodName),
                dto
            );

    }

    protected ResultActions lookup(LookupFeeDto lookupFeeDto) throws Exception{

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

        if(lookupFeeDto.getChannel() != null){
            lookup = lookup.param("channel", lookupFeeDto.getChannel());
        }

        if(lookupFeeDto.getService() != null){
            lookup = lookup.param("service", lookupFeeDto.getService());
        }

        if(lookupFeeDto.getJurisdiction1() != null){
            lookup = lookup.param("jurisdiction1", lookupFeeDto.getJurisdiction1());
        }

        if(lookupFeeDto.getJurisdiction2() != null){
            lookup = lookup.param("jurisdiction2", lookupFeeDto.getJurisdiction2());
        }

        if(lookupFeeDto.getEvent() != null){
            lookup = lookup.param("event", lookupFeeDto.getEvent());
        }

        if(lookupFeeDto.getAmountOrVolume() != null){
            lookup = lookup.param("amount_or_volume", lookupFeeDto.getAmountOrVolume().toString());
        }

        return mvc.perform(lookup);
    }


    protected ResultActions lookupUsingUsingReferenceDataFrom(CreateFeeDto createDto, BigDecimal claimValue) throws Exception{

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

        if(createDto.getUnspecifiedClaimAmount() == null || !createDto.getUnspecifiedClaimAmount()){
            lookup = lookup.param("amount_or_volume", claimValue.toString());
        }

        if(createDto.getChannel() != null){
            lookup = lookup.param("channel", createDto.getChannel());
        }

        if(createDto.getService() != null){
            lookup = lookup.param("service", createDto.getService());
        }

        if(createDto.getJurisdiction1() != null){
            lookup = lookup.param("jurisdiction1", createDto.getJurisdiction1());
        }

        if(createDto.getJurisdiction2() != null){
            lookup = lookup.param("jurisdiction2", createDto.getJurisdiction2());
        }

        if(createDto.getEvent() != null){
            lookup = lookup.param("event", createDto.getEvent());
        }

        return mvc.perform(lookup);

    }

    /* --- RESULT MATCHERS --- */

    protected ResultMatcher versionIsOneAndStatusIsDraft() {
        return body().as(Fee2Dto.class, (feeDto) -> {

            FeeVersionDto v = feeDto.getFeeVersionDtos().get(0);
            assertTrue(v.getVersion().equals(1));
            assertTrue(v.getStatus() == FeeVersionStatus.draft);
        });
    }

    protected ResultMatcher channelIsDefault() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            assertTrue(feeDto.getChannelTypeDto().getName().equals(ChannelType.DEFAULT));
        });
    }

    protected ResultMatcher lookupResultMatchesFee(CreateFeeDto feeDto) {
        return body().as(FeeLookupResponseDto.class, (res) -> {
            assertTrue(feeDto.getMemoLine().equalsIgnoreCase(res.getDescription()));
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

    protected CreateFixedFeeDto createCMCIssueCivilCountyFixedFee() {
        return new CreateFixedFeeDto()
        .setService("civil money claims")
        .setEvent("issue")
        .setJurisdiction1("civil")
        .setDirection("license")
        .setJurisdiction2("family court")
        .setCode(String.valueOf(System.currentTimeMillis()))
        .setMemoLine("description");
    }

    protected CreateFixedFeeDto createDivorceIssueFamilyFixedFee() {
        return new CreateFixedFeeDto()
        .setService("divorce")
        .setEvent("issue")
        .setJurisdiction1("family")
        .setJurisdiction2("family court")
        .setDirection("license")
        .setCode(String.valueOf(System.currentTimeMillis()))
        .setMemoLine("description");

    }



}
