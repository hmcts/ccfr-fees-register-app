package uk.gov.hmcts.fees.register.functional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.fees.register.functional.idam.IdamService;
import uk.gov.hmcts.fees.register.functional.service.FeeService;

@ContextConfiguration(classes = TestContextConfiguration.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected IdamService idamService;
    @Autowired
    protected UserBootstrap userBootstrap;
    @Autowired
    protected FeeService feeService;
}
