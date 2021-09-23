package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.IdamService;

import java.text.ParseException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

        assertEquals("EEE", feeVersionDto.getApprovedBy());
        assertEquals("FFF", feeVersionDto.getAuthor());
    }

}
