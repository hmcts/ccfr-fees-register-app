package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.fees2.register.data.exceptions.GatewayTimeoutException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserIdResponse;
import uk.gov.hmcts.fees2.register.data.service.IdamService;
import uk.gov.hmcts.fees2.register.data.service.impl.IdamServiceFrImpl;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdamComponentTest {

    @Value("${auth.idam.client.baseUrl}")
    static final String IDAM_BASE_URL = "/api/v1/users";

    @Mock
    private RestTemplate restTemplateIdam;

    @InjectMocks private IdamService idamService = new IdamServiceFrImpl();

    IdamUserIdResponse[] idamUserIdResponse = new IdamUserIdResponse[1];

    IdamUserIdResponse[] idamUserNotFoundResponse = new IdamUserIdResponse[0];

    @Before
    public void setUp() {
        idamUserIdResponse[0] = IdamUserIdResponse.idamUserIdResponseWith()
            .email("test@test.com")
            .surname("Surname")
            .forename("Forename")
            .build();
    }

    @Test
    public void testGetUserName() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.any(HttpEntity.class),
            ArgumentMatchers.<Class<Object>>any())).thenReturn(new ResponseEntity(idamUserIdResponse, HttpStatus.OK));

        String userName = idamService.getUserName(header, "John");

        assertEquals("Forename Surname", userName);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUserNotFound() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.any(HttpEntity.class),
            ArgumentMatchers.<Class<Object>>any())).thenReturn(new ResponseEntity(idamUserNotFoundResponse, HttpStatus.OK));

        String userName = idamService.getUserName(header, "John");

        fail();
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserHttpClientError() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.any(HttpEntity.class),
            ArgumentMatchers.<Class<Object>>any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        String userName = idamService.getUserName(header, "John");

        fail();
    }

    @Test(expected = GatewayTimeoutException.class)
    public void testGetUserHttpServerError() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.any(HttpEntity.class),
            ArgumentMatchers.<Class<Object>>any())).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        String userName = idamService.getUserName(header, "John");

        fail();
    }

}
