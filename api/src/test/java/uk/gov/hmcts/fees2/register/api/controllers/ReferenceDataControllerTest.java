package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import uk.gov.hmcts.fees2.register.api.controllers.refdata.ReferenceDataController;
import uk.gov.hmcts.fees2.register.api.controllers.refdata.ReferenceDataDtoMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.ReferenceDataNotFoundException;
import uk.gov.hmcts.fees2.register.data.service.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tarun on 18/10/2017.
 */

public class ReferenceDataControllerTest extends BaseTest{

    private MockMvc mockMvc;

    @Mock
    private AmountTypeService amountTypeService;

    @Mock
    private ChannelTypeService channelTypeService;

    @Mock
    private DirectionTypeService directionTypeService;

    @Mock
    private EventTypeService eventTypeService;

    @Mock
    private FeeTypeService feeTypeService;

    @Mock
    private Jurisdiction1Service jurisdiction1Service;

    @Mock
    private Jurisdiction2Service jurisdiction2Service;

    @Mock
    private ServiceTypeService serviceTypeService;

    @Spy
    private ReferenceDataDtoMapper referenceDataDtoMapper;

    @InjectMocks
    private ReferenceDataController referenceDataController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(referenceDataController).build();
    }

    @Test
    public void testGetAmountTypes() throws Exception {
        when(amountTypeService.findAll()).thenReturn(getAmountTypes());

        this.mockMvc.perform(get("/amount-types"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("flat")))
            .andExpect(jsonPath("$[1].name", is("percentage")))
            .andExpect(jsonPath("$[2].name", is("rateable")));

        verify(amountTypeService, times(1)).findAll();
        verifyNoMoreInteractions(amountTypeService);
    }

    @Test
    public void testGetAmountTypeByName() throws Exception {
        when(amountTypeService.findByNameOrThrow("flat")).thenReturn(getAmountTypes().get(0));

        this.mockMvc.perform(get("/amount-type/flat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("flat")));

        verify(amountTypeService, times(1)).findByNameOrThrow("flat");
        verifyNoMoreInteractions(amountTypeService);
    }

    @Test(expected = NestedServletException.class)
    public void testGetAmountTypeNameNotFound() throws Exception {
        when(amountTypeService.findByNameOrThrow("demo")).thenThrow(new ReferenceDataNotFoundException("AmountType", "test"));

        this.mockMvc.perform(get("/amount-type/test"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Reference data for AmountType=demo was not found")));

        verify(amountTypeService, times(1)).findByNameOrThrow("test");
        verifyNoMoreInteractions(amountTypeService);
    }

    @Test
    public void testGetChannelTypes() throws Exception {
        when(channelTypeService.findAll()).thenReturn(getChannelTypes());

        this.mockMvc.perform(get("/channel-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("online")))
            .andExpect(jsonPath("$[1].name", is("bulk")));

        verify(channelTypeService, times(1)).findAll();
        verifyNoMoreInteractions(channelTypeService);
    }




}
