package uk.gov.hmcts.fees2.register.data.service.validator;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.*;

import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.FeeVersionDateRangeValidator;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.GenericFeeValidator;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeVersionValidator;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.RangedFeeValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class FeeValidatorTest {

    ApplicationContext context = mock(ApplicationContext.class);

    {
        when(context.getBean(RangedFeeValidator.class)).thenReturn(new RangedFeeValidator());
        when(context.getBean(GenericFeeValidator.class)).thenReturn(new GenericFeeValidator());
    }

    List<IFeeVersionValidator> versionValidators = new ArrayList<>();

    {
        versionValidators.add(new FeeVersionDateRangeValidator());
    }

    @Mock
    private Fee2Repository feeRepository;

    private FeeValidator validator = new FeeValidator(context, null, versionValidators, feeRepository);

    @Test(expected = BadRequestException.class)
    public void testWrongDateInFeeVersionRangeIsRejected(){

        long millis = System.currentTimeMillis();

        Fee fee = new FixedFee();
        fee.setUnspecifiedClaimAmount(false);
        fee.setChannelType(new ChannelType("default", new Date(), new Date()));
        FeeVersion v = new FeeVersion();
        v.setValidFrom(new Date(millis + 1000));
        v.setValidTo(new Date(millis));

        fee.setFeeVersions(Arrays.asList(v));

        validator.validateAndDefaultNewFee(fee);

    }

    @Test
    public void testKeywordWithSpecialCharactersIsValid(){

        long millis = System.currentTimeMillis();

        Fee fee = new FixedFee();
        fee.setUnspecifiedClaimAmount(false);
        fee.setChannelType(new ChannelType("default", new Date(), new Date()));

        FeeVersion v = new FeeVersion();
        v.setValidFrom(new Date(millis + 1000));
        v.setValidTo(new Date(millis + 5000));

        fee.setFeeVersions(Arrays.asList(v));

        fee.setKeyword("xxx-xxx_xxx");

        validator.validateAndDefaultNewFee(fee);

    }


    @Test(expected = BadRequestException.class)
    public void testKeywordWithSpacesIsRejected(){

        long millis = System.currentTimeMillis();

        Fee fee = new FixedFee();
        fee.setUnspecifiedClaimAmount(false);
        fee.setChannelType(new ChannelType("default", new Date(), new Date()));

        FeeVersion v = new FeeVersion();
        v.setValidFrom(new Date(millis + 1000));
        v.setValidTo(new Date(millis));

        fee.setFeeVersions(Arrays.asList(v));

        fee.setKeyword("xxx xxx");

        validator.validateAndDefaultNewFee(fee);

    }

    @Test(expected = BadRequestException.class)
    public void testRangedFeeCannotHaveUnspecifiedAmount(){

        RangedFee fee = new RangedFee();
        fee.setUnspecifiedClaimAmount(true);

        validator.validateAndDefaultNewFee(fee);
    }


    @Test(expected = BadRequestException.class)
    public void testRangedFeeRangesMustBeValid(){

        RangedFee fee = new RangedFee();

        fee.setMinRange(BigDecimal.TEN);

        fee.setMaxRange(BigDecimal.ZERO);

        validator.validateAndDefaultNewFee(fee);
    }


}
