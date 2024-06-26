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

import java.util.*;

@Service
public class IdamServiceFrImpl implements IdamService {

    private static final Logger LOG = LoggerFactory.getLogger(IdamServiceFrImpl.class);

    private static final String USERID_ENDPOINT = "/api/v1/users";

    @Value("${auth.idam.client.baseUrl}")
    private String idamBaseURL;

    @Autowired()
    @Qualifier("restTemplateIdam")
    private RestTemplate restTemplateIdam;

    @Override
    public String getUserName(final MultiValueMap<String, String> headers, final String userId) {
        try {

            final ResponseEntity<IdamUserIdResponse[]> responseEntity = getResponseEntity(headers, userId);

            if (null != responseEntity && null != responseEntity.getBody()) {

                final IdamUserIdResponse[] idamUserIdResponse = responseEntity.getBody();

                if (null != idamUserIdResponse && idamUserIdResponse.length >= 1) {

                    IdamUserIdResponse response = (IdamUserIdResponse) Array.get(idamUserIdResponse, 0);

                    if (null != response) {
                        return response.getForename() + " " +
                                response.getSurname();
                    }
                }
            }
            LOG.error("Parse error user not found: {}", responseEntity.getBody());
            throw new UserNotFoundException("User not found");
        } catch (final HttpClientErrorException e) {
            LOG.error("client err ", e);
            throw new UserNotFoundException("User not found");
        } catch (final HttpServerErrorException e) {
            LOG.error("server err ", e);
            throw new GatewayTimeoutException("Unable to retrieve User information. Please try again later");
        }
    }

    private ResponseEntity<IdamUserIdResponse[]> getResponseEntity(final MultiValueMap<String, String> headers, final String userId) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(idamBaseURL + USERID_ENDPOINT)
                .queryParam("query", "id:" + userId);

        return restTemplateIdam
                .exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        getEntity(headers), IdamUserIdResponse[].class
                );
    }

    private HttpEntity<String> getEntity(MultiValueMap<String, String> headers) {
        MultiValueMap<String, String> headerMultiValueMap = new LinkedMultiValueMap<>();
        headerMultiValueMap.put(
                "Content-Type",
                headers.get("content-type") == null ? List.of("application/json") : headers.get("content-type")
        );
        String userAuthorization = headers.get("authorization") == null ? headers.get("Authorization").get(0) : headers.get(
                "authorization").get(0);
        headerMultiValueMap.put(
                "Authorization", Collections.singletonList(userAuthorization.startsWith("Bearer ")
                        ? userAuthorization : "Bearer ".concat(userAuthorization))
        );
        HttpHeaders httpHeaders = new HttpHeaders(headerMultiValueMap);
        return new HttpEntity<>(httpHeaders);
    }
}
