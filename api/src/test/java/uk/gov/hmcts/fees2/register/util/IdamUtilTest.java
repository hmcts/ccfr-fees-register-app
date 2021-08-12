package uk.gov.hmcts.fees2.register.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.fees2.register.data.exceptions.InternalServerException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserInfoResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
public class IdamUtilTest {

    @InjectMocks
    private IdamUtil idamUtil;

    @Mock
    @Qualifier("restTemplateIdam")
    private RestTemplate restTemplateIdam;

    @Test
    @WithMockUser("AA BB")
    public void getResponseOnValidToken() throws Exception {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        IdamUserInfoResponse mockIdamUserInfoResponse = new IdamUserInfoResponse();
        mockIdamUserInfoResponse.setGivenName("AA");
        mockIdamUserInfoResponse.setFamilyName("BB");
        mockIdamUserInfoResponse.setName("AA BB");
        mockIdamUserInfoResponse.setUid("986-erfg-kjhg-123");
        mockIdamUserInfoResponse.setRoles(Arrays.asList("DD"));
        mockIdamUserInfoResponse.setSub("CC@gmail.com");

        ResponseEntity<IdamUserInfoResponse> responseEntity = new ResponseEntity<>(mockIdamUserInfoResponse, HttpStatus.OK);

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(org.springframework.http.HttpEntity.class),
                eq(IdamUserInfoResponse.class)
        )).thenReturn(responseEntity);

        String userName = idamUtil.getUserName();
        assertEquals(userName, mockIdamUserInfoResponse.getName());
    }

    @Test(expected = UserNotFoundException.class)
    @WithMockUser
    public void getExceptionOnInvalidToken() throws Exception {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(org.springframework.http.HttpEntity.class),
                eq(IdamUserInfoResponse.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found"));

        idamUtil.getUserName();
    }

    @Test(expected = UserNotFoundException.class)
    @WithMockUser
    public void getExceptionOnTokenReturnsNullResponse() throws Exception {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        ResponseEntity<IdamUserInfoResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(org.springframework.http.HttpEntity.class),
                eq(IdamUserInfoResponse.class)
        )).thenReturn(responseEntity);

        idamUtil.getUserName();
    }

    @Test(expected = InternalServerException.class)
    @WithMockUser
    public void getExceptionOnValidToken() throws Exception {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(IdamUserInfoResponse.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"));

        idamUtil.getUserName();
    }

    @Test
    public void validateResponseDto() throws Exception{

        IdamUserInfoResponse idamUserIdResponse = new IdamUserInfoResponse();

        idamUserIdResponse.setGivenName("AA");
        idamUserIdResponse.setFamilyName("BB");
        idamUserIdResponse.setName("AA BB");
        idamUserIdResponse.setUid("986-erfg-kjhg-123");
        idamUserIdResponse.setRoles(Arrays.asList("DD"));
        idamUserIdResponse.setSub("CC@gmail.com");

        assertEquals(idamUserIdResponse.getFamilyName(),"BB");
        assertEquals(idamUserIdResponse.getGivenName(),"AA");
        assertEquals(idamUserIdResponse.getName(),"AA BB");
        assertEquals(idamUserIdResponse.getRoles(),Arrays.asList("DD"));
        assertEquals(idamUserIdResponse.getUid(),"986-erfg-kjhg-123");
        assertEquals(idamUserIdResponse.getSub(),"CC@gmail.com");
    }

}