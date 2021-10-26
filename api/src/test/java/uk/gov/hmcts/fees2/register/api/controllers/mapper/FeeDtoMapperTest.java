package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.repository.*;

import java.text.ParseException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

}
