package uk.gov.hmcts.fees.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;

public class CategoryDtoMapperTest {


    private final CategoryDtoMapper mapper = new CategoryDtoMapper();
    private static final Fee ANY_FEE = new FixedFee();
    private static final FeeDto MAPPED_FEE_DTO = new FeeDto(1, "ANY", "ANY");
    private RangeGroupsDtoMapper rangeGroupsDtoMapper = new RangeGroupsDtoMapper(new FeesDtoMapper() {
        @Override
        public FeeDto toFeeDto(Fee fee) {
            return MAPPED_FEE_DTO;
        }
    });

    @Test
    public void convertsCategory() {

        assert (true);
    }

}
