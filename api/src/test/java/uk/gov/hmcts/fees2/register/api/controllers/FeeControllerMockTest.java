package uk.gov.hmcts.fees2.register.api.controllers;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees2.register.api.controllers.base.MockUtils;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


}
