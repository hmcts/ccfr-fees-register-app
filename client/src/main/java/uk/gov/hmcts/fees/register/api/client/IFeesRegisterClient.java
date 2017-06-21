package uk.gov.hmcts.fees.register.api.client;

import java.util.List;


public interface IFeesRegisterClient {

    String ROOT = "/fees-register";

    FeesRegisterDto getFeesRegister();
    List<CategoryDto> getCategoriesInFeesRegister();
    CategoryDto getCategoryById(String categoryId);
    FeesDto getFeesForClaimAmountForAGivenCategory(String categoryId, int amount);
    FeesDto getFlatFeesForFeeIdInACategory(String categoryId, String feeId);
    List<FeesDto> getAllFlatFeesInACategory(String categoryId);






}
