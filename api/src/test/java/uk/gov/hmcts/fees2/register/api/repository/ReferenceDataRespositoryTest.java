package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;

/**
 * Created by tarun on 15/11/2017.
 */
public class ReferenceDataRespositoryTest extends BaseTest {

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Test
    public void testChannelRespository() throws Exception {
        System.out.println(channelTypeRepository.getEntityName());
    }
}
