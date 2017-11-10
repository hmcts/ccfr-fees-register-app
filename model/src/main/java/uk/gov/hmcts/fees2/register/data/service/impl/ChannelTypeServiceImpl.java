package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.ChannelTypeService;

/**
 *
 * @author Tarun Palisetty
 *
 */

@Service
public class ChannelTypeServiceImpl extends AbstractServiceImpl<ChannelType> implements ChannelTypeService {

    @Autowired
    public ChannelTypeServiceImpl(ChannelTypeRepository channelTypeRepository) {
        super(channelTypeRepository);
    }
}
