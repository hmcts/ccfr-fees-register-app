package uk.gov.hmcts.fees2.register.api.controllers.provider;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.IdamService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeSearchServiceImpl;
import uk.gov.hmcts.fees2.register.data.service.validator.FeeValidator;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class FeeRangeGroupProviderTestConfiguration {

    @Bean
    public FeeSearchService feeSearchService(){
        return mock(FeeSearchServiceImpl.class);
    }
    @Bean
    public FeeDtoMapper feeDtoMapper(){
        return mock(FeeDtoMapper.class);
    }

    @MockitoBean
    FeeVersionRepository feeVersionRepository;

    @MockitoBean
    ChannelTypeRepository channelTypeRepository;
    @MockitoBean
    Jurisdiction1Repository jurisdiction1Repository;
    @MockitoBean
    Jurisdiction2Repository jurisdiction2Repository;
    @MockitoBean
    EventTypeRepository eventTypeRepository;
    @MockitoBean
    ServiceTypeRepository serviceTypeRepository;
    @MockitoBean
    ApplicantTypeRepository applicantTypeRepository;
    @MockitoBean
    Fee2Repository fee2Repository;
    @MockitoBean
    FeeCodeHistoryRepository feeCodeHistoryRepository;
    @MockitoBean
    FeeValidator feeValidator;
    @MockitoBean
    IdamService idamService;
}
