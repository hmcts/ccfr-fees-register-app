package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.*;
import uk.gov.hmcts.fees2.register.data.service.impl.*;

import java.util.ArrayList;
import java.util.Date;
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
        assertEquals(result.size(), 2);
        assertEquals(result.stream().filter(c -> c.getName().equals("civil")).findAny().get().getName(), "civil");

        verify(channelTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllChannelTypes() throws Exception {
        List<DirectionType> directions = new ArrayList<>();
        directions.add(new DirectionType("cost recovery", null, null));
        directions.add(new DirectionType("enhanced", null, null));
        directions.add(new DirectionType("license", null, null));

        when(directionTypeService.findAll()).thenReturn(directions);

        List<DirectionType> result = directionTypeService.findAll();

        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertNotNull(result.stream().filter(d -> d.getName().equals("license")).findAny().isPresent());

        verify(directionTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllEventTypes() throws Exception {
        List<EventType> events = new ArrayList<>();
        events.add(new EventType("issue", null, null));
        events.add(new EventType("copies", null, null));
        events.add(new EventType("hearing", null, null));
        events.add(new EventType("appeal", null, null));

        when(eventTypeRepository.findAll()).thenReturn(events);

        List<EventType> result = eventTypeService.findAll();

        assertNotNull(result);
        assertEquals(result.size(), 4);
        assertSame(result.contains("copies"), events.contains("copies"));
        assertEquals(result.stream().filter(e -> e.getName().equals("hearing")).findAny().get().getName(), "hearing");
        assertNotNull(result.stream().filter(e -> e.getName().equals("appeal")).findAny().isPresent());

        verify(eventTypeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllJurisdictions1() throws Exception {
        List<Jurisdiction1> jurisdictions1 = new ArrayList<>();
        jurisdictions1.add(new Jurisdiction1("civil", null, null));
        jurisdictions1.add(new Jurisdiction1("family", null, null));
        jurisdictions1.add(new Jurisdiction1("tribunal", null, null));

        when(jurisdiction1Repository.findAll()).thenReturn(jurisdictions1);

        List<Jurisdiction1> result = jurisdiction1Service.findAll();
        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertEquals(result.stream().filter(j -> j.getName().equals("civil")).findAny().get().getName(), "civil");
        assertNotNull(result.stream().filter(j -> j.equals("family")).findAny().isPresent());

        verify(jurisdiction1Repository, times(1)).findAll();
    }

    @Test
    public void testFindAllJurisdictions2() throws Exception {
        List<Jurisdiction2> jurisdictions2 =new ArrayList<>();
        jurisdictions2.add(new Jurisdiction2("county court", null, null));
        jurisdictions2.add(new Jurisdiction2("high court" , null, null));
        jurisdictions2.add(new Jurisdiction2("family court", null, null));

        when(jurisdiction2Repository.findAll()).thenReturn(jurisdictions2);

        List<Jurisdiction2> result = jurisdiction2Service.findAll();
        assertNotNull(result.stream().filter(j -> j.getName().equals("county court")).findAny().isPresent());
        assertEquals(result.stream().filter(j -> j.getName().equals("family court")).findAny().get().getName(), "family court");


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
        assertNotNull(result.stream().filter(s -> s.getName().equals("probate")).findAny().isPresent());
        assertEquals(result.stream().filter(s -> s.getName().equals("divorce")).findAny().get().getName(), "divorce");

        verify(serviceTypeRepository, times(1)).findAll();

     }




}
