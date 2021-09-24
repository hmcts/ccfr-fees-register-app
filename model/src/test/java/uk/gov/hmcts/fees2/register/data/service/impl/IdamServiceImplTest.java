package uk.gov.hmcts.fees2.register.data.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.fees2.register.data.exceptions.GatewayTimeoutException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserIdResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"idam-test"})
public class IdamServiceImplTest {

    @Mock
    private Authentication auth;

    @Mock
    @Qualifier("restTemplateIdam")
    private RestTemplate restTemplateIdam;

    @InjectMocks
    private IdamServiceImpl idamService;

    @Before
    public void initSecurityContext() {
        MockitoAnnotations.initMocks(this);
        when(auth.getCredentials()).thenReturn("mockedPassword");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @After
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getResponseOnValidToken() throws Exception {

        final IdamUserIdResponse mockIdamUserInfoResponse = new IdamUserIdResponse();
        mockIdamUserInfoResponse.setActive(false);
        mockIdamUserInfoResponse.setEmail("CC@gmail.com");
        mockIdamUserInfoResponse.setCreateDate("");
        mockIdamUserInfoResponse.setRoles(Arrays.asList("DD"));
        mockIdamUserInfoResponse.setForename("AA");
        mockIdamUserInfoResponse.setSurname("BB");
        mockIdamUserInfoResponse.setId("123");
        mockIdamUserInfoResponse.setLastModified("");
        mockIdamUserInfoResponse.setStale(false);

        final IdamUserIdResponse[] responses = new IdamUserIdResponse[1];
        responses[0] = mockIdamUserInfoResponse;

        final ResponseEntity<IdamUserIdResponse[]> responseEntity = new ResponseEntity<IdamUserIdResponse[]>(
                responses, HttpStatus.OK);

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserIdResponse[].class)
        )).thenReturn(responseEntity);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        final String userName = idamService.getUserName(header, "AA");

        assertEquals(userName, Arrays.stream(responses).findFirst().get().getForename() + " "
                + Arrays.stream(responses).findFirst().get().getSurname());
    }

    @Test(expected = UserNotFoundException.class)
    public void getExceptionOnInvalidToken() throws Exception {

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserIdResponse[].class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found"));

        idamService.getUserName(header, "AA");
    }

    @Test(expected = UserNotFoundException.class)
    public void getExceptionOnTokenReturnsNullResponse() throws Exception {

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        final IdamUserIdResponse[] response = {};

        final ResponseEntity<IdamUserIdResponse[]> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserIdResponse[].class)
        )).thenReturn(responseEntity);

        idamService.getUserName(header, "AA");
    }

    @Test
    public void getExceptionOnValidToken() throws Exception {

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserIdResponse[].class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"));
        assertThrows(GatewayTimeoutException.class, () -> {
            idamService.getUserName(header, "AA");
        });
    }

    @Test
    public void getExceptionOnEmptyIdamResponseBody() throws Exception {

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        final IdamUserIdResponse[] response = {};
        final ResponseEntity<IdamUserIdResponse[]> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserIdResponse[].class)
        )).thenReturn(responseEntity);
        assertThrows(UserNotFoundException.class, () -> {
            idamService.getUserName(header, "AA");
        });
    }

    @Test
    public void validateResponseDto() throws Exception {

        final IdamUserIdResponse idamUserIdResponse = new IdamUserIdResponse();

        idamUserIdResponse.setActive(false);
        idamUserIdResponse.setEmail("CC@gmail.com");
        idamUserIdResponse.setCreateDate("");
        idamUserIdResponse.setRoles(Arrays.asList("DD"));
        idamUserIdResponse.setForename("AA");
        idamUserIdResponse.setSurname("BB");
        idamUserIdResponse.setId("123");
        idamUserIdResponse.setLastModified("");
        idamUserIdResponse.setStale(false);

        assertFalse(idamUserIdResponse.isActive());
        assertEquals("CC@gmail.com", idamUserIdResponse.getEmail());
        assertEquals("", idamUserIdResponse.getCreateDate());
        assertEquals(Arrays.asList("DD"), idamUserIdResponse.getRoles());
        assertEquals("AA", idamUserIdResponse.getForename());
        assertEquals("BB", idamUserIdResponse.getSurname());
        assertEquals("123", idamUserIdResponse.getId());
        assertEquals("", idamUserIdResponse.getLastModified());
        assertFalse(idamUserIdResponse.isStale());
    }

}