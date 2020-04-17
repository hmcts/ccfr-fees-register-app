package uk.gov.hmcts.fees.register.api.componenttests.sugar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.SecurityUtilsMock;


import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestActions {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final MockMvc mvc;
    //private final UserResolverBackdoor userRequestAuthorizer;
    private final SecurityUtilsMock securityUtilsMock;
    private final ObjectMapper objectMapper;

    public RestActions(final MockMvc mvc, final SecurityUtilsMock securityUtilsMock, final ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.securityUtilsMock = securityUtilsMock;
        this.objectMapper = objectMapper;
        httpHeaders.add("Accept","application/vnd.uk.gov.hmcts.cc.fr.v2+json");
    }

    public RestActions withUser(String userId) {
        String token = UUID.randomUUID().toString();
        securityUtilsMock.registerToken(token, userId);
        httpHeaders.add(SecurityUtilsMock.AUTHORISATION, token);
        return this;
    }

    public ResultActions get(String urlTemplate, Object... uriVars) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                .get(urlTemplate, uriVars)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .headers(httpHeaders));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResultActions delete(String urlTemplate, Object... uriVars) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                .delete(urlTemplate, uriVars)
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

    public ResultActions post(String urlTemplate, Object dto) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                .post(urlTemplate)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(dto))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ResultActions patch(String urlTemplate, Object dto) {
        try {
            return mvc.perform(MockMvcRequestBuilders
                .patch(urlTemplate)
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
