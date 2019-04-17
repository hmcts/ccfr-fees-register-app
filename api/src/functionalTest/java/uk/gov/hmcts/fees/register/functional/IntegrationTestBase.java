package uk.gov.hmcts.fees.register.functional;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.fees.register.functional.idam.IdamService;
import uk.gov.hmcts.fees.register.functional.service.FeeService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
@Ignore
public class IntegrationTestBase {

    @Autowired
    protected IdamService idamService;
    @Autowired
    protected UserBootstrap userBootstrap;
    @Autowired
    protected FeeService feeService;
}
