package uk.gov.hmcts.fees2.register.api.controllers.base;

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
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.SecurityUtilsMock;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
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

    protected final static String AUTHOR = "TEST";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserResolverBackdoor userRequestAuthorizer;

    @Autowired
    protected SecurityUtilsMock securityUtilsMock;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected ChannelTypeRepository channelTypeRepository;

    @Autowired
    protected ChannelTypeService channelTypeService;

    @Autowired
    protected DirectionTypeRepository directionTypeRepository;

    @Autowired
    protected DirectionTypeService directionTypeService;

    @Autowired
    protected EventTypeRepository eventTypeRepository;

    @Autowired
    protected EventTypeService eventTypeService;

    @Autowired
    protected Jurisdiction1Repository jurisdiction1Repository;

    @Autowired
    protected Jurisdiction1Service jurisdiction1Service;

    @Autowired
    protected Jurisdiction2Repository jurisdiction2Repository;

    @Autowired
    protected Jurisdiction2Service jurisdiction2Service;

    @Autowired
    protected ServiceTypeRepository serviceTypeRepository;

    @Autowired
    protected ServiceTypeService serviceTypeService;

    protected RestActions restActions;

    protected MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, securityUtilsMock, objectMapper);
    }

    protected CustomResultMatcher body() {
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
            add(new DirectionType("licence", new Date(), new Date()));
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
            add(new ServiceType("protected law", new Date(), new Date()));
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

    public RangedFeeDto getRangedFeeDto(String feeCode) {


        RangedFeeDto rangedFeeDto = new RangedFeeDto();

        rangedFeeDto.setMinRange(new BigDecimal(1));
        rangedFeeDto.setMaxRange(new BigDecimal(3000));
        rangedFeeDto.setVersion(getFeeVersionDto(FeeVersionStatus.approved, "Test memo line", "CMC online fee order name",
            "Natural code 001", "SI", "siRefId", DirectionType.directionWith().name("enhanced").build()));
        rangedFeeDto.setJurisdiction1(null);
        rangedFeeDto.setJurisdiction2(null);
        rangedFeeDto.setEvent(null);
        rangedFeeDto.setService(null);
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return rangedFeeDto;
    }

    public RangedFeeDto getRangedFeeDtoWithReferenceData(int minRange, int maxRange, String feeCode, FeeVersionStatus status) {

        RangedFeeDto rangedFeeDto = new RangedFeeDto();
        rangedFeeDto.setMinRange(new BigDecimal(minRange));
        rangedFeeDto.setMaxRange(new BigDecimal(maxRange));
        rangedFeeDto.setVersion(getFeeVersionDto(status, "Test memo line", "CMC online fee order name", "Natural code 001", null, null, directionTypeService.findByNameOrThrow("enhanced")));
        rangedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        rangedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        rangedFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        rangedFeeDto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return rangedFeeDto;
    }

    public RangedFeeDto getRangeFeeDtoForSearch(int minRange, int maxRange, String feeCode, FeeVersionStatus status, String service) {
        RangedFeeDto rangedFeeDto = new RangedFeeDto();
        rangedFeeDto.setMinRange(new BigDecimal(minRange));
        rangedFeeDto.setMaxRange(new BigDecimal(maxRange));
        rangedFeeDto.setVersion(getFeeVersionDto(status, "Test memo line", "CMC online fee order name", "Natural code 001", null, null, directionTypeService.findByNameOrThrow("enhanced")));
        rangedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        rangedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        rangedFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        rangedFeeDto.setService(serviceTypeService.findByNameOrThrow(service).getName());
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("default").getName());

        return rangedFeeDto;
    }

    public RangedFeeDto getRangedFeeDtoForLookup(int minRange, int maxRange, String feeCode, FeeVersionStatus status) {

        RangedFeeDto rangedFeeDto = new RangedFeeDto();
        rangedFeeDto.setMinRange(new BigDecimal(minRange));
        rangedFeeDto.setMaxRange(new BigDecimal(maxRange));
        rangedFeeDto.setVersion(getFeeVersionDto(status, "Test lookup fee", "Divorce online fee order name", "Natural code for lookup", null, null, directionTypeService.findByNameOrThrow("licence")));
        rangedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("family").getName());
        rangedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("high court").getName());
        rangedFeeDto.setEvent(eventTypeService.findByNameOrThrow("copies").getName());
        rangedFeeDto.setService(serviceTypeService.findByNameOrThrow("divorce").getName());
        rangedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return rangedFeeDto;
    }

    public BandedFeeDto getBandedFeeDtoWithReferenceData(String feeCode, FeeVersionStatus status) {

        BandedFeeDto bandedFeeDto = new BandedFeeDto();
        bandedFeeDto.setVersion(getFeeVersionDto(status, "Test memo line", "CMC online fee order name", "Natural code 001", null, null, directionTypeService.findByNameOrThrow("enhanced")));
        bandedFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        bandedFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        bandedFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        bandedFeeDto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        bandedFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return bandedFeeDto;
    }

    public RelationalFeeDto getRelationalFeeDtoWithReferenceData(String feeCode, FeeVersionStatus status) {

        RelationalFeeDto relationalFeeDto = new RelationalFeeDto();
        relationalFeeDto.setVersion(getFeeVersionDto(status, "Test memo line", "CMC online fee order name", "Natural code 001", null, null, directionTypeService.findByNameOrThrow("enhanced")));
        relationalFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        relationalFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        relationalFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        relationalFeeDto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        relationalFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return relationalFeeDto;
    }

    public RateableFeeDto getRateableFeeDtoWithReferenceData(String feeCode, FeeVersionStatus status) {

        RateableFeeDto rateablelFeeDto = new RateableFeeDto();
        rateablelFeeDto.setVersion(getFeeVersionDto(status, "Test memo line", "CMC online fee order name", "Natural code 001", null, null, directionTypeService.findByNameOrThrow("enhanced")));
        rateablelFeeDto.setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName());
        rateablelFeeDto.setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName());
        rateablelFeeDto.setEvent(eventTypeService.findByNameOrThrow("issue").getName());
        rateablelFeeDto.setService(serviceTypeService.findByNameOrThrow("civil money claims").getName());
        rateablelFeeDto.setChannel(channelTypeService.findByNameOrThrow("online").getName());

        return rateablelFeeDto;
    }


    public FeeVersionDto getFeeVersionDto(FeeVersionStatus status, String memoLine, String feeOrderName, String naturalAccountCode, String statutoryInstrument, String siRefId, DirectionType direction) {
        MutableDateTime validTo = new MutableDateTime(new Date());
        validTo.addDays(90);

        return new FeeVersionDto(1, new Date(), validTo.toDate(), "First version description", FeeVersionStatusDto.valueOf(status.name()), getFlatAmountDto(), null, null, AUTHOR, AUTHOR,
            memoLine, statutoryInstrument, siRefId, naturalAccountCode, feeOrderName, direction.getName());
    }

    public FlatAmountDto getFlatAmountDto() {
        return new FlatAmountDto(new BigDecimal(2500));
    }

    public PercentageAmountDto getPercentageAmountDto() {
        return new PercentageAmountDto(new BigDecimal(4.5));
    }

}
