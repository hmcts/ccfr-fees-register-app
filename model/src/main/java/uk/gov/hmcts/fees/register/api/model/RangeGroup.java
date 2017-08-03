package uk.gov.hmcts.fees.register.api.model;

import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Immutable;

@Data
@Entity
@NoArgsConstructor
public class RangeGroup {
    @Id
    private Integer id;

    @NonNull
    private String code;

    @NonNull
    private String description;

    @OneToMany(mappedBy = "rangeGroupId")
    @Cascade(CascadeType.ALL)
    @Immutable
    private List<Range> ranges;

    @Builder(builderMethodName = "rangeGroupWith")
    public RangeGroup(Integer id, String code, String description, List<Range> ranges) {
        checkRangeIsContinuous(ranges);

        this.id = id;
        this.code = code;
        this.description = description;
        this.ranges = ranges;
    }

    public void setRanges(List<Range> ranges) {
        checkRangeIsContinuous(ranges);
        this.ranges = ranges;
    }

    private void checkRangeIsContinuous(List<Range> ranges) {
        List<Range> sortedRanges = Ordering.from(Comparator.comparing(Range::getFrom)).sortedCopy(ranges);
        for (int i = 0; i < sortedRanges.size() - 1; i++) {
            if (!sortedRanges.get(i + 1).follows(sortedRanges.get(i))) {
                throw new RangeGroupNotContinuousException();
            }
        }
    }
}
