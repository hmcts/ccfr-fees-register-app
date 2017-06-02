package uk.gov.hmcts.register.fees.model;

import java.util.List;
import lombok.Data;

@Data
public class FeesRegister {

    private String serviceName;
    private List<Category> categories;
    private List<Fee> flatFees;

    public Category getClaimCategory(String claimCategoryId) {
        return this.categories.stream()
                .filter(x -> claimCategoryId.equals(x.getId())).findFirst().orElse(null);
    }

    public Fee getFeeDetails(String id) {
        return flatFees.stream().filter(x -> id.equals(x.getId())).findAny().orElse(null);
    }

    public Fee getFeeDetailsForClaimAmountAndCategory(int amount, String claimCategoryId) {
        Category category = getClaimCategory(claimCategoryId);
        Range range = category.findRange(amount).get();
        return range.getFee();
    }
}
