package uk.gov.hmcts.register.fees.model;

import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class FeesRegister {

    private String serviceName;
    private List<Category> categories;
    private List<Fee> flatFees;

    public Optional<Category> getClaimCategory(String claimCategoryId) {
        return this.categories.stream()
                .filter(x -> claimCategoryId.equals(x.getId())).findFirst();
    }

    public Fee getFeeDetails(String id) {
        return flatFees.stream().filter(x -> id.equals(x.getId())).findAny().orElse(null);
    }
}
