package uk.gov.hmcts.fees2.register.api.controllers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.impl.FeeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeControllerMockTest extends BaseMockTest {

    private MockMvc mockMvc;

    @InjectMocks
    private FeeController feeController;

    @Mock
    protected FeeService feeService;

    protected FeeRepository feeRepository;

    @Mock
    protected FeeDtoMapper feeDtoMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(feeController).build();
    }

    @Test
    public void csvImportFeesTest() throws Exception{
        feeService = new FeeServiceImpl();
        FeeService fs = Mockito.spy(feeService);

        List<Fee> fees = new ArrayList<>();
        fees.add(getFixedFee("X0MOCK1"));

        doNothing().when(fs).save(fees);
        when(feeDtoMapper.toFee(getFixedFeeDto())).thenReturn(getFixedFee("XOMOCK1"));

        this.mockMvc.perform(post("/fees-register/bulkfixedfees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getFeeJson()))
            .andExpect(status().isCreated());
    }


}
