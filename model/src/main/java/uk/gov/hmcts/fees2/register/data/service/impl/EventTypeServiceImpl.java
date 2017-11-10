package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.EventType;
import uk.gov.hmcts.fees2.register.data.repository.EventTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.EventTypeService;

/**
 * Created by tarun on 17/10/2017.
 */

@Service
public class EventTypeServiceImpl extends AbstractServiceImpl<EventType> implements EventTypeService {

    @Autowired
    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        super(eventTypeRepository);
    }
}
