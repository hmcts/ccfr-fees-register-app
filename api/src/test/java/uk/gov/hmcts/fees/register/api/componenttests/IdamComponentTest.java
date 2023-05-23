package uk.gov.hmcts.fees.register.api.componenttests;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.IdamUserIdResponse;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction1Repository;
import uk.gov.hmcts.fees2.register.data.service.IdamService;
import uk.gov.hmcts.fees2.register.data.service.impl.IdamServiceImpl;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(MockitoJUnitRunner.class)
public class IdamComponentTest {

    @Value("${auth.idam.client.baseUrl}")
    static final String IDAM_BASE_URL = "/api/v1/users";

    @Mock
    private RestTemplate restTemplateIdam;

    @InjectMocks private IdamService idamService = new IdamServiceImpl();

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

}
