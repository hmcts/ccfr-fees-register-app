package uk.gov.hmcts.fees2.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.auth.checker.user.UserRequestAuthorizer;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
        restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, "createRangedFee"),
                dto
            )
            .andExpect(status().isCreated());
    }

    protected ResultActions lookupUsingCreateFeeDtoReferenceData(CreateFeeDto createDto, BigDecimal claimValue) throws Exception{

        HttpHeaders httpHeaders = new HttpHeaders();

        String token = UUID.randomUUID().toString();
        userRequestAuthorizer.registerToken(token, "admin");
        httpHeaders.add(UserRequestAuthorizer.AUTHORISATION, token);

        MockHttpServletRequestBuilder lookup = MockMvcRequestBuilders
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "lookup"))
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .headers(httpHeaders)
            .param("amount", claimValue.toString());

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
            assertThat(v.getVersion().equals(1));
            assertThat(v.getStatus() == FeeVersionStatus.draft);
        });
    }

    protected ResultMatcher channelIsDefault() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            assertThat(feeDto.getChannelTypeDto().getName().equals(ChannelType.DEFAULT));
        });
    }

    protected ResultMatcher lookupResultMatchesFee(CreateFeeDto feeDto) {
        return body().as(FeeLookupResponseDto.class, (res) -> {
            assertThat(feeDto.getMemoLine().equalsIgnoreCase(res.getDescription()));
            assertThat(feeDto.getCode().equalsIgnoreCase(res.getCode()));
            assertThat(res.getVersion() != null);
        });
    }

    protected ResultMatcher lookupResultMatchesExpectedFeeAmount(BigDecimal feeAmount) {
        return body().as(FeeLookupResponseDto.class, (res) -> {
            assertThat(feeAmount.compareTo(res.getFeeAmount()) == 0);
        });
    }

    /* --- DTO BUILDERS --- */

    protected CreateFixedFeeDto createCMCIssueCivilCountyFixedFee() {
        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");

        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        return dto;
    }

}
