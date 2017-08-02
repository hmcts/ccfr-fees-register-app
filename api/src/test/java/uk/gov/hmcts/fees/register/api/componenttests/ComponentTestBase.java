package uk.gov.hmcts.fees.register.api.componenttests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles("embedded")
public class ComponentTestBase {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    RestActions restActions;

    @Before
    public void setUp() {
        MockMvc mvc = webAppContextSetup(webApplicationContext).build();
        this.restActions = new RestActions(mvc, objectMapper);
    }

    CustomResultMatcher body() {
        return new CustomResultMatcher(objectMapper);
    }
}
