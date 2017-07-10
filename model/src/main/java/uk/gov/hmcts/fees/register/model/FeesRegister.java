package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder(builderMethodName = "feeRegisterWith")
public class FeesRegister {

    private List<Category> categories;
    private List<Fee> flatFees;

    public Optional<Category> getClaimCategory(String claimCategoryId) {
        return this.categories.stream()
                .filter(x -> claimCategoryId.equals(x.getId())).findFirst();
    }

    public Optional<Fee> getFeeDetails(String id) {
        return flatFees.stream().filter(x -> id.equals(x.getId())).findFirst();
    }
}
