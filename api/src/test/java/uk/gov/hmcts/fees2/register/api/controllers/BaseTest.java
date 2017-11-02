package uk.gov.hmcts.fees2.register.api.controllers;

import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tarun on 18/10/2017.
 */

public abstract class BaseTest {

    /**
     *
     * @return
     */
//    public List<Amount> getAmountTypes() {
//        return new ArrayList<Amount>(){{
//            add(new Amount("flat", new Date(), new Date()));
//            add(new Amount("percentage", new Date(), new Date()));
//            add(new Amount("rateable", new Date(), new Date()));
//        }};
//
//    }

    /**
     *
     * @return
     */
    public List<ChannelType> getChannelTypes() {
        return new ArrayList<ChannelType>(){{
            add(new ChannelType("online", new Date(), new Date()));
            add(new ChannelType("bulk", new Date(), new Date()));
        }};
    }

    /**
     *
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
     *
     * @return
     */
    public List<FeeType> getFeeTyes() {
        return new ArrayList<FeeType>() {{
            add(new FeeType("fixed fee", new Date(), new Date()));
            add(new FeeType("range fee", new Date(), new Date()));
        }};
    }

    /**
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

    public List<Jurisdiction2> getJurisdiction2() {
        return new ArrayList<Jurisdiction2>(){{
            add(new Jurisdiction2("county court", new Date(), new Date()));
            add(new Jurisdiction2("high court", new Date(), new Date()));
            add(new Jurisdiction2("magistrates court", new Date(), new Date()));
            add(new Jurisdiction2("court of protection", new Date(), new Date()));
        }};
    }

    public RangedFeeDto getRangedFeeDto() {
        return new RangedFeeDto(new BigDecimal(1), new BigDecimal(3000), "X0024", getFeeVersionDto(),
            null, null, null,
            null, null, "Test");
    }

    public FeeVersionDto getFeeVersionDto() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 90);
        return new FeeVersionDto(1, new Date(), cal.getTime(), "First version description", FeeVersionStatus.approved, getFlatAmountDto(), null);
    }

    public FlatAmountDto getFlatAmountDto() {
        return new FlatAmountDto(new BigDecimal(2500), Unit.POUNDS);
    }

    public PercentageAmountDto getPercentageAmountDto() {
        return new PercentageAmountDto(new BigDecimal(4.5));
    }
}
