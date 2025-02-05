package uk.gov.hmcts.fees2.register.api.controllers.mapper;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.IdamUser;
import uk.gov.hmcts.fees2.register.data.service.IdamService;

import java.util.Collections;


public class FeeVersionMapperTest extends BaseIntegrationTest {

    @Mock
    private IdamService idamService;

    @InjectMocks
    private FeeDtoMapper feeVersionMapper;

    private FeeVersion feeVersion = buildAFeeVersion();
    private MultiValueMap<String, String> headers;

    // Constants
    private static final String AUTHOR_ID = "user123";
    private static final String APPROVER_ID = "admin456";
    private static final String AUTHOR_NAME = "John Doe";
    private static final String APPROVER_NAME = "Jane Admin";
    private static final String AUTH_HEADER = "authorization";
    private static final String AUTH_TOKEN = "Bearer token";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        headers = new LinkedMultiValueMap<>();
        headers.add(AUTH_HEADER, AUTH_TOKEN);
        feeVersion.setApprovedBy(APPROVER_ID);
        feeVersion.setAuthor(AUTHOR_ID);
    }


    @Test
    public void shouldReturnFeeVersionDtoWithUserNames() {
        // Mock IDAM service responses
        when(idamService.getUserName(headers, AUTHOR_ID)).thenReturn(AUTHOR_NAME);
        when(idamService.getUserName(headers, APPROVER_ID)).thenReturn(APPROVER_NAME);

        // Call method under test
        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        assertEquals(AUTHOR_NAME, result.getAuthor());
        assertEquals(APPROVER_NAME, result.getApprovedBy());
    }

    @Test
    public void shouldHandleAuthorUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, AUTHOR_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, APPROVER_ID)).thenReturn(APPROVER_NAME);

        // Call method under test
        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
        assertEquals(APPROVER_NAME, result.getApprovedBy());
    }

    @Test
    public void shouldHandleApproverUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, APPROVER_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, AUTHOR_ID)).thenReturn(AUTHOR_NAME);

        // Call method under test
        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        assertEquals(AUTHOR_NAME, result.getAuthor());
    }

    @Test
    public void shouldHandleUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, APPROVER_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, AUTHOR_ID)).thenThrow(new UserNotFoundException("User not found"));


        // Call method under test
        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = new LinkedMultiValueMap<>();

        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenNullAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = null;

        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenIncorrectAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = new LinkedMultiValueMap<>();
        emptyHeaders.put("pepeGrillo", Collections.singletonList("Bearer saraSAA"));

        FeeVersionDto result = feeVersionMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }
}
