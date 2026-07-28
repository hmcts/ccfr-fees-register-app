package uk.gov.hmcts.fees2.register.api.controllers.provider;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.IdamService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeSearchServiceImpl;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeServiceImpl;
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
    @Bean
    public FeeService feeService(){
        return mock(FeeServiceImpl.class);
    }

    @Bean
    public FeeVersionRepository feeVersionRepository(){
        return mock(FeeVersionRepository.class);
    }

    @Bean
    public ChannelTypeRepository channelTypeRepository(){
        return mock(ChannelTypeRepository.class);
    }

    @Bean
    public Jurisdiction1Repository jurisdiction1Repository(){
        return mock(Jurisdiction1Repository.class);
    }

    @Bean
    public Jurisdiction2Repository jurisdiction2Repository(){
        return mock(Jurisdiction2Repository.class);
    }

    @Bean
    public EventTypeRepository eventTypeRepository(){
        return mock(EventTypeRepository.class);
    }

    @Bean
    public ServiceTypeRepository serviceTypeRepository(){
        return mock(ServiceTypeRepository.class);
    }

    @Bean
    public ApplicantTypeRepository applicantTypeRepository(){
        return mock(ApplicantTypeRepository.class);
    }

    @Bean
    public Fee2Repository fee2Repository(){
        return mock(Fee2Repository.class);
    }

    @Bean
    public FeeCodeHistoryRepository feeCodeHistoryRepository(){
        return mock(FeeCodeHistoryRepository.class);
    }

    @Bean
    public FeeValidator feeValidator(){
        return mock(FeeValidator.class);
    }

    @Bean
    public IdamService idamService(){
        return mock(IdamService.class);
    }
}
