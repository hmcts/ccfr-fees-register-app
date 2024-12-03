package uk.gov.hmcts.fees2.register.api.controllers.provider;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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

    @MockBean
    FeeVersionRepository feeVersionRepository;

    @MockBean
    ChannelTypeRepository channelTypeRepository;
    @MockBean
    Jurisdiction1Repository jurisdiction1Repository;
    @MockBean
    Jurisdiction2Repository jurisdiction2Repository;
    @MockBean
    EventTypeRepository eventTypeRepository;
    @MockBean
    ServiceTypeRepository serviceTypeRepository;
    @MockBean
    ApplicantTypeRepository applicantTypeRepository;
    @MockBean
    Fee2Repository fee2Repository;
    @MockBean
    FeeCodeHistoryRepository feeCodeHistoryRepository;
    @MockBean
    FeeValidator feeValidator;
}
