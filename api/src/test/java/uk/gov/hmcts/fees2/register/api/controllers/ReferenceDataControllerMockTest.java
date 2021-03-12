package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.ReferenceDataDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.RangeUnitRepository;
import uk.gov.hmcts.fees2.register.data.service.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees2.register.api.contract.ApplicantTypeDto.applicantTypeDtoWith;
import static uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest.*;


public class ReferenceDataControllerMockTest {

    @InjectMocks
    private ReferenceDataController referenceDataController;

    @Mock
    ApplicantTypeService applicantTypeService;

    @Mock
    ChannelTypeService channelTypeService;

    @Mock
    DirectionTypeService directionTypeService;

    @Mock
    ServiceTypeService serviceTypeService;

    @Mock
    EventTypeService eventTypeService;

    @Mock
    Jurisdiction1Service jurisdiction1Service;

    @Mock
    Jurisdiction2Service jurisdiction2Service;

    @Mock
    ReferenceDataDtoMapper referenceDataDtoMapper;

    @Mock
    RangeUnitRepository rangeUnitRepository;

    private List<ChannelType> channelTypeList;

    private List<DirectionType> directionTypeList;

    private List<ServiceType> serviceTypeList;

    private List<EventType> eventTypeList;

    private List<Jurisdiction1> jurisdiction1List;

    private List<Jurisdiction2> jurisdiction2List;

    private List<ApplicantType> applicantTypeList;

    private List<RangeUnit> rangeUnitList;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(referenceDataController).build();
        channelTypeList = getChannelTypes();
        directionTypeList = getDirectionTypes();
        serviceTypeList = getServiceTypes();
        eventTypeList = getEventTypes();
        jurisdiction1List = getJurisdictions1();
        jurisdiction2List = getJurisdictions2();

        applicantTypeList = new ArrayList<>();
        applicantTypeList.add(ApplicantType.applicantWith().name("personal").build());
        applicantTypeList.add(ApplicantType.applicantWith().name("all").build());
        applicantTypeList.add(ApplicantType.applicantWith().name("professional").build());

        rangeUnitList = new ArrayList<>();
        rangeUnitList.add(RangeUnit.UnitWith().name("RangeUnit1").build());
        rangeUnitList.add(RangeUnit.UnitWith().name("RangeUnit2").build());

        when(rangeUnitRepository.findAll()).thenReturn(rangeUnitList);
        when(applicantTypeService.findAll()).thenReturn(applicantTypeList);
        when(channelTypeService.findAll()).thenReturn(channelTypeList);
        when(directionTypeService.findAll()).thenReturn(directionTypeList);
        when(serviceTypeService.findAll()).thenReturn(serviceTypeList);
        when(eventTypeService.findAll()).thenReturn(eventTypeList);
        when(jurisdiction1Service.findAll()).thenReturn(jurisdiction1List);
        when(jurisdiction2Service.findAll()).thenReturn(jurisdiction2List);
    }

    @Test
    public void testReferenceDataList() throws Exception {
        this.mockMvc.perform(get("/referenceData")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }


    @Test
    public void testGetRangeTypeList() throws Exception {
        this.mockMvc.perform(get("/range-units")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("RangeUnit1"))
            .andExpect(jsonPath("$[1].name").value("RangeUnit2"));
    }

    @Test
    public void getApplicationTypeList() throws Exception {
        when(referenceDataDtoMapper.toApplicantTypeDto(applicantTypeList.get(0))).thenReturn(applicantTypeDtoWith().name("personal").build());
        when(referenceDataDtoMapper.toApplicantTypeDto(applicantTypeList.get(1))).thenReturn(applicantTypeDtoWith().name("all").build());
        when(referenceDataDtoMapper.toApplicantTypeDto(applicantTypeList.get(2))).thenReturn(applicantTypeDtoWith().name("professional").build());

        this.mockMvc.perform(get("/applicant-types")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("personal"))
            .andExpect(jsonPath("$[1].name").value("all"))
            .andExpect(jsonPath("$[2].name").value("professional"));

    }

    @Test
    public void getChannelTypeList() throws Exception {
        when(referenceDataDtoMapper.toChannelTypeDto(getChannelTypes().get(0))).thenReturn(ChannelTypeDto.channelTypeDtoWith().name("online").build());
        when(referenceDataDtoMapper.toChannelTypeDto(getChannelTypes().get(1))).thenReturn(ChannelTypeDto.channelTypeDtoWith().name("bulk").build());


        this.mockMvc.perform(get("/channel-types")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("online"))
            .andExpect(jsonPath("$[1].name").value("bulk"));
    }

    @Test
    public void getDirectionTypeList() throws Exception {
        when(referenceDataDtoMapper.toDirectionTypeDto(directionTypeList.get(0))).thenReturn(DirectionTypeDto.directionTypeDtoWith().name("cost recovery").build());
        when(referenceDataDtoMapper.toDirectionTypeDto(directionTypeList.get(6))).thenReturn(DirectionTypeDto.directionTypeDtoWith().name("simplified").build());


        this.mockMvc.perform(get("/direction-types")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("cost recovery"))
            .andExpect(jsonPath("$[6].name").value("simplified"));
    }


    @Test
    public void getServiceTypeList() throws Exception {
        when(referenceDataDtoMapper.toServiceTypeDto(serviceTypeList.get(0))).thenReturn(ServiceTypeDto.serviceTypeDtoWith().name("civil money claims").build());
        when(referenceDataDtoMapper.toServiceTypeDto(serviceTypeList.get(17))).thenReturn(ServiceTypeDto.serviceTypeDtoWith().name("magistrates").build());


        this.mockMvc.perform(get("/service-types")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("civil money claims"))
            .andExpect(jsonPath("$[17].name").value("magistrates"));
    }

    @Test
    public void getEventTypeList() throws Exception {
        when(referenceDataDtoMapper.toEventTypeDto(eventTypeList.get(0))).thenReturn(EventTypeDto.eventTypeDtoWith().name("enforcement").build());
        when(referenceDataDtoMapper.toEventTypeDto(eventTypeList.get(9))).thenReturn(EventTypeDto.eventTypeDtoWith().name("cost assessment").build());


        this.mockMvc.perform(get("/event-types")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("enforcement"))
            .andExpect(jsonPath("$[9].name").value("cost assessment"));
    }

    @Test
    public void getJurisdiction1TypeList() throws Exception {
        when(referenceDataDtoMapper.toJuridiction1Dto(jurisdiction1List.get(0))).thenReturn(Jurisdiction1Dto.jurisdiction1TypeDtoWith().name("civil").build());
        when(referenceDataDtoMapper.toJuridiction1Dto(jurisdiction1List.get(2))).thenReturn(Jurisdiction1Dto.jurisdiction1TypeDtoWith().name("tribunal").build());


        this.mockMvc.perform(get("/jurisdictions1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("civil"))
            .andExpect(jsonPath("$[2].name").value("tribunal"));
    }

    @Test
    public void getJurisdiction2TypeList() throws Exception {
        when(referenceDataDtoMapper.toJurisdiction2Dto(jurisdiction2List.get(0))).thenReturn(Jurisdiction2Dto.jurisdiction2TypeDtoWith().name("county court").build());
        when(referenceDataDtoMapper.toJurisdiction2Dto(jurisdiction2List.get(3))).thenReturn(Jurisdiction2Dto.jurisdiction2TypeDtoWith().name("court of protection").build());

        this.mockMvc.perform(get("/jurisdictions2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("county court"))
            .andExpect(jsonPath("$[3].name").value("court of protection"));
    }


}
