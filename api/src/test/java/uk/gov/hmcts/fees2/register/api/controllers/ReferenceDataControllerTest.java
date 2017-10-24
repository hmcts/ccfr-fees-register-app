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

    @Test
    public void testGetChannelTypeByName() throws Exception {
        when(channelTypeService.findByNameOrThrow("online")).thenReturn(getChannelTypes().get(0));

        this.mockMvc.perform(get("/channel-type/online"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("online")));

        verify(channelTypeService, times(1)).findByNameOrThrow("online");
        verifyNoMoreInteractions(channelTypeService);
    }

    @Test(expected = NestedServletException.class)
    public void testGetChannelTypeNameNotFound() throws Exception {
        when(channelTypeService.findByNameOrThrow("demo1")).thenThrow(new ReferenceDataNotFoundException("Channeltype", "demo"));

        this.mockMvc.perform(get("/channel-type/demo"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Reference data for ChannelType=demofdf was not found")));

        verify(channelTypeService, times(1)).findByNameOrThrow("test");
        verifyNoMoreInteractions(channelTypeService);
    }

    @Test
    public void testGetDirectionTypes() throws Exception {
        when(directionTypeService.findAll()).thenReturn(getDirectionTypes());

        this.mockMvc.perform(get("/direction-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(7)))
            .andExpect(jsonPath("$[0].name", is("cost recovery")))
            .andExpect(jsonPath("$[1].name", is("enhanced")))
            .andExpect(jsonPath("$[2].name", is("license")))
            .andExpect(jsonPath("$[3].name", is("partial cost recovery")))
            .andExpect(jsonPath("$[4].name", is("pre cost recovery")))
            .andExpect(jsonPath("$[5].name", is("reduced chum")))
            .andExpect(jsonPath("$[6].name", is("simplified")));

        verify(directionTypeService, times(1)).findAll();
        verifyNoMoreInteractions(directionTypeService);
    }

    @Test
    public void testGetDirectionTypeByName() throws Exception {
        when(directionTypeService.findByNameOrThrow("cost recovery")).thenReturn(getDirectionTypes().get(0));

        this.mockMvc.perform(get("/direction-type/cost recovery"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("cost recovery")));

        verify(directionTypeService, times(1)).findByNameOrThrow("cost recovery");
        verifyNoMoreInteractions(directionTypeService);
    }

    @Test
    public void testGetFeeTypes() throws Exception {
        when(feeTypeService.findAll()).thenReturn(getFeeTyes());

        this.mockMvc.perform(get("/fee-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("fixed fee")))
            .andExpect(jsonPath("$[1].name", is("range fee")));

        verify(feeTypeService, times(1)).findAll();
        verifyNoMoreInteractions(feeTypeService);
    }

    @Test
    public void testGetFeeTypeByName() throws Exception {
        when(feeTypeService.findByNameOrThrow("fixed fee")).thenReturn(getFeeTyes().get(0));

        this.mockMvc.perform(get("/fee-type/fixed fee"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("fixed fee")));

        verify(feeTypeService, times(1)).findByNameOrThrow("fixed fee");
        verifyNoMoreInteractions(feeTypeService);
    }

    @Test
    public void testGetServiceTypes() throws Exception {
        when(serviceTypeService.findAll()).thenReturn(getServiceTypes());

        this.mockMvc.perform(get("/service-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(18)))
            .andExpect(jsonPath("$[0].name", is("civil money claims")))
            .andExpect(jsonPath("$[1].name", is("possession claims")))
            .andExpect(jsonPath("$[2].name", is("insolvency")))
            .andExpect(jsonPath("$[3].name", is("private law")))
            .andExpect(jsonPath("$[4].name", is("public law")))
            .andExpect(jsonPath("$[5].name", is("divorce")))
            .andExpect(jsonPath("$[6].name", is("adoption")))
            .andExpect(jsonPath("$[7].name", is("employment appeals tribunal")))
            .andExpect(jsonPath("$[8].name", is("employment tribunal")))
            .andExpect(jsonPath("$[9].name", is("gambling tribunal")))
            .andExpect(jsonPath("$[10].name", is("gender recognition tribunal")))
            .andExpect(jsonPath("$[11].name", is("gender recognition tribunal")))
            .andExpect(jsonPath("$[12].name", is("immigration and asylum chamber tribunal")))
            .andExpect(jsonPath("$[13].name", is("property chamber")))
            .andExpect(jsonPath("$[14].name", is("tax chamber")))
            .andExpect(jsonPath("$[15].name", is("probate")))
            .andExpect(jsonPath("$[16].name", is("general")))
            .andExpect(jsonPath("$[17].name", is("magistrates")));

        verify(serviceTypeService, times(1)).findAll();
        verifyNoMoreInteractions(serviceTypeService);
    }

    @Test
    public void testGetServiceTypeByName() throws Exception {
        when(serviceTypeService.findByNameOrThrow("divorce")).thenReturn(getServiceTypes().get(5));

        this.mockMvc.perform(get("/service-type/divorce"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("divorce")));

        verify(serviceTypeService, times(1)).findByNameOrThrow("divorce");
        verifyNoMoreInteractions(serviceTypeService);
    }

    @Test
    public void testGetEventTypes() throws Exception {
        when(eventTypeService.findAll()).thenReturn(getEventTypes());

        this.mockMvc.perform(get("/event-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(10)))
            .andExpect(jsonPath("$[0].name", is("enforcement")))
            .andExpect(jsonPath("$[1].name", is("judicial review")))
            .andExpect(jsonPath("$[2].name", is("appeal")))
            .andExpect(jsonPath("$[3].name", is("search")))
            .andExpect(jsonPath("$[4].name", is("issue")))
            .andExpect(jsonPath("$[5].name", is("general application")))
            .andExpect(jsonPath("$[6].name", is("copies")))
            .andExpect(jsonPath("$[7].name", is("hearing")))
            .andExpect(jsonPath("$[8].name", is("miscellaneous")))
            .andExpect(jsonPath("$[9].name", is("cost assessment")));

        verify(eventTypeService, times(1)).findAll();
        verifyNoMoreInteractions(eventTypeService);
    }

    @Test
    public void testGetEventTypeByName() throws Exception {
        when(eventTypeService.findByNameOrThrow("search")).thenReturn(getEventTypes().get(3));

        this.mockMvc.perform(get("/event-type/search"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("search")));

        verify(eventTypeService, times(1)).findByNameOrThrow("search");
        verifyNoMoreInteractions(eventTypeService);
    }

    @Test
    public void testGetJurisdictions1() throws Exception {
        when(jurisdiction1Service.findAll()).thenReturn(getJurisdictions1());

        this.mockMvc.perform(get("/jurisdictions1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("civil")))
            .andExpect(jsonPath("$[1].name", is("family")))
            .andExpect(jsonPath("$[2].name", is("tribunal")));

        verify(jurisdiction1Service, times(1)).findAll();
        verifyNoMoreInteractions(jurisdiction1Service);
    }

    @Test
    public void testGetJurisdictions1ByName() throws Exception {
        when(jurisdiction1Service.findByNameOrThrow("civil")).thenReturn(getJurisdictions1().get(0));

        this.mockMvc.perform(get("/jurisdiction1/civil"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("civil")));

        verify(jurisdiction1Service, times(1)).findByNameOrThrow("civil");
        verifyNoMoreInteractions(jurisdiction1Service);
    }

    @Test
    public void testGetJurisdiction2() throws Exception {
        when(jurisdiction2Service.findAll()).thenReturn(getJurisdiction2());

        this.mockMvc.perform(get("/jurisdictions2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].name", is("county court")))
            .andExpect(jsonPath("$[1].name", is("high court")))
            .andExpect(jsonPath("$[2].name", is("magistrates court")))
            .andExpect(jsonPath("$[3].name", is("court of protection")));

        verify(jurisdiction2Service, times(1)).findAll();
        verifyNoMoreInteractions(jurisdiction2Service);
    }

    @Test
    public void testGetJurisdictions2ByName() throws Exception {
        when(jurisdiction2Service.findByNameOrThrow("magistrates court")).thenReturn(getJurisdiction2().get(2));

        this.mockMvc.perform(get("/jurisdiction2/magistrates court"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.name", is("magistrates court")));

        verify(jurisdiction2Service, times(1)).findByNameOrThrow("magistrates court");
        verifyNoMoreInteractions(jurisdiction2Service);
    }
}
