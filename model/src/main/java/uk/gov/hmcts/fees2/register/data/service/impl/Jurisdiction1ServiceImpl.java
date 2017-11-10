package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.Jurisdiction1;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction1Repository;
import uk.gov.hmcts.fees2.register.data.service.Jurisdiction1Service;

/**
 *
 *
 * @author Tarun Palisetty
 *
 */

@Service
public class Jurisdiction1ServiceImpl extends AbstractServiceImpl<Jurisdiction1> implements Jurisdiction1Service {

    @Autowired
    public Jurisdiction1ServiceImpl(Jurisdiction1Repository jurisdiction1Repository) {
        super(jurisdiction1Repository);
    }
}
