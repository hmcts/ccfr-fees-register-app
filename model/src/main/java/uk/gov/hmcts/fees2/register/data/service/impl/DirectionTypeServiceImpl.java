package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.repository.DirectionTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.DirectionTypeService;
import uk.gov.hmcts.fees2.register.data.service.impl.AbstractServiceImpl;

/**
 * Created by tarun on 17/10/2017.
 */

@Service
public class DirectionTypeServiceImpl extends AbstractServiceImpl<DirectionType> implements DirectionTypeService {

    @Autowired
    public DirectionTypeServiceImpl(DirectionTypeRepository directionTypeRepository) {
        super(directionTypeRepository);
    }
}
