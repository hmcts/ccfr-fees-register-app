package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRelationalFeeDto;

import java.util.List;

/**
 * Created by tarun on 20/11/2017.
 */

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder(builderMethodName = "feeLoaderJsonMapperWith")
@AllArgsConstructor
@NoArgsConstructor
public class FeeLoaderJsonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(FeeLoaderJsonMapper.class);

    private List<LoaderRangedFeeDto> rangedFees;
    private List<LoaderFixedFeeDto> fixedFees;
    private List<LoaderRelationalFeeDto> relationalFees;

}
