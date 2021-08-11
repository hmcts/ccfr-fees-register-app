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

        IdamUserInfoResponse mockIdamUserIdResponse = IdamUserInfoResponse.idamUserNameResponseWith()
                .familyName("AA")
                .givenName("BB")
                .name("AA BB")
                .sub("CC@gmail.com")
                .roles(Arrays.asList("DD"))
                .uid("986-erfg-kjhg-123")
                .build();

        ResponseEntity<IdamUserInfoResponse> responseEntity = new ResponseEntity<>(mockIdamUserIdResponse, HttpStatus.OK);

        when(restTemplateIdam.exchange(anyString(), any(HttpMethod.class), any(org.springframework.http.HttpEntity.class),
                eq(IdamUserInfoResponse.class)
        )).thenReturn(responseEntity);

        String userName = idamUtil.getUserName();
        assertEquals(userName, mockIdamUserIdResponse.getName());
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
        IdamUserInfoResponse idamUserIdResponse = IdamUserInfoResponse.idamUserNameResponseWith()
                .familyName("VP")
                .givenName("VP")
                .name("VP")
                .sub("V_P@gmail.com")
                .roles(Arrays.asList("vp"))
                .uid("986-erfg-kjhg-123")
                .build();

        assertEquals(idamUserIdResponse.getFamilyName(),"VP");
        assertEquals(idamUserIdResponse.getGivenName(),"VP");
        assertEquals(idamUserIdResponse.getName(),"VP");
        assertEquals(idamUserIdResponse.getRoles(),Arrays.asList("vp"));
        assertEquals(idamUserIdResponse.getUid(),"986-erfg-kjhg-123");
        assertEquals(idamUserIdResponse.getSub(),"V_P@gmail.com");
    }

}