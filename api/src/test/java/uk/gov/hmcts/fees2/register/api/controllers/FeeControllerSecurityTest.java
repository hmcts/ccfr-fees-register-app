package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
public class FeeControllerSecurityTest {

    @MockBean private FeeSearchService feeSearchService;
    @MockBean private FeeVersionService feeVersionService;
    @MockBean private FeeService feeService;
    @MockBean private FeeDtoMapper feeDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateRangedFee_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        given(feeDtoMapper.toFee(any(RangedFeeDto.class), anyString())).willReturn(aRangedFee());
        given(feeService.save(any(Fee.class))).willReturn(aRangedFee());

        // when & then
        this.mockMvc.perform(
            post("/fees-register/ranged-fees")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateRangedFee_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            post("/fees-register/ranged-fees")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateFixedFee_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        given(feeDtoMapper.toFee(any(FixedFeeDto.class), anyString())).willReturn(aFixedFee());
        given(feeService.save(any(Fee.class))).willReturn(aFixedFee());

        // when & then
        this.mockMvc.perform(
            post("/fees-register/fixed-fees")
                .contentType(APPLICATION_JSON)
                .content(aFixedFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateFixedFee_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            post("/fees-register/fixed-fees")
                .contentType(APPLICATION_JSON)
                .content(aFixedFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateRangedFee_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        given(feeService.getFee(anyString())).willReturn(aRangedFee());

        // when & then
        this.mockMvc.perform(
            put("/fees-register/ranged-fees/testCode")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateRangedFee_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            put("/fees-register/ranged-fees/testCode")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateFixedFee_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        given(feeDtoMapper.toFee(any(FixedFeeDto.class), anyString())).willReturn(aFixedFee());
        given(feeService.save(any(Fee.class))).willReturn(aFixedFee());

        // when & then
        this.mockMvc.perform(
            put("/fees-register/fixed-fees/testCode")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateFixedFee_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            put("/fees-register/fixed-fees/testCode")
                .contentType(APPLICATION_JSON)
                .content(aRangeFeePayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteFee_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");
        given(feeService.safeDelete(anyString())).willReturn(true);

        // when & then
        this.mockMvc.perform(
            delete("/fees-register/fees/testCode")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFee_shouldReturnForbiddenWhenUserDoesNotHaveFeeDeleteAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            delete("/fees-register/fees/testCode")
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteFee_shouldReturnOkWhenUserHasFeeAdminAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor", "freg-admin");

        // when
        this.mockMvc.perform(
            delete("/fees-register/fees/testCode")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
        // then
        verify(feeService).delete("testCode");
    }

    @Test
    public void testCreateBulkFees_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        // when & then
        this.mockMvc.perform(
            post("/fees-register/bulk-fixed-fees")
                .contentType(APPLICATION_JSON)
                .content(bulkFeesPayload())
                .with(authentication(authentication)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateBulkFees_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            post("/fees-register/bulk-fixed-fees")
                .contentType(APPLICATION_JSON)
                .content(bulkFeesPayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    private Authentication testAuthenticationTokenWithAuthority(String... authorities) {
        return new TestingAuthenticationToken("principal", "anonymous", authorities);
    }


}
