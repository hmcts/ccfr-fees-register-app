package uk.gov.hmcts.fees.register.api.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class FeesRegisterClientTest {

    static final int PORT = SocketUtils.findAvailableTcpPort();


    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);
    public FeesRegisterClient client;
    @Autowired
    public RestTemplateBuilder templateBuilder;

    public String feesAsString;

    @Before
    public void init() {
        client = new FeesRegisterClient(templateBuilder, getBaseUrl(PORT));
    }


    @Before
    public void setUp() throws Exception {

        FeesDto fees = createFees();
        ObjectMapper mapper = new ObjectMapper();
        feesAsString = mapper.writeValueAsString(fees);
        WireMock.reset();
        stubFor(get(urlMatching("/cmc/categories/hearingfees/flat/X0046"))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(feesAsString)));
    }

    private String getBaseUrl(int port) {
        return String.format("http://localhost:%d/fees-register", port);
    }

    public FeesDto createFees() throws Exception {
        FeesDto fees = FeesDto.feesDtoWith().id("X0046").type("fixed").amount(109000).description("Civil Court fees - Hearing fees - Multi track claim").build();

        return fees;
    }

    @Test
    public void getFlatFeesForFeeIdInACategory() throws Exception {
        //Torevisit later
        //FeesDto fetchedFees = client.getFlatFeesForFeeIdInACategory("hearingfees","X0046");
        //assertEquals(createFees().getId(), fetchedFees.getId());


    }


}
