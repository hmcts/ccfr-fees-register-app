package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tarun on 14/11/2017.
 */

public class FeeLoaderComponentTest extends BaseTest {

    /**
     *
     *
     */

    @Test
    public void testFeeLoaderForCmcPaperFees() throws Exception{
        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=default&service=civil money claims")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
               assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                   assertThat(fee2Dto.getCode()).isEqualTo("X0009");
                   assertThat(fee2Dto.getChannelTypeDto().getName()).isEqualTo("default");
                   assertThat(fee2Dto.getServiceTypeDto().getName()).isEqualTo("civil money claims");
                   assertThat(fee2Dto.getJurisdiction1Dto().getName()).isEqualTo("civil");
                   assertThat(fee2Dto.getJurisdiction2Dto().getName()).isEqualTo("county court");
                   //assertThat(fee2Dto.getDirectionTypeDto().getName()).isEqualTo("enhanced");
                   assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                   assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                   assertThat(fee2Dto.getFeeVersionDtos().get(0).getVersion()).isEqualTo(3);
                   assertThat(fee2Dto.getMinRange()).isEqualTo(new BigDecimal("200000.01"));
                   //assertThat(fee2Dto.getMemoLine()).isEqualTo("CC-Money claim >£200,000");
                   assertThat(fee2Dto.getRangeUnit()).isEqualTo("GBP");
               });
            }));
    }


    @Test
    public void testFeeLoaderForCmcOnlineFees() throws Exception {
        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=online&service=civil money claims")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                    assertThat(fee2Dto.getCode()).isEqualTo("X0433");
                    assertThat(fee2Dto.getServiceTypeDto().getName()).isEqualTo("civil money claims");
                    assertThat(fee2Dto.getChannelTypeDto().getName()).isEqualTo("online");
                    assertThat(fee2Dto.getMinRange()).isEqualTo(new BigDecimal("5000.01"));
                    assertThat(fee2Dto.getMaxRange()).isEqualTo(new BigDecimal("10000.00"));
                    assertThat(fee2Dto.getJurisdiction1Dto().getName()).isEqualTo("civil");
                    assertThat(fee2Dto.getJurisdiction2Dto().getName()).isEqualTo("county court");
                    //assertThat(fee2Dto.getDirectionTypeDto().getName()).isEqualTo("enhanced");
                    assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getVersion()).isEqualTo(1);
                    //assertThat(fee2Dto.getMemoLine()).isEqualTo("GOV.UK Pay online claims - Money Claim £5000-10000");
                    assertThat(fee2Dto.getRangeUnit()).isEqualTo("GBP");
                });
            }));
    }

    @Test
    public void testFeeLoaderForDivorceFee() throws Exception {
        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=default&service=divorce")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                Fee2Dto fixedFee = fee2Dtos.stream().filter(f -> f.getCode().equals("X0165")).findAny().get();
                assertThat(fixedFee.getCode()).isEqualTo("X0165");
                assertThat(fixedFee.getServiceTypeDto().getName()).isEqualTo("divorce");
                assertThat(fixedFee.getChannelTypeDto().getName()).isEqualTo("default");
                assertThat(fixedFee.getJurisdiction1Dto().getName()).isEqualTo("family");
                assertThat(fixedFee.getJurisdiction2Dto().getName()).isEqualTo("family court");
                assertThat(fixedFee.getCurrentVersion().getVersion()).isEqualTo(new Integer(4));
                assertThat(fixedFee.getCurrentVersion().getFlatAmount().getAmount()).isEqualTo(new BigDecimal("550.00"));
            }));
    }

    @Test
    public void testFeeLoaderForPersonalFee() throws Exception {
        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=default&service=probate&applicant_type=personal")
        .andExpect(status().isOk())
        .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
            Fee2Dto fee = fee2Dtos.stream().filter(f -> f.getCode().equals("X0250-1")).findAny().orElse(null);
            assertThat(fee.getCode()).isEqualTo("X0250-1");
            assertThat(fee.getJurisdiction1Dto().getName()).isEqualTo("family");
            assertThat(fee.getJurisdiction2Dto().getName()).isEqualTo("probate registry");
            assertThat(fee.getServiceTypeDto().getName()).isEqualTo("probate");
            assertThat(fee.getApplicantTypeDto().getName()).isEqualTo("personal");
            assertThat(fee.getCurrentVersion().getFlatAmount().getAmount()).isEqualTo(new BigDecimal("215.00"));
        }));
    }

    @Test
    public void testFeeLoaderForCMCUnspecifiedFee() throws Exception {

        restActions
            .get("/fees-register/fees/lookup-unspecified?service=civil money claims&jurisdiction1=civil&jurisdiction2=county court&event=issue&channel=default")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, (fee) -> {
                assertThat(fee.getCode()).isEqualTo("X0012");
                assertThat(fee.getFeeAmount()).isEqualTo(new BigDecimal("10000.00"));
                assertThat(fee.getVersion()).isEqualTo(1);
            }));

    }

    @Test
    public void testFeeLoader_toUpdate_RangedFeeAttribute() throws Exception {

        /* -- Before fee update -- */
        restActions
            .get("/fees-register/fees/X0249_NO_FEE")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo("X0249_NO_FEE");
                assertThat(fee2Dto.getMinRange()).isEqualTo(new BigDecimal("0.00"));
                assertThat(fee2Dto.getMaxRange()).isEqualTo(new BigDecimal("5000.00"));
            }));

        restActions
            .withUser("admin")
            .put("/fees-register/ranged-fees/X0249_NO_FEE", getUpdatedRangedFeeRequest())
            .andExpect(status().isNoContent());

        /* -- After fee update */
        restActions
            .get("/fees-register/fees/X0249_NO_FEE")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo("X0249_NO_FEE");
                assertThat(fee2Dto.getMinRange()).isEqualTo(new BigDecimal("1000.00"));
                assertThat(fee2Dto.getMaxRange()).isEqualTo(new BigDecimal("5000.00"));
            }));
    }


    @Test
    public void testFeeLoader_toUpdate_FixedFeeAttributes() throws Exception {

        /* -- Before fee update -- */
        restActions
            .get("/fees-register/fees/X0165")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo("X0165");
                assertThat(fee2Dto.getCurrentVersion().getMemoLine()).isEqualTo("GOV - App for divorce/nullity of marriage or CP");
                assertThat(fee2Dto.getCurrentVersion().getVersion()).isEqualTo(4);
            }));

        restActions
            .withUser("admin")
            .put("/fees-register/fixed-fees/X0165", getUpdateFixedFeeRequest())
            .andExpect(status().isNoContent());

        /* -- After fee update */
        restActions
            .get("/fees-register/fees/X0165")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo("X0165");
                assertThat(fee2Dto.getCurrentVersion().getMemoLine()).isEqualTo("New memo line");
                assertThat(fee2Dto.getCurrentVersion().getVersion()).isEqualTo(5);
                assertThat(fee2Dto.getCurrentVersion().getStatutoryInstrument()).isEqualTo("New statutory instrument");
            }));
    }


    private CreateRangedFeeDto getUpdatedRangedFeeRequest() throws Exception {
        return objectMapper.readValue(getModifiedRangedFeeJSON().getBytes(), CreateRangedFeeDto.class);
    }

    private CreateFixedFeeDto getUpdateFixedFeeRequest() throws Exception {
        return objectMapper.readValue(getModifiedFixedFeeJson().getBytes(), CreateFixedFeeDto.class);
    }

    private String getModifiedRangedFeeJSON() {
        return "{\n" +
            "      \"min_range\": 1000.00,\n" +
            "      \"max_range\": 5000.00,\n" +
            "      \"range_unit\": \"GBP\",\n" +
            "      \"code\": \"X0249_NO_FEE\",\n" +
            "      \"version\": {\n" +
            "        \"version\": 1,\n" +
            "        \"valid_from\" : \"2011-04-04T00:00:00.511Z\",\n" +
            "        \"description\": \"Application for a grant of probate (Estate under £5000)\",\n" +
            "        \"status\": \"approved\",\n" +
            "        \"statutory_instrument\": \"2011 No. 588 (L. 4)\",\n" +
            "        \"fee_order_name\": \"Non-Contentious Probate Fees\",\n" +
            "        \"si_ref_id\": \"1\",\n" +
            "        \"direction\": \"cost recovery\",\n" +
            "        \"flat_amount\": {\n" +
            "          \"amount\": 0\n" +
            "        }\n" +
            "      },\n" +
            "      \"jurisdiction1\": \"family\",\n" +
            "      \"jurisdiction2\": \"probate registry\",\n" +
            "      \"service\": \"probate\",\n" +
            "      \"channel\": \"default\",\n" +
            "      \"event\": \"issue\",\n" +
            "      \"applicant_type\": \"all\"\n" +
            "    }";
    }

    private String getModifiedFixedFeeJson() {
        return "{\n" +
            "      \"code\": \"X0165\",\n" +
            "      \"version\": {\n" +
            "        \"version\": 5,\n" +
            "        \"valid_from\": \"2016-03-21T00:00:00.000Z\",\n" +
            "        \"description\": \"Filing an application for a divorce, nullity or civil partnership dissolution – fees order 1.2.\",\n" +
            "        \"status\": \"approved\",\n" +
            "        \"direction\": \"enhanced\",\n" +
            "        \"memo_line\": \"New memo line\",\n" +
            "        \"statutory_instrument\": \"New statutory instrument\",\n" +
            "        \"si_ref_id\": \"New si ref id\",\n" +
            "        \"fee_order_name\": \"The Civil Proceedings, Family Proceedings and Upper Tribunal Fees (Amendment) Order 2016\",\n" +
            "        \"natural_account_code\":\"4481102159\",\n" +
            "        \"flat_amount\": {\n" +
            "          \"amount\": 550.00\n" +
            "        }\n" +
            "      },\n" +
            "      \"jurisdiction1\": \"family\",\n" +
            "      \"jurisdiction2\": \"family court\",\n" +
            "      \"service\": \"divorce\",\n" +
            "      \"channel\": \"default\",\n" +
            "      \"event\": \"issue\",\n" +
            "      \"applicant_type\": \"all\"\n" +
            "    }";
    }
}
