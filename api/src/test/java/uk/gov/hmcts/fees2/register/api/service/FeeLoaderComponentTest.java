package uk.gov.hmcts.fees2.register.api.service;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;

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
    public void testFeeLoaderCmcPaperFees() throws Exception{
        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=default&service=civil money claims")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
               assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                   assertThat(fee2Dto.getCode()).isEqualTo("X0012");
                   assertThat(fee2Dto.getChannelTypeDto().getName()).isEqualTo("default");
                   assertThat(fee2Dto.getServiceTypeDto().getName()).isEqualTo("civil money claims");
                   assertThat(fee2Dto.getJurisdiction1Dto().getName()).isEqualTo("civil");
                   assertThat(fee2Dto.getJurisdiction2Dto().getName()).isEqualTo("county court");
                   assertThat(fee2Dto.getDirectionTypeDto().getName()).isEqualTo("enhanced");
                   assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                   assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                   assertThat(fee2Dto.getFeeVersionDtos().get(0).getVersion()).isEqualTo(1);
                   assertThat(fee2Dto.getMinRange()).isEqualTo(new BigDecimal("200000.01"));
               });
            }));
    }


    @Test
    public void testFeeLoaderCmcOnlineFees() throws Exception {
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
                    assertThat(fee2Dto.getDirectionTypeDto().getName()).isEqualTo("enhanced");
                    assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getVersion()).isEqualTo(1);
                });
            }));
    }
}
