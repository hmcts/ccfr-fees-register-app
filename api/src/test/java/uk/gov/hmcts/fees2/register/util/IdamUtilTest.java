package uk.gov.hmcts.fees2.register.util;

import org.apache.http.HttpEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.fees2.register.data.model.UserDetails;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdamUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IdamUtil idamUtil;

    @InjectMocks
    private UserDetails userDetails = new UserDetails();

    /*@Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {
        UserDetails details = new UserDetails();
        details.setName("ABC DEF");
        String url = "http://localhost:5000/o/userinfo";

        HttpHeaders headers = new HttpHeaders();
        when(headers.set(any(), any())).thenReturn(null);

        Mockito
                .when(restTemplate.getForEntity(url, UserDetails.class))
          .thenReturn(new ResponseEntity(details, HttpStatus.OK));

        String name = idamUtil.getUserName();
        Assert.assertEquals(details.getName(), name);
    }

    @Test
    public void testGetObjectAList() {
        UserDetails details = new UserDetails();
        details.setName("ABC DEF");
        //define the entity you want the exchange to return
        ResponseEntity<UserDetails> myEntity = new ResponseEntity<UserDetails>(HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Matchers.eq("/o/userinfo"),
                Matchers.eq(HttpMethod.GET),
                Matchers.<HttpEntity<>>any(),
                Matchers.<ParameterizedTypeReference<UserDetails>>any())
        ).thenReturn(myEntity);

        String name = idamUtil.getUserName();
        Assert.assertEquals(details.getName(), name);
    }

    @Test
    public void testGetUserName {

        String url = "http://localhost:5000/o/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + "xxxxxx");
        org.springframework.http.HttpEntity<String>
                httpEntity = new org.springframework.http.HttpEntity<>("parameters", headers);

        restTemplate = new RestTemplate();

        ResponseEntity<UserDetails> res = restTemplate.exchange(url + "/",
                HttpMethod.GET, httpEntity, UserDetails.class);
        UserDetails result = res.getBody();

        assertNotNull(result);
        assertEquals(govPayPayment.getReference(), result.getName());
    }*/

}