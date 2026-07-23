package uk.gov.hmcts.fees2.register.api.controllers.provider;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeServiceImpl;
import uk.gov.hmcts.fees2.register.data.service.validator.FeeValidator;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class FeeLookupProviderTestConfiguration {

    @Bean
    public FeeService feeService(){
        return mock(FeeServiceImpl.class);
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
}
