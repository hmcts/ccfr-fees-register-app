package uk.gov.hmcts.fees.register.api.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class FeesRegisterClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());
    private FeesRegisterClient client;
    private String flatFeesAsString;
    private String onlineFeesAsString;


    @Before
    public void setUp() throws Exception {

        client = new FeesRegisterClient(
            HttpClients.createMinimal(),
            "http://localhost:" + wireMockRule.port()
        );
        ObjectMapper mapper = new ObjectMapper();

        FeesDto flatFees = createFlatFees();
        flatFeesAsString = mapper.writeValueAsString(flatFees);

        FeesDto onlineFees = createOnlineFees();
        onlineFeesAsString = mapper.writeValueAsString(onlineFees);

    }

    private FeesDto createFlatFees() throws Exception {
        FeesDto fees = FeesDto.feesDtoWith().id("X0046").type("fixed").amount(109000).description("Civil Court fees - Hearing fees - Multi track claim").build();
        return fees;
    }

    private FeesDto createOnlineFees() throws Exception {
        FeesDto onlineFees = FeesDto.feesDtoWith().id("X0024").type("fixed").amount(2500).description("Civil Court fees - Money Claims Online - Claim Amount - 0.01 upto 300 GBP").build();
        return onlineFees;
    }


    @Test
    public void getFeesRegister() throws Exception {

        stubFor(get(urlEqualTo("/fees-register"))
            .willReturn(aResponse().
                withStatus(200)
                .withBodyFile("feesRegister.json")));

        FeesRegisterDto fetchedFeesRegister = client.getFeesRegister();
        assertThat(fetchedFeesRegister.getCategories().size() == 2);
    }

    @Test
    public void getCategories() throws Exception {

        stubFor(get(urlEqualTo("/fees-register/categories"))
            .willReturn(aResponse().
                withStatus(200)
                .withBodyFile("categories.json")));

        List<CategoryDto> fetchedCategories = client.getCategories();
        assertThat(fetchedCategories.size() == 2);

    }


    @Test
    public void getCategoryById() throws Exception {
        String categoryId = "onlinefees";
        stubFor(get(urlEqualTo("/fees-register/categories/" + categoryId))
            .willReturn(aResponse().
                withStatus(200)
                .withBodyFile("category.json")));

        CategoryDto fetchedCategory = client.getCategory(categoryId);
        assertThat(fetchedCategory.getId().equals(categoryId));

    }


    @Test
    public void getFeesByCategoryAndAmount() throws Exception {
        String categoryId = "onlinefees";
        int amountInPence = 30000;

        stubFor(get(urlEqualTo("/fees-register/categories/" + categoryId + "/range/" + amountInPence))
            .willReturn(aResponse().
                withStatus(200)
                .withBody(onlineFeesAsString)));

        FeesDto fetchedOnlineFees = client.getFeesByCategoryAndAmount(categoryId, amountInPence);
        assertEquals(createOnlineFees(), fetchedOnlineFees);

    }


    @Test
    public void getFeesByCategoryAndFeeId() throws Exception {

        stubFor(get(urlEqualTo("/fees-register/categories/hearingfees/flat/X0046"))
            .willReturn(aResponse().
                withStatus(200)
                .withBody(flatFeesAsString)));

        FeesDto fetchedFees = client.getFeesByCategoryAndFeeId("hearingfees", "X0046");
        assertEquals(createFlatFees(), fetchedFees);

    }

    @Test
    public void getFlatFeesByCategory() throws Exception {
        String categoryId = "hearingfees";

        stubFor(get(urlEqualTo("/fees-register/categories/" + categoryId + "/flat"))
            .willReturn(aResponse().
                withStatus(200)
                .withBodyFile("flatFees.json")));

        List<FeesDto> fetchedFlatFees = client.getFlatFeesByCategory(categoryId);
        assertThat(fetchedFlatFees.size() == 2);

    }


}
