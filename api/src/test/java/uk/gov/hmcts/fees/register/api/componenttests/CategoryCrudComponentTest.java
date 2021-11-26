package uk.gov.hmcts.fees.register.api.componenttests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.CategoryUpdateDto;

import javax.transaction.Transactional;
import java.util.Collections;

import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static uk.gov.hmcts.fees.register.api.contract.CategoryUpdateDto.categoryUpdateDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class CategoryCrudComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected UserResolverBackdoor userRequestAuthorizer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    RestActions restActions;

    @Before
    public void setUp() {
        MockMvc mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }

    CustomResultMatcher body() {
        return new CustomResultMatcher(objectMapper);
    }

    @Test
    public void retrieveAll() throws Exception {
        restActions
            .get("/categories")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(CategoryDto.class, (categories) -> {
                assertThat(categories).anySatisfy(category -> {
                    assertThat(category.getCode()).isEqualTo("probate-copies");
                    assertThat(category.getDescription()).isEqualTo("Probate - Copies");
                    assertThat(category.getRangeGroup().getCode()).isEqualTo("probate-copies");
                    assertThat(category.getRangeGroup().getRanges()).hasSize(2);
                    assertThat(category.getFees()).hasSize(2);
                    assertThat(category.getFees()).contains(fixedFeeDtoWith()
                        .code("X0251-4")
                        .description("‘Sealed and certified copy’ – if assets are held abroad you may need one of these. " +
                            "Please check with the appropriate organisations before ordering.")
                        .amount(50)
                        .build());
                });
            }));
    }

    @Test
    public void retrieveOne() throws Exception {
        restActions
            .get("/categories/cmc-online")
            .andExpect(status().isOk())
            .andExpect(body().as(CategoryDto.class, (category) -> {
                assertThat(category.getCode()).isEqualTo("cmc-online");
                assertThat(category.getDescription()).isEqualTo("CMC - Online fees");
                assertThat(category.getRangeGroup().getCode()).isEqualTo("cmc-online");
                assertThat(category.getRangeGroup().getRanges()).hasSize(8);
                assertThat(category.getFees()).isEmpty();
            }));
    }

    @Test
    public void retrieveNonExisting() throws Exception {
        restActions
            .get("/categories/-1")
            .andExpect(status().isNotFound());
    }

    @Test
    public void update() throws Exception {
        CategoryUpdateDto.CategoryUpdateDtoBuilder proposeCategory = categoryUpdateDtoWith()
            .description("New Description")
            .rangeGroupCode("probate-copies")
            .feeCodes(asList("X0046", "X0047"));

        restActions
            .withUser("admin")
            .put("/categories/cmc-online", proposeCategory.build())
            .andExpect(status().isOk())
            .andExpect(body().as(CategoryDto.class, categoryDto -> {
                assertThat(categoryDto.getCode()).isEqualTo("cmc-online");
                assertThat(categoryDto.getDescription()).isEqualTo("New Description");
                assertThat(categoryDto.getRangeGroup().getCode()).isEqualTo("probate-copies");
                assertThat(categoryDto.getFees()).hasSize(2);
                assertThat(categoryDto.getFees()).contains(
                    fixedFeeDtoWith().code("X0047").amount(54500).description("Civil Court fees - Hearing fees - Fast track claim").build()
                );
            }));
    }

    @Test
    public void create() throws Exception {
        CategoryUpdateDto.CategoryUpdateDtoBuilder proposeCategory = categoryUpdateDtoWith()
            .description("New Description")
            .rangeGroupCode("probate-copies")
            .feeCodes(asList("X0046", "X0047"));

        restActions
            .withUser("admin")
            .put("/categories/new-category", proposeCategory.build())
            .andExpect(status().isOk())
            .andExpect(body().as(CategoryDto.class, categoryDto -> {
                assertThat(categoryDto.getCode()).isEqualTo("new-category");
                assertThat(categoryDto.getDescription()).isEqualTo("New Description");
                assertThat(categoryDto.getRangeGroup().getCode()).isEqualTo("probate-copies");
                assertThat(categoryDto.getFees()).hasSize(2);
            }));
    }

    @Test
    public void validateCode() throws Exception {
        assertValidationMessage("/categories/" + join("", nCopies(51, "A")), validCategoryDto().build(), "code: length must be between 0 and 50");
    }

    @Test
    public void validateDescription() throws Exception {
        assertValidationMessage("/categories/cmc-online", validCategoryDto().description(null).build(), "description: may not be empty");
        assertValidationMessage("/categories/cmc-online", validCategoryDto().description("").build(), "description: may not be empty");
        assertValidationMessage("/categories/cmc-online", validCategoryDto().description(join("", nCopies(2001, "A"))).build(),
            "description: length must be between 0 and 2000");
    }

    @Test
    public void validateFeeExists() throws Exception {
        CategoryUpdateDto categoryWithNonExistingFee = validCategoryDto().feeCodes(Collections.singletonList("non-existing")).build();
        assertValidationMessage("/categories/cmc-online", categoryWithNonExistingFee, "feeCodes: contains unknown fee code");
    }

    @Test
    public void validateRangeGroupExists() throws Exception {
        CategoryUpdateDto categoryWithNonExistingRangeGroup = validCategoryDto().rangeGroupCode("non-existing").build();
        assertValidationMessage("/categories/cmc-online", categoryWithNonExistingRangeGroup, "rangeGroupCode: contains unknown range group code");
    }

    private CategoryUpdateDto.CategoryUpdateDtoBuilder validCategoryDto() {
        return categoryUpdateDtoWith().description("any").rangeGroupCode(null).feeCodes(Collections.emptyList());
    }

    private void assertValidationMessage(String urlTemplate, CategoryUpdateDto dto, String message) throws Exception {
        restActions
            .withUser("admin")
            .put(urlTemplate, dto)
            .andExpect(status().isBadRequest())
            .andExpect(body().isErrorWithMessage(message));
    }

}
