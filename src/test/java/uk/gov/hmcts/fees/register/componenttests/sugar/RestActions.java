package uk.gov.hmcts.fees.register.componenttests.sugar;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestActions {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final MockMvc mvc;

    public RestActions(MockMvc mvc) {
        this.mvc = mvc;
    }

    public ResultActions get(String urlTemplate) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                    .get(urlTemplate)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .headers(httpHeaders));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
