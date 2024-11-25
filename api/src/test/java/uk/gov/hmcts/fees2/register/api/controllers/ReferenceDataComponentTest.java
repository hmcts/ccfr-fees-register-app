package uk.gov.hmcts.fees2.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Reference data verification component test
 *
 */

public class ReferenceDataComponentTest extends BaseTest {


    @Test
    public void getAllApplicationTypeTest() throws Exception {

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllApplicantTypes"))
            .andExpect(body().asListOf(ApplicantTypeDto.class, applicantTypeDtos -> {
                assertThat(applicantTypeDtos.size()).isEqualTo(3);
                assertThat(applicantTypeDtos).contains(
                    ApplicantTypeDto.applicantTypeDtoWith()
                        .name("personal")
                        .build(),
                    ApplicantTypeDto.applicantTypeDtoWith()
                        .name("professional")
                        .build(),
                    ApplicantTypeDto.applicantTypeDtoWith()
                        .name("all")
                        .build()
                );
            }));
    }


    @Test
    public void getAllChannelTypesTest() throws Exception{

        restActions
            .get(URIUtils.getUrlForGetMethod(ReferenceDataController.class, "getAllChannelTypes"))
            .andExpect(body().asListOf(ChannelTypeDto.class, channelTypeDtos -> {
                assertThat(channelTypeDtos.size()).isEqualTo(3);
                assertThat(channelTypeDtos).contains(
                    ChannelTypeDto.channelTypeDtoWith()
                        .name("default")
                        .build(),
                    ChannelTypeDto.channelTypeDtoWith()
                        .name("online")
                        .build(),
                    ChannelTypeDto.channelTypeDtoWith()
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
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("cost recovery")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("enhanced")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("licence")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("partial cost recovery")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("pre cost recovery")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
                        .name("reduced churn")
                        .build(),
                    DirectionTypeDto.directionTypeDtoWith()
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
                assertThat(eventTypeDtos.size()).isEqualTo(9);
                assertThat(eventTypeDtos).contains(
                    EventTypeDto.eventTypeDtoWith()
                        .name("enforcement")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("appeal")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("search")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("issue")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("general application")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("copies")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("hearing")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
                        .name("miscellaneous")
                        .build(),
                    EventTypeDto.eventTypeDtoWith()
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
                assertThat(jurisdiction2Dtos.size()).isEqualTo(15);
                assertThat(jurisdiction2Dtos).contains(
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("county court")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("high court")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("magistrates court")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("court of protection")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("family court")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("probate registry")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("gambling tribunal")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("gender recognition panel")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("immigration and asylum chamber")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("property chamber")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                        .name("upper tribunal immigration and asylum chamber")
                        .build(),
                    Jurisdiction2Dto.jurisdiction2TypeDtoWith()
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
                assertThat(serviceTypeDtos.size()).isEqualTo(18);
                assertThat(serviceTypeDtos).contains(
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("civil money claims")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("possession claim")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("insolvency")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("private law")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("public law")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("divorce")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("adoption")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("gambling")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("gender recognition")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("immigration and asylum")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("property")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("probate")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("general")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("magistrates")
                        .build(),
                    ServiceTypeDto.serviceTypeDtoWith()
                        .name("other")
                        .build()
                );
            }));
    }




}
