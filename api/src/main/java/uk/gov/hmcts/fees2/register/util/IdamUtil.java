package uk.gov.hmcts.fees2.register.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.fees2.register.data.model.UserDetails;

@Component
public class IdamUtil {

    @Value("${auth.idam.client.baseUrl}")
    private String idamApiUrl;

    public String getUserName() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(idamApiUrl + "/o/userinfo");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDetails> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                uk.gov.hmcts.fees2.register.data.model.UserDetails.class);
        return response.getBody().getName();

    }
}
