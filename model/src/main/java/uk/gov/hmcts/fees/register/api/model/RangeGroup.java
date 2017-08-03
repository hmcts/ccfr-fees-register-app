package uk.gov.hmcts.fees.register.api.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "rangeGroupWith")
public class RangeGroup {
    @Id
    private Integer id;

    @NonNull
    private String code;

    @NonNull
    private String description;

    @OneToMany(mappedBy = "rangeGroupId")
    private List<Range> ranges;
}
