package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.fees2.register.api.contract.ReasonDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.util.IdamUtil;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class FeeVersionControllerMvcTest {

    @InjectMocks
    private FeeVersionController controller;

    private MockMvc mvc;

    @Mock
    private FeeVersionService feeVersionService;

    @Mock
    private IdamUtil idamUtil;

    @Before
    public void setup() {
        mvc = standaloneSetup(controller).build();
    }

    @Test
    public void shouldSubmitToReview() throws Exception {
        // given
        final String code = "aCode";
        final int version = 2;

        // when
        this.mvc.perform(
                patch("/fees/aCode/versions/2/submit-for-review"))
                .andExpect(status().isNoContent());

        // then
        verify(feeVersionService).changeStatus(code, version, FeeVersionStatus.pending_approval, null);
    }

    @Test
    public void shouldApprove() throws Exception {
        // given
        final String code = "aCode";
        final int version = 2;

        // when
        this.mvc.perform(
                patch("/fees/aCode/versions/2/approve"))
                .andExpect(status().isNoContent());

        // then
        verify(feeVersionService).changeStatus(code, version, FeeVersionStatus.approved, null);
    }

    @Test
    public void shouldReject() throws Exception {
        // given
        final String code = "aCode";
        final int version = 2;

        // when
        this.mvc.perform(
                patch("/fees/aCode/versions/2/reject"))
                .andExpect(status().isNoContent());

        // then
        verify(feeVersionService).changeStatus(code, version, FeeVersionStatus.draft, null, null);
    }

    @Test
    public void shouldRejectWithReason() throws Exception {
        // given
        final String code = "aCode";
        final int version = 2;
        final ReasonDto reasonDto = new ReasonDto();
        reasonDto.setReasonForReject("wrong value");

        // when
        this.mvc.perform(
                patch("/fees/aCode/versions/2/reject")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"reasonForReject\": \"wrong value\"}"))
                .andExpect(status().isNoContent());

        // then
        verify(feeVersionService)
                .changeStatus(code, version, FeeVersionStatus.draft, null, reasonDto.getReasonForReject());
    }

    @Test
    public void shouldRejectWithEmptyReason() throws Exception {
        // given
        final String code = "aCode";
        final int version = 2;
        final ReasonDto reasonDto = new ReasonDto();
        reasonDto.setReasonForReject("");

        // when
        this.mvc.perform(
                patch("/fees/aCode/versions/2/reject")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"reasonForReject\": \"\"}"))
                .andExpect(status().isNoContent());

        // then
        verify(feeVersionService)
                .changeStatus(code, version, FeeVersionStatus.draft, null, reasonDto.getReasonForReject());
    }
}
