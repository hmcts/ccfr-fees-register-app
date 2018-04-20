package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

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

    @Before
    public void setup() {
        mvc = standaloneSetup(controller).build();
    }

    @Test
    public void shouldSubmitToReview() throws Exception {
        // given
        String code = "aCode";
        int version = 2;

        // when
        this.mvc.perform(
            patch("/fees/aCode/versions/2/submit-for-review"))
            .andExpect(status().isNoContent());

        // then
        verify(feeVersionService).changeStatus(code, version, FeeVersionStatus.pending_approval, null);
    }
}
