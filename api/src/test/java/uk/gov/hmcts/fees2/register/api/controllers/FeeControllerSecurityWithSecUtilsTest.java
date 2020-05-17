package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees2.register.api.FeeTestFixtures.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FeeController.class)
@ContextConfiguration(classes = {TestSecurityUtilsConfiguration.class})
public class FeeControllerSecurityWithSecUtilsTest {

    @MockBean
    private FeeSearchService feeSearchService;
    @MockBean
    private FeeVersionService feeVersionService;
    @MockBean
    private FeeService feeService;
    @MockBean
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testUpdateFixedFee_shouldThrowClassCastExceptionkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        given(feeDtoMapper.toFee(any(FixedFeeDto.class), anyString())).willReturn(aFixedFee());
        given(feeService.save(any(Fee.class))).willReturn(aFixedFee());

        // when & then
        Exception result = null;
        try {
            this.mockMvc.perform(
                put("/fees-register/fixed-fees/testCode")
                    .contentType(APPLICATION_JSON)
                    .content(aRangeFeePayload())
                    .with(authentication(authentication)))
                .andExpect(status().isNoContent());
        } catch (Exception e) {
            result = e;
        }

        Assert.assertTrue(result instanceof ClassCastException);
    }

    @Test
    public void testUpdateFixedFee_shouldReturnNullWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithNullPrincipal("freg-editor");

        given(feeDtoMapper.toFee(any(FixedFeeDto.class), anyString())).willReturn(aFixedFee());
        given(feeService.save(any(Fee.class))).willReturn(aFixedFee());

        // when & then
        Exception result = null;

        this.mockMvc.perform(
            put("/fees-register/fixed-fees/testCode")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isUnauthorized());

    }

    @Test(expected = NullPointerException.class)
    public void testUpdateFixedFee_shouldReturnNullWhenAuthenticationInstanceNull() throws Exception {
        // given
        final HttpHeaders httpHeaders = new HttpHeaders();
        final String token = UUID.randomUUID().toString();
        httpHeaders.add(SecurityUtils.AUTHORISATION, token);
        Authentication authentication = null;

        given(feeDtoMapper.toFee(any(FixedFeeDto.class), anyString())).willReturn(aFixedFee());
        given(feeService.save(any(Fee.class))).willReturn(aFixedFee());

        // when & then
        Exception result = null;

        this.mockMvc.perform(
            put("/fees-register/fixed-fees/testCode")
                .contentType(APPLICATION_JSON)
                .headers(httpHeaders)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isUnauthorized());

    }

    private Authentication testAuthenticationTokenWithNullPrincipal(final String... authorities) {
        final TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(null, "anonymous", authorities);
        return testingAuthenticationToken;
    }

    private Authentication testAuthenticationTokenWithAuthority(final String... authorities) {
        final TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken("principal", "anonymous", authorities);
        return testingAuthenticationToken;
    }


}