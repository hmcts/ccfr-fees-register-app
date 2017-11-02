package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.FeesRegisterServiceApplication;
import uk.gov.hmcts.fees2.register.api.contract.ChannelTypeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.util.Date;

/**
 * Created by tarun on 02/11/2017.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("embedded")
@SpringBootTest(classes = FeesRegisterServiceApplication.class)
public class Fee2ApiTest extends BaseTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;


    private RangedFeeDto rangedFeeDto;

    @Before
    public void setUp() {

        ChannelType DEFAULT = new ChannelType(ChannelType.DEFAULT, new Date(), new Date());

        channelTypeRepository.save(DEFAULT);

    }

    @Test
    public void createRangedFeeTest() {
        rangedFeeDto = getRangedFeeDto();

        feeService.save(feeDtoMapper.toFee(rangedFeeDto));
    }
}
