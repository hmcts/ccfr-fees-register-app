package uk.gov.hmcts.fees2.register.data.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.service.IdamService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
public class FeeServiceImplTest {

    @Mock
    private Fee2Repository fee2Repository;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private FeeServiceImpl feeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFeeWhenHeadersNull() {

        when(fee2Repository.findByCodeOrThrow(anyString())).thenReturn(getFixedFee("FEE0001"));

        Fee resultFee = feeService.getFee("FEE0001", null);
        assertEquals("FEE0001", resultFee.getCode());
        assertEquals(1, resultFee.getFeeVersions().size());
        assertEquals("UserId", resultFee.getFeeVersions().get(0).getAuthor());
    }

    @Test
    public void testGetFeeWhenAuthorizationHeader() {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("authorization", Collections.singletonList("Bearer 131313"));

        when(fee2Repository.findByCodeOrThrow(anyString())).thenReturn(getFixedFee("FEE0001"));
        when(idamService.getUserName(any(), anyString())).thenReturn("Forename Surname");

        Fee resultFee = feeService.getFee("FEE0001", header);
        assertEquals("FEE0001", resultFee.getCode());
        assertEquals(1, resultFee.getFeeVersions().size());
        assertEquals("Forename Surname", resultFee.getFeeVersions().get(0).getAuthor());
    }

    @Test
    public void testGetFeeWhenNoAuthorizationHeader() {

        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.put("Content-type", Collections.singletonList("application/json"));

        when(fee2Repository.findByCodeOrThrow(anyString())).thenReturn(getFixedFee("FEE0001"));

        Fee resultFee = feeService.getFee("FEE0001", header);
        assertEquals("FEE0001", resultFee.getCode());
        assertEquals(1, resultFee.getFeeVersions().size());
        assertEquals("UserId", resultFee.getFeeVersions().get(0).getAuthor());
    }

    private Fee getFixedFee(final String code) {
        final Fee fee = new FixedFee();
        fee.setCode(code);
        fee.setChannelType(new ChannelType("default", null, null));
        fee.setEventType(new EventType("issue", null, null));
        fee.setJurisdiction1(new Jurisdiction1("family", null, null));
        fee.setJurisdiction2(new Jurisdiction2("court of protection", null, null));
        fee.setService(new ServiceType("civil money claims", null, null));
        fee.setFeeVersions(getFeeVersions());

        return fee;
    }

    private List<FeeVersion> getFeeVersions() {
        final List<FeeVersion> feeVersions = new ArrayList<>();
        final FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(getFlatAmount());
        feeVersion.setDescription("Testing1");
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setVersion(1);
        feeVersion.setDirectionType(new DirectionType("enhanced", null, null));
        feeVersion.setLastAmendingSi("CMC online fee order name");
        feeVersion.setConsolidatedFeeOrderName("CMC online fee order name");
        feeVersion.setNaturalAccountCode("Natural code 001");
        feeVersion.setAuthor("UserId");
        feeVersions.add(feeVersion);

        return feeVersions;
    }

    private FlatAmount getFlatAmount() {
        return new FlatAmount(new BigDecimal("150.00"));
    }
}
