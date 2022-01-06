package uk.gov.hmcts.fees2.register.api.controllers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees2.register.api.controllers.base.MockUtils;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeControllerMockTest {

    private MockMvc mockMvc;

    private MockUtils baseMockTest;

    @InjectMocks
    private FeeController feeController;

    @Mock
    protected FeeService feeService;

    protected FeeRepository feeRepository;

    @Mock
    protected FeeDtoMapper feeDtoMapper;

    @Before
    public void setUp() {
        baseMockTest = new MockUtils();
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(feeController).build();
    }

    @Test
    public void csvImportFeesTest() throws Exception{
        feeService = new FeeServiceImpl();
        FeeService fs = Mockito.spy(feeService);

        List<Fee> fees = new ArrayList<>();
        fees.add(baseMockTest.getFixedFee("X0MOCK1"));

        doNothing().when(fs).save(fees);
        when(feeDtoMapper.toFee(baseMockTest.getFixedFeeDto(), "TEST")).thenReturn(baseMockTest.getFixedFee("XOMOCK1"));

        this.mockMvc.perform(post("/fees-register/bulk-fixed-fees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(baseMockTest.getFeeJson()))
            .andExpect(status().isCreated());
    }

    @Test
    public void testLookupWhenNegativeAmount() throws Exception {

        this.mockMvc.perform(get("/fees/lookup", null, null, null, null, null, null, BigDecimal.valueOf(-100), null)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testLookupWhenCopiesEvent() throws Exception {

        this.mockMvc.perform(get("/fees/lookup", null, null, null, null, "copies", null, BigDecimal.valueOf(-100), null)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
