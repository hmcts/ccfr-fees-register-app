package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;

import java.util.List;

/**
 * Created by tarun on 20/11/2017.
 */

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder(builderMethodName = "feeLoaderJsonMapperWith")
public class FeeLoaderJsonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(FeeLoaderJsonMapper.class);

    private List<LoaderRangedFeeDto> rangedFees;
    private List<LoaderFixedFeeDto> fixedFees;


}
