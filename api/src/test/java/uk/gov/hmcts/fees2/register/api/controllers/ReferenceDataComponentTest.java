package uk.gov.hmcts.fees2.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees2.register.api.contract.ChannelTypeDto.*;
import static uk.gov.hmcts.fees2.register.api.contract.DirectionTypeDto.*;
import static uk.gov.hmcts.fees2.register.api.contract.EventTypeDto.*;
import static uk.gov.hmcts.fees2.register.api.contract.Jurisdiction2Dto.*;
import static uk.gov.hmcts.fees2.register.api.contract.ServiceTypeDto.*;

/**
 * Reference data verification component test
 *
 */

public class ReferenceDataComponentTest extends BaseTest {


    @Test
    public void getAllChannelTypesTest() throws Exception{

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllChannelTypes"))
            .andExpect(body().asListOf(ChannelTypeDto.class, channelTypeDtos -> {
                assertThat(channelTypeDtos.size()).isEqualTo(3);
                assertThat(channelTypeDtos).contains(
                    channelTypeDtoWith()
                        .name("default")
                        .build(),
                    channelTypeDtoWith()
                        .name("online")
                        .build(),
                    channelTypeDtoWith()
                        .name("bulk")
                        .build()
                );
            }));
    }

    @Test
    public void getDirectionTypesTest() throws Exception {

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllDirectionTypes"))
            .andExpect(body().asListOf(DirectionTypeDto.class, directionTypeDtos -> {
                assertThat(directionTypeDtos.size()).isEqualTo(7);
                assertThat(directionTypeDtos).contains(
                    directionTypeDtoWith()
                        .name("cost recovery")
                        .build(),
                    directionTypeDtoWith()
                        .name("enhanced")
                        .build(),
                    directionTypeDtoWith()
                        .name("licence")
                        .build(),
                    directionTypeDtoWith()
                        .name("partial cost recovery")
                        .build(),
                    directionTypeDtoWith()
                        .name("pre cost recovery")
                        .build(),
                    directionTypeDtoWith()
                        .name("reduced churn")
                        .build(),
                    directionTypeDtoWith()
                        .name("simplified")
                        .build()
                );
            }));
    }

    @Test
    public void getAllEventTypesTest() throws Exception {
        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllEventTypes"))
            .andExpect(status().isOk())
            .andExpect(body().asListOf(EventTypeDto.class, eventTypeDtos -> {
                assertThat(eventTypeDtos.size()).isEqualTo(10);
                assertThat(eventTypeDtos).contains(
                    eventTypeDtoWith()
                        .name("enforcement")
                        .build(),
                    eventTypeDtoWith()
                        .name("judicial review")
                        .build(),
                    eventTypeDtoWith()
                        .name("appeal")
                        .build(),
                    eventTypeDtoWith()
                        .name("search")
                        .build(),
                    eventTypeDtoWith()
                        .name("issue")
                        .build(),
                    eventTypeDtoWith()
                        .name("general application")
                        .build(),
                    eventTypeDtoWith()
                        .name("copies")
                        .build(),
                    eventTypeDtoWith()
                        .name("hearing")
                        .build(),
                    eventTypeDtoWith()
                        .name("miscellaneous")
                        .build(),
                    eventTypeDtoWith()
                        .name("cost assessment")
                        .build()
                );
            }));
    }

    @Test
    public void getAllJurisdicitons1Test() throws Exception {

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllJurisdictions1"))
            .andExpect(status().isOk());

    }

    @Test
    public void getAllJurisdicitons2Test() throws Exception {

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllJurisdictions2"))
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Jurisdiction2Dto.class, jurisdiction2Dtos -> {
                assertThat(jurisdiction2Dtos.size()).isEqualTo(13);
                assertThat(jurisdiction2Dtos).contains(
                    jurisdiction2TypeDtoWith()
                        .name("county court")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("high court")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("magistrates court")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("court of protection")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("family court")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("probate registry")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("gambling tribunal")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("gender recognition panel")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("immigration and asylum chamber")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("property chamber")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("tax chamber")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("upper tribunal immigration and asylum chamber")
                        .build(),
                    jurisdiction2TypeDtoWith()
                        .name("upper tribunal lands chamber")
                        .build()

                );
            }));
    }

    @Test
    public void getAllServiceTypes() throws Exception {

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllServiceTypes"))
            .andExpect(status().isOk())
            .andExpect(body().asListOf(ServiceTypeDto.class, serviceTypeDtos -> {
                assertThat(serviceTypeDtos.size()).isEqualTo(15);
                assertThat(serviceTypeDtos).contains(
                    serviceTypeDtoWith()
                        .name("civil money claims")
                        .build(),
                    serviceTypeDtoWith()
                        .name("possession claim")
                        .build(),
                    serviceTypeDtoWith()
                        .name("insolvency")
                        .build(),
                    serviceTypeDtoWith()
                        .name("private law")
                        .build(),
                    serviceTypeDtoWith()
                        .name("public law")
                        .build(),
                    serviceTypeDtoWith()
                        .name("divorce")
                        .build(),
                    serviceTypeDtoWith()
                        .name("adoption")
                        .build(),
                    serviceTypeDtoWith()
                        .name("gambling")
                        .build(),
                    serviceTypeDtoWith()
                        .name("gender recognition")
                        .build(),
                    serviceTypeDtoWith()
                        .name("immigration and asylum")
                        .build(),
                    serviceTypeDtoWith()
                        .name("property")
                        .build(),
                    serviceTypeDtoWith()
                        .name("tax")
                        .build(),
                    serviceTypeDtoWith()
                        .name("probate")
                        .build(),
                    serviceTypeDtoWith()
                        .name("general")
                        .build(),
                    serviceTypeDtoWith()
                        .name("magistrates")
                        .build()
                );
            }));
    }




}
