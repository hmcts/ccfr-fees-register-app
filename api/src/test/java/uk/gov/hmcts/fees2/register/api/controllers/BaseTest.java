package uk.gov.hmcts.fees2.register.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by tarun on 18/10/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
public abstract class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserResolverBackdoor userRequestAuthorizer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private ChannelTypeService channelTypeService;

    @Autowired
    private DirectionTypeRepository directionTypeRepository;

    @Autowired
    private DirectionTypeService directionTypeService;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private Jurisdiction1Repository jurisdiction1Repository;

    @Autowired
    private Jurisdiction1Service jurisdiction1Service;

    @Autowired
    private Jurisdiction2Repository jurisdiction2Repository;

    @Autowired
    private Jurisdiction2Service jurisdiction2Service;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ServiceTypeService serviceTypeService;

    protected RestActions restActions;

    protected MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }

    CustomResultMatcher body() {
        return new CustomResultMatcher(objectMapper);
    }


    /**
     * @return
     */
    public List<ChannelType> getChannelTypes() {
        return new ArrayList<ChannelType>() {{
            add(new ChannelType("online", new Date(), new Date()));
            add(new ChannelType("bulk", new Date(), new Date()));
        }};
    }

    /**
     * @return
     */
    public List<DirectionType> getDirectionTypes() {
        return new ArrayList<DirectionType>() {{
            add(new DirectionType("cost recovery", new Date(), new Date()));
            add(new DirectionType("enhanced", new Date(), new Date()));
            add(new DirectionType("license", new Date(), new Date()));
            add(new DirectionType("partial cost recovery", new Date(), new Date()));
            add(new DirectionType("pre cost recovery", new Date(), new Date()));
            add(new DirectionType("reduced chum", new Date(), new Date()));
            add(new DirectionType("simplified", new Date(), new Date()));
        }};
    }

    /**
     * /**
     *
     * @return
     */
    public List<ServiceType> getServiceTypes() {
        return new ArrayList<ServiceType>() {{
            add(new ServiceType("civil money claims", new Date(), new Date()));
            add(new ServiceType("possession claims", new Date(), new Date()));
            add(new ServiceType("insolvency", new Date(), new Date()));
            add(new ServiceType("private law", new Date(), new Date()));
            add(new ServiceType("public law", new Date(), new Date()));
            add(new ServiceType("divorce", new Date(), new Date()));
            add(new ServiceType("adoption", new Date(), new Date()));
            add(new ServiceType("employment appeals tribunal", new Date(), new Date()));
            add(new ServiceType("employment tribunal", new Date(), new Date()));
            add(new ServiceType("gambling tribunal", new Date(), new Date()));
            add(new ServiceType("gender recognition tribunal", new Date(), new Date()));
            add(new ServiceType("gender recognition tribunal", new Date(), new Date()));
            add(new ServiceType("immigration and asylum chamber tribunal", new Date(), new Date()));
            add(new ServiceType("property chamber", new Date(), new Date()));
            add(new ServiceType("tax chamber", new Date(), new Date()));
            add(new ServiceType("probate", new Date(), new Date()));
            add(new ServiceType("general", new Date(), new Date()));
            add(new ServiceType("magistrates", new Date(), new Date()));
        }};
    }

    public List<EventType> getEventTypes() {
        return new ArrayList<EventType>() {{
            add(new EventType("enforcement", new Date(), new Date()));
            add(new EventType("judicial review", new Date(), new Date()));
            add(new EventType("appeal", new Date(), new Date()));
            add(new EventType("search", new Date(), new Date()));
            add(new EventType("issue", new Date(), new Date()));
            add(new EventType("general application", new Date(), new Date()));
            add(new EventType("copies", new Date(), new Date()));
            add(new EventType("hearing", new Date(), new Date()));
            add(new EventType("miscellaneous", new Date(), new Date()));
            add(new EventType("cost assessment", new Date(), new Date()));
        }};
    }

    public List<Jurisdiction1> getJurisdictions1() {
        return new ArrayList<Jurisdiction1>() {{
            add(new Jurisdiction1("civil", new Date(), new Date()));
            add(new Jurisdiction1("family", new Date(), new Date()));
            add(new Jurisdiction1("tribunal", new Date(), new Date()));
        }};
    }

    public List<Jurisdiction2> getJurisdictions2() {
        return new ArrayList<Jurisdiction2>() {{
            add(new Jurisdiction2("county court", new Date(), new Date()));
            add(new Jurisdiction2("high court", new Date(), new Date()));
            add(new Jurisdiction2("magistrates court", new Date(), new Date()));
            add(new Jurisdiction2("court of protection", new Date(), new Date()));
        }};
    }

    public CreateRangedFeeDto getRangedFeeDto(String feeCode) {


        CreateRangedFeeDto rangedFeeDto = new CreateRangedFeeDto();

        rangedFeeDto.setMinRange(new BigDecimal(1));
        rangedFeeDto.setMaxRange(new BigDecimal(3000));
        rangedFeeDto.setCode(feeCode);
        rangedFeeDto.setVersion(getFeeVersionDto(FeeVersionStatus.approved));
        rangedFeeDto.setJurisdiction1(null);
        rangedFeeDto.setJurisdiction2(null);
        rangedFeeDto.setDirection(null);
        rangedFeeDto.setEvent(null);
        rangedFeeDto.setService(null);
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());
        rangedFeeDto.setMemoLine("Test memo line");
        rangedFeeDto.setFeeOrderName("CMC online fee order name");
        rangedFeeDto.setNaturalAccountCode("Natural code 001");


        return rangedFeeDto;
    }

    public CreateRangedFeeDto getRangedFeeDtoWithReferenceData(int minRange, int maxRange, String feeCode, FeeVersionStatus status) {

        CreateRangedFeeDto rangedFeeDto = new CreateRangedFeeDto();
        rangedFeeDto.setMinRange(new BigDecimal(minRange));
        rangedFeeDto.setMaxRange(new BigDecimal(maxRange));
        rangedFeeDto.setCode(feeCode);
        rangedFeeDto.setVersion(getFeeVersionDto(status));
        rangedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        rangedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        rangedFeeDto.setDirection(directionTypeService.findByNameOrThrow("enhanced").getName());
        rangedFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        rangedFeeDto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());
        rangedFeeDto.setMemoLine("Test memo line");
        rangedFeeDto.setFeeOrderName("CMC online fee order name");
        rangedFeeDto.setNaturalAccountCode("Natural code 001");

        return rangedFeeDto;
    }

    public CreateRangedFeeDto getRangedFeeDtoForLookup(int minRange, int maxRange, String feeCode, FeeVersionStatus status) {

        CreateRangedFeeDto rangedFeeDto = new CreateRangedFeeDto();
        rangedFeeDto.setMinRange(new BigDecimal(minRange));
        rangedFeeDto.setMaxRange(new BigDecimal(maxRange));
        rangedFeeDto.setCode(feeCode);
        rangedFeeDto.setVersion(getFeeVersionDto(status));
        rangedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("family").getName());
        rangedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("high court").getName());
        rangedFeeDto.setDirection(directionTypeService.findByNameOrThrow("license").getName());
        rangedFeeDto.setEvent(eventTypeService.findByNameOrThrow("copies").getName());
        rangedFeeDto.setService(serviceTypeService.findByNameOrThrow("divorce").getName());
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());
        rangedFeeDto.setMemoLine("Test lookup fee");
        rangedFeeDto.setFeeOrderName("Divorce online fee order name");
        rangedFeeDto.setNaturalAccountCode("Natural code for lookup");

        return rangedFeeDto;
    }

    public Fee2Dto getFeeDtoWithReferenceData(int minRange, int maxRange, String feeCode, FeeVersionStatus status) {

        Fee2Dto feeDto = new Fee2Dto();
        feeDto.setMinRange(new BigDecimal(minRange));
        feeDto.setMaxRange(new BigDecimal(maxRange));
        feeDto.setCode(feeCode);
        //feeDto.setVersion(getFeeVersionDto(status));
        feeDto.setJurisdiction1Dto(jurisdiction1Service.findByNameOrThrow("civil"));
        feeDto.setJurisdiction2Dto(jurisdiction2Service.findByNameOrThrow("county court"));
        feeDto.setDirectionTypeDto(directionTypeService.findByNameOrThrow("enhanced"));
        feeDto.setEventTypeDto(eventTypeService.findByNameOrThrow("issue"));
        feeDto.setServiceTypeDto(serviceTypeService.findByNameOrThrow("civil money claims"));
        feeDto.setChannelTypeDto(channelTypeService.findByNameOrThrow("online"));
        feeDto.setMemoLine("Test memo line");
        feeDto.setFeeOrderName("CMC online fee order name");
        feeDto.setNaturalAccountCode("Natural code 001");

        return feeDto;
    }

    public FeeVersionDto getFeeVersionDto(FeeVersionStatus status) {
        MutableDateTime validTo = new MutableDateTime(new Date());
        validTo.addDays(90);

        return new FeeVersionDto(1, new Date(), validTo.toDate(), "First version description", status, getFlatAmountDto(), null);
    }

    public FlatAmountDto getFlatAmountDto() {
        return new FlatAmountDto(new BigDecimal(2500));
    }

    public PercentageAmountDto getPercentageAmountDto() {
        return new PercentageAmountDto(new BigDecimal(4.5));
    }

}
