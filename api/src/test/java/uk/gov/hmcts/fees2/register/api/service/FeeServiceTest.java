package uk.gov.hmcts.fees2.register.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
public class FeeServiceTest {

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private FeeService feeService;

    @Test
    public void testBasicLookup() {

        createDefaultChannelType();
        createSimplestFee();

        List<Fee> res = feeService.lookup(new LookupFeeDto());

        assertTrue(res.size() > 0);

    }

    /* --- */

    private void createSimplestFee() {

        Fee fee = new FixedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setVersion(1);
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        fee = feeService.get(fee.getCode());

        assertEquals(ChannelType.DEFAULT, fee.getChannelType().getName());

    }

    private void createDefaultChannelType() {

        ChannelType def = channelTypeRepository.findOne(ChannelType.DEFAULT);

        if(def == null) {
            def = new ChannelType();
            def.setName(ChannelType.DEFAULT);
            channelTypeRepository.save(def);
        }

    }

}
