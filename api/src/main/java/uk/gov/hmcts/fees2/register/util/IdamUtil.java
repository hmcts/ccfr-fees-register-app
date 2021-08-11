package uk.gov.hmcts.fees2.register.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.fees2.register.data.exceptions.InternalServerException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserInfoResponse;

@Service
@SuppressWarnings("PMD.PreserveStackTrace")
public class IdamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IdamUtil.class);

    private static final String USERID_ENDPOINT = "/o/userinfo";

    @Value("${auth.idam.client.baseUrl}")
    private String idamBaseURL;

    @Autowired()
    @Qualifier("restTemplateIdam")
    private RestTemplate restTemplateIdam;

    public String getUserName() {

        try {
            ResponseEntity<IdamUserInfoResponse> responseEntity = getResponseEntity();
            if (responseEntity != null) {
                IdamUserInfoResponse idamUserIdResponse = responseEntity.getBody();
                if (idamUserIdResponse != null) {
                    return idamUserIdResponse.getUid();
                }
            }
            LOG.error("Parse error user not found");
            throw new UserNotFoundException("User not found");
        } catch (HttpClientErrorException e) {
            LOG.error("client err ", e);
            throw new UserNotFoundException("User not found");
        } catch (HttpServerErrorException e) {
            LOG.error("server err ", e);
            throw new InternalServerException("Unable to retrieve User information. Please try again later");
        }
    }

    private ResponseEntity<IdamUserInfoResponse> getResponseEntity() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(idamBaseURL + USERID_ENDPOINT);
        LOG.debug("builder.toUriString() : {}", builder.toUriString());
        return restTemplateIdam
                .exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        getHeaders(), IdamUserInfoResponse.class
                );
    }

    private HttpEntity<?> getHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.set(HttpHeaders.AUTHORIZATION, SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(headers);
    }
}
