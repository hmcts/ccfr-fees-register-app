package uk.gov.hmcts.fees.register.api.componenttests.sugar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestActions {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    public RestActions(MockMvc mvc, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
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

    public ResultActions put(String urlTemplate, Object dto) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                .put(urlTemplate)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(dto))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
