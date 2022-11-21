package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.*;
import uk.gov.hmcts.fees2.register.data.service.impl.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tarun on 15/11/2017.
 */

public class ReferenceDataServiceTest extends BaseTest {

    @Mock
    private ChannelTypeRepository channelTypeRepository;

    @Mock
    private DirectionTypeRepository directionTypeRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private Jurisdiction1Repository jurisdiction1Repository;

    @Mock
    private Jurisdiction2Repository jurisdiction2Repository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    private ChannelTypeService channelTypeService;
    private DirectionTypeService directionTypeService;
    private EventTypeService eventTypeService;
    private Jurisdiction1Service jurisdiction1Service;
    private Jurisdiction2Service jurisdiction2Service;
    private ServiceTypeService serviceTypeService;

    @Before
    public void setUp() {
        channelTypeService = new ChannelTypeServiceImpl(channelTypeRepository);
        directionTypeService = new DirectionTypeServiceImpl(directionTypeRepository);
        eventTypeService = new EventTypeServiceImpl(eventTypeRepository);
        jurisdiction1Service = new Jurisdiction1ServiceImpl(jurisdiction1Repository);
        jurisdiction2Service = new Jurisdiction2ServiceImpl(jurisdiction2Repository);
        serviceTypeService = new ServiceTypeServiceImpl(serviceTypeRepository);
    }

    @Test
    public void given() throws Exception {
        List<ChannelType> channels = new ArrayList<>();
        channels.add(new ChannelType("civil", null, null));
        channels.add(new ChannelType("bulk", null, null));
        when(channelTypeRepository.findAll()).thenReturn(channels);

        List<ChannelType> result = channelTypeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("civil", result.stream().filter(c -> c.getName().equals("civil")).findAny().get().getName());

        verify(channelTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllChannelTypes() {
        List<DirectionType> directions = new ArrayList<>();
        directions.add(new DirectionType("cost recovery", null, null));
        directions.add(new DirectionType("enhanced", null, null));
        directions.add(new DirectionType("licence", null, null));

        when(directionTypeService.findAll()).thenReturn(directions);

        List<DirectionType> result = directionTypeService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNotNull(result.stream().filter(d -> d.getName().equals("licence")).findAny());

        verify(directionTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllEventTypes() {
        List<EventType> events = new ArrayList<>();
        events.add(new EventType("issue", null, null));
        events.add(new EventType("copies", null, null));
        events.add(new EventType("hearing", null, null));
        events.add(new EventType("appeal", null, null));

        when(eventTypeRepository.findAll()).thenReturn(events);

        List<EventType> result = eventTypeService.findAll();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertSame(result.contains("copies"), events.contains("copies"));
        assertEquals("hearing", result.stream().filter(e -> e.getName().equals("hearing")).findAny().get().getName());
        assertNotNull(result.stream().filter(e -> e.getName().equals("appeal")).findAny());

        verify(eventTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllJurisdictions1() {
        List<Jurisdiction1> jurisdictions1 = new ArrayList<>();
        jurisdictions1.add(new Jurisdiction1("civil", null, null));
        jurisdictions1.add(new Jurisdiction1("family", null, null));
        jurisdictions1.add(new Jurisdiction1("tribunal", null, null));

        when(jurisdiction1Repository.findAll()).thenReturn(jurisdictions1);

        List<Jurisdiction1> result = jurisdiction1Service.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("civil", result.stream().filter(j -> j.getName().equals("civil")).findAny().get().getName());
        assertNotNull(result.stream().filter(j -> j.toString().equals("family")).findAny());

        verify(jurisdiction1Repository, times(1)).findAll();
    }

    @Test
    public void testFindAllJurisdictions2() {
        List<Jurisdiction2> jurisdictions2 =new ArrayList<>();
        jurisdictions2.add(new Jurisdiction2("county court", null, null));
        jurisdictions2.add(new Jurisdiction2("high court" , null, null));
        jurisdictions2.add(new Jurisdiction2("family court", null, null));

        when(jurisdiction2Repository.findAll()).thenReturn(jurisdictions2);

        List<Jurisdiction2> result = jurisdiction2Service.findAll();
        assertNotNull(result.stream().filter(j -> j.getName().equals("county court")).findAny());
        assertEquals("family court",
                result.stream().filter(j -> j.getName().equals("family court")).findAny().get().getName());


        verify(jurisdiction2Repository, times(1)).findAll();
     }


     @Test
     public void testFindAllServiceTypes() throws Exception {
        List<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(new ServiceType("civil money claims", null, null));
        serviceTypes.add(new ServiceType("divorce", null, null));
        serviceTypes.add(new ServiceType("probate", null, null));

        when(serviceTypeRepository.findAll()).thenReturn(serviceTypes);

        List<ServiceType> result = serviceTypeService.findAll();
        assertNotNull(result);
        assertNotNull(result.stream().filter(s -> s.getName().equals("probate")).findAny());
        assertEquals("divorce", result.stream().filter(s -> s.getName().equals("divorce")).findAny().get().getName());

        verify(serviceTypeRepository, times(1)).findAll();

     }




}
