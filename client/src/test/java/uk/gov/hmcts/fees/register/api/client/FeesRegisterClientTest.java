package uk.gov.hmcts.fees.register.api.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

public class FeesRegisterClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());
    private FeesRegisterClient client;
    private String feesAsString;


    @Before
    public void setUp() throws Exception {

        client = new FeesRegisterClient(
            HttpClients.createMinimal(),
            "http://localhost:" + wireMockRule.port()
        );
        FeesDto fees = createFees();
        ObjectMapper mapper = new ObjectMapper();
        feesAsString = mapper.writeValueAsString(fees);

    }

    private FeesDto createFees() throws Exception {
        FeesDto fees = FeesDto.feesDtoWith().id("X0046").type("fixed").amount(109000).description("Civil Court fees - Hearing fees - Multi track claim").build();

        return fees;
    }

    @Test
    public void getFeesByCategoryAndFeeId() throws Exception {

        stubFor(get(urlEqualTo("/fees-register/cmc/categories/hearingfees/flat/X0046"))
            .willReturn(aResponse().
                withStatus(200)
                .withBody(feesAsString)));

        FeesDto fetchedFees = client.getFeesByCategoryAndFeeId("hearingfees", "X0046");
        assertEquals(createFees(), fetchedFees);

    }


}
