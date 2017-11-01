package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rateable_amount")
public class RateableAmount extends Amount {

    private BigDecimal min;

    private BigDecimal max;

    private BigDecimal rateableValue;

}
