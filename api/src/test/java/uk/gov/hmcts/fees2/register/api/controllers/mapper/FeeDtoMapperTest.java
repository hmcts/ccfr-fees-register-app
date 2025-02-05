package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.IdamUser;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.IdamService;

import java.text.ParseException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FeeDtoMapperTest extends BaseIntegrationTest {

    @Mock
    private ChannelTypeRepository channelTypeRepository;

    @Mock
    private ApplicantTypeRepository applicantTypeRepository;

    @Mock
    private Jurisdiction1Repository jurisdiction1Repository;

    @Mock
    private Jurisdiction2Repository jurisdiction2Repository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private IdamService idamService;

    @Mock
    private DirectionTypeRepository directionTypeRepository;

    @InjectMocks
    private FeeDtoMapper feeDtoMapper;

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
    public void testUpdateRangedFee() throws ParseException {
        feeDtoMapper.updateRangedFee(getRangedFeeDtoValues("FEE0001"), getRangedFee("FEE0001"),null);
        verify(channelTypeRepository, times(1)).findByNameOrThrow(anyString());
        verify(applicantTypeRepository, times(1)).findByNameOrThrow(anyString());
        verify(jurisdiction1Repository, times(1)).findByNameOrThrow(anyString());
        verify(jurisdiction2Repository, times(1)).findByNameOrThrow(anyString());
        verify(eventTypeRepository, times(1)).findByNameOrThrow(anyString());
    }

    @Test
    public void testUpdateFixedFee() throws ParseException {
        feeDtoMapper.updateFixedFee(getFixedFeeDto("FEE0001"), getFixedFee("FEE0001"),null);
        verify(channelTypeRepository, times(1)).findByNameOrThrow(anyString());
        verify(applicantTypeRepository, times(1)).findByNameOrThrow(anyString());
        verify(jurisdiction1Repository, times(1)).findByNameOrThrow(anyString());
        verify(jurisdiction2Repository, times(1)).findByNameOrThrow(anyString());
        verify(eventTypeRepository, times(1)).findByNameOrThrow(anyString());
    }

    @Test
    public void testToFeeVersionDto() throws ParseException {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));
        when(idamService.getUserName(any(), anyString())).thenReturn("Forename Surname");

        FeeVersionDto feeVersionDto = feeDtoMapper.toFeeVersionDto(getFeeVersion(), header);

        assertEquals("Forename Surname", feeVersionDto.getApprovedBy());
        assertEquals("Forename Surname", feeVersionDto.getAuthor());
    }

    @Test
    public void testToFeeVersionDtoExecption() throws ParseException {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));
        when(idamService.getUserName(any(), anyString())
        ).thenThrow(new UserNotFoundException("User not found"));

        FeeVersionDto feeVersionDto = feeDtoMapper.toFeeVersionDto(getFeeVersion(), header);

        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), feeVersionDto.getApprovedBy());
        assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), feeVersionDto.getAuthor());
    }

    @Test
    public void shouldReturnFeeVersionDtoWithUserNames() {
        // Mock IDAM service responses
        when(idamService.getUserName(headers, AUTHOR_ID)).thenReturn(AUTHOR_NAME);
        when(idamService.getUserName(headers, APPROVER_ID)).thenReturn(APPROVER_NAME);

        // Call method under test
        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        Assertions.assertEquals(AUTHOR_NAME, result.getAuthor());
        Assertions.assertEquals(APPROVER_NAME, result.getApprovedBy());
    }

    @Test
    public void shouldHandleAuthorUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, AUTHOR_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, APPROVER_ID)).thenReturn(APPROVER_NAME);

        // Call method under test
        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
        Assertions.assertEquals(APPROVER_NAME, result.getApprovedBy());
    }

    @Test
    public void shouldHandleApproverUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, APPROVER_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, AUTHOR_ID)).thenReturn(AUTHOR_NAME);

        // Call method under test
        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        Assertions.assertEquals(AUTHOR_NAME, result.getAuthor());
    }

    @Test
    public void shouldHandleUserNotFoundExceptionGracefully() {
        // Mock IDAM service to throw an exception
        when(idamService.getUserName(headers, APPROVER_ID)).thenThrow(new UserNotFoundException("User not found"));
        when(idamService.getUserName(headers, AUTHOR_ID)).thenThrow(new UserNotFoundException("User not found"));


        // Call method under test
        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, headers);

        // Assertions
        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = new LinkedMultiValueMap<>();

        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenNullAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = null;

        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

    @Test
    public void shouldReturnDtoWhenIncorrectAuthorizationHeaderIsMissing() {
        MultiValueMap<String, String> emptyHeaders = new LinkedMultiValueMap<>();
        emptyHeaders.put("pepeGrillo", Collections.singletonList("Bearer saraSAA"));

        FeeVersionDto result = feeDtoMapper.toFeeVersionDto(feeVersion, emptyHeaders);

        assertNotNull(result);
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getApprovedBy());
        Assertions.assertEquals(IdamUser.USER_NOT_FOUND.getMessage(), result.getAuthor());
    }

}
