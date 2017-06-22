package uk.gov.hmcts.fees.register.api.client;

import java.util.List;


public interface FeesRegister
{
    FeesRegisterDto getFeesRegister();
    List<CategoryDto> getCategories();
    CategoryDto getCategory(String categoryId);
    FeesDto getFeesByCategoryAndAmount(String categoryId, int amount);
    FeesDto getFeesByCategoryAndFeeId(String categoryId, String feeId);
    List<FeesDto> getFlatFeesByCategory(String categoryId);
}
