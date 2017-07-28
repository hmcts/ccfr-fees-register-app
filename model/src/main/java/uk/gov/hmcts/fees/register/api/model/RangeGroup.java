package uk.gov.hmcts.fees.register.api.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "rangeGroupWith")
public class RangeGroup {
    @Id
    private Integer id;

    private String description;

    @OneToMany(mappedBy = "rangeGroupId")
    private List<Range> ranges;
}
