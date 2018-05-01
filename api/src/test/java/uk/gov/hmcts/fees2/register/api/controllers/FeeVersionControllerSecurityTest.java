package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.auth.checker.RequestAuthorizer;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees2.register.api.FeeTestFixtures.aFeeVersionPayload;

@RunWith(SpringRunner.class)
@WebMvcTest(FeeVersionController.class)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
public class FeeVersionControllerSecurityTest {

    @MockBean private FeeVersionService feeVersionService;
    @MockBean private FeeDtoMapper feeDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSubmitToReview_shouldReturnOkWhenUserHasFeeEditAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/submit-for-review")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testSubmitToReview_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/submit-for-review")
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testApprove_shouldReturnOkWhenUserHasFeeApproveAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-approver");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/approve")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testApprove_shouldReturnForbiddenWhenUserDoesNotHaveFeeApproveAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/approve")
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testReject_shouldReturnOkWhenUserHasFeeApproveAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-approver");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/reject")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testReject_shouldReturnForbiddenWhenUserDoesNotHaveFeeApproveAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");
        // when & then
        this.mockMvc.perform(
            patch("/fees/aCode/versions/2/reject")
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }


    @Test
    public void testCreateFeeVersion_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        // when & then
        this.mockMvc.perform(
            post("/fees/testCode/versions")
                .contentType(APPLICATION_JSON)
                .content(aFeeVersionPayload())
                .with(authentication(authentication)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateFeeVersion_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-unknown");
        // when & then
        this.mockMvc.perform(
            post("/fees/testCode/versions")
                .contentType(APPLICATION_JSON)
                .content(aFeeVersionPayload())
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteFeeVersion_shouldReturnOkWhenUserHasFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-editor");

        // when & then
        this.mockMvc.perform(
            delete("/fees/testCode/versions/2")
                .with(authentication(authentication)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFeeVersion_shouldReturnForbiddenWhenUserDoesNotHaveFeeEditorAuthority() throws Exception {
        // given
        Authentication authentication = testAuthenticationTokenWithAuthority("freg-approver");
        // when & then
        this.mockMvc.perform(
            delete("/fees/testCode/versions/2")
                .with(authentication(authentication)))
            .andExpect(status().isForbidden());
    }

    private Authentication testAuthenticationTokenWithAuthority(String... authorities) {
        return new TestingAuthenticationToken("principal", "anonymous", authorities);
    }

    @TestConfiguration
    public static class MockConfiguration {
        @Bean
        public RequestAuthorizer requestAuthorizer() {
            return Mockito.mock(RequestAuthorizer.class);
        }
    }

}
