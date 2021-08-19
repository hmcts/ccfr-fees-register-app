package uk.gov.hmcts.fees2.register.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.fees2.register.data.exceptions.GatewayTimeoutException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserIdResponse;
import uk.gov.hmcts.fees2.register.data.service.IdamService;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.Collections;

@Service
public class IdamServiceImpl implements IdamService {

    private static final Logger LOG = LoggerFactory.getLogger(IdamServiceImpl.class);

    private static final String USERID_ENDPOINT = "/api/v1/users";

    @Value("${auth.idam.client.baseUrl}")
    private String idamBaseURL;

    @Autowired()
    @Qualifier("restTemplateIdam")
    private RestTemplate restTemplateIdam;

    @Override
    public String getUserName(final Principal principal, final String userId) {

        try {

            final ResponseEntity<IdamUserIdResponse[]> responseEntity = getResponseEntity(principal, userId);

            if (responseEntity != null && responseEntity.getBody().length > 0) {

                final IdamUserIdResponse[] idamUserIdResponse = responseEntity.getBody();

                if (idamUserIdResponse != null) {

                    IdamUserIdResponse response = (IdamUserIdResponse) Array.get(idamUserIdResponse, 0);

                    return response.getForename() + " " +
                            response.getSurname();
                }
            }
            LOG.error("Parse error user not found");
            throw new UserNotFoundException("User not found");
        } catch (final HttpClientErrorException e) {
            LOG.error("client err ", e);
            throw new UserNotFoundException("User not found");
        } catch (final HttpServerErrorException e) {
            LOG.error("server err ", e);
            throw new GatewayTimeoutException("Unable to retrieve User information. Please try again later");
        }
    }

    private ResponseEntity<IdamUserIdResponse[]> getResponseEntity(final Principal principal, final String userId) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(idamBaseURL + USERID_ENDPOINT)
                .queryParam("query", "id:" + userId);

        LOG.debug("builder.toUriString() : {}", builder.toUriString());

        return restTemplateIdam
                .exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        getEntity(principal), IdamUserIdResponse[].class
                );
    }

    private HttpEntity<String> getEntity(final Principal principal) {

        final MultiValueMap<String, String> headerMultiValueMap = new LinkedMultiValueMap<>();

        final String userAuthorization = ((PreAuthenticatedAuthenticationToken) principal).getCredentials().toString();

        headerMultiValueMap.put("Authorization", Collections.singletonList(userAuthorization.startsWith("Bearer ")
                ? userAuthorization : "Bearer ".concat(userAuthorization)));

        final HttpHeaders httpHeaders = new HttpHeaders(headerMultiValueMap);

        return new HttpEntity<>(httpHeaders);
    }
}
