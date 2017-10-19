package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.Jurisdiction2;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction2Repository;
import uk.gov.hmcts.fees2.register.data.service.Jurisdiction2Service;
import uk.gov.hmcts.fees2.register.data.service.impl.AbstractServiceImpl;

/**
 *
 *
 * @author Tarun Palisetty
 *
 */

@Service
public class Jurisdiction2ServiceImpl extends AbstractServiceImpl<Jurisdiction2> implements Jurisdiction2Service {

    @Autowired
    public Jurisdiction2ServiceImpl(Jurisdiction2Repository jurisdiction2Repository) {
        super(jurisdiction2Repository);
    }
}
