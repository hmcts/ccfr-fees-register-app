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
@Table(name = "percentage_amount")
public class PercentageAmount extends Amount{

    private BigDecimal percentage;

}
