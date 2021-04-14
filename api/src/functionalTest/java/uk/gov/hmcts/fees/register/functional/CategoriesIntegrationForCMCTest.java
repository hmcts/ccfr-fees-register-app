package uk.gov.hmcts.fees.register.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.api.contract.*;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static uk.gov.hmcts.fees.register.api.contract.CategoryDto.categoryDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.CategoryUpdateDto.categoryUpdateDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto.percentageFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto.rangeGroupUpdateDtoWith;

@RunWith(SpringIntegrationSerenityRunner.class)
public class CategoriesIntegrationForCMCTest extends IntegrationTestBase {

    @Autowired
    private FeesRegisterTestDsl scenario;

    private CategoryUpdateDto.CategoryUpdateDtoBuilder proposeCategory = categoryUpdateDtoWith()
            .description("New Description")
            .rangeGroupCode("cmc-online")
            .feeCodes(asList("X0026", "X0027"));

    private PercentageFeeDto.PercentageFeeDtoBuilder proposePercentage = percentageFeeDtoWith()
            .code("X0434")
            .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
            .percentage(BigDecimal.valueOf(4.5));


    private FixedFeeDto.FixedFeeDtoBuilder proposeFees = fixedFeeDtoWith()
            .code("X0999")
            .description("New Description")
            .amount(101);


    private RangeGroupUpdateDto.RangeGroupUpdateDtoBuilder proposeRangeGroup = rangeGroupUpdateDtoWith()
            .description("New Description")
            .ranges(asList(
                    new RangeGroupUpdateDto.RangeUpdateDto(0, 1000, "X0046"),
                    new RangeGroupUpdateDto.RangeUpdateDto(1001, null, "X0047")

            ));


    @Test
    public void findAllCategories() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getAllCategories()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findCategoryByCode() throws IOException {
        scenario.given()
                .when().getCategoryByCode("cmc-hearing")
                .then().got(CategoryDto.class, (category -> Assertions.assertThat(category).isEqualTo(categoryDtoWith()
                .code("cmc-hearing")
                .description("CMC - Hearing fees")
                .rangeGroup(category.getRangeGroup())
                .fees(category.getFees())
                .build())));
    }

    @Test
    public void getAllFees() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getAllFees()
                .then().got(List.class, (feesList -> Assertions.assertThat(feesList).isNotEmpty()));
    }

    @Test
    public void findFeesByCode() throws IOException {
        scenario.given()
                .when().getFeesByCode("X0026")
                .then().got(FixedFeeDto.class, (fee -> Assertions.assertThat(fee).isEqualTo(fixedFeeDtoWith()
                .code("X0026")
                .description("Civil Court fees - Money Claims Online - Claim Amount - 500.01 upto 1000 GBP")
                .amount(6000)
                .build())));
    }

    @Test
    public void findFeesByPercentage() throws IOException {
        scenario.given()
                .when().getFeesByCaluclations("X0434", 15000)
                .then().got(CalculationDto.class, calculatedFee -> Assertions.assertThat(calculatedFee).hasFieldOrPropertyWithValue("amount",675));
    }

    @Test
    public void findAllRangeGroups() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getAllRangeGroups()
                .then().got(List.class, (feesList -> Assertions.assertThat(feesList).isNotEmpty()));
    }

    @Test
    public void findRangeGroupByCode() throws IOException {
        scenario.given()
                .when().getRangeGroupsByCode("cmc-hearing")
                .then().got(RangeGroupDto.class, (category -> Assertions.assertThat(category).isEqualTo(rangeGroupDtoWith()
                .code("cmc-hearing")
                .description("CMC - Hearing")
                .ranges(category.getRanges())
                .build())));
    }

    @Test
    public void findPercentageFeesForRangeGroup() throws IOException {
        scenario.given()
                .when().getRangeGroupByCaluclations("cmc-online", 1500000)
                .then().got(CalculationDto.class, calculatedFee -> Assertions.assertThat(calculatedFee).hasFieldOrPropertyWithValue("amount",67500));
    }

    @Test
    public void findCMCPaperCalculationUnspecified() throws IOException {
        scenario.given()
                .when().getCMCPaperCalculationsUnspecified()
                .then().got(CalculationDto.class, calculatedFee -> Assertions.assertThat(calculatedFee).hasFieldOrPropertyWithValue("amount",1000000));
    }

    @Test
    public void findFeesByPercentageForBadRequest() throws IOException {
        scenario.given()
                .when().getFeesByCaluclations("X04341", 1500000001)
                .then().notFound();
    }

    @Test
    public void findPercentageFeesForRangeGroupForNotFound() throws IOException {
        scenario.given()
                .when().getRangeGroupByCaluclations("X0434", 1500001)
                .then().notFound();
    }

    @Test
    public void findCategoryByCodeForBadRequest() throws IOException {
        scenario.given()
                .when().getCategoryByCode("cmc-hearing1")
                .then().notFound();
    }

    @Test
    public void findFeesByCodeForNotFound() throws IOException {
        scenario.given()
                .when().getFeesByCode("X00261")
                .then().notFound();
    }
}

