package uk.gov.hmcts.fees2.register.api.controllers.provider;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("feeRegister_rangeGroup")
@PactBroker(scheme = "http", host = "localhost", port = "${PACT_BROKER_PORT:80}")
@Import(FeeRangeGroupProviderTestConfiguration.class)
public class FeeRangeGroupProviderTest {

    @Autowired
    FeeDtoMapper feeDtoMapper;

    @Autowired
    FeeSearchService feeSearchService;

    @MockBean
    FeeService feeService;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) throws ParseException {
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setPrintRequestResponse(true);
        FeeController feeController = new FeeController(feeService, feeDtoMapper, feeSearchService);
        testTarget.setControllers(feeController);
        if (context != null) {
            context.setTarget(testTarget);
        }
    }

    @State("Money Claims Fees exists for Civil")
    public void lookupCivilMoneyClaimsFees() throws ParseException {

        FeeVersionDto feeVersionDto = FeeVersionDto.feeVersionDtoWith()
            .approvedBy("approvedBy")
            .author("author")
            .description("description")
            .direction("direction")
            .flatAmount(new FlatAmountDto(new BigDecimal(100)))
            .memoLine("memoLine")
            .naturalAccountCode("naturalAccountCode")
            .percentageAmount(new PercentageAmountDto(new BigDecimal(100)))
            .siRefId("siRefId")
            .status(FeeVersionStatusDto.approved)
            .statutoryInstrument("statutoryInstrument")
            .validFrom(new SimpleDateFormat("dd MMMM yyyy").parse("09 March 2015"))
            .validTo(new SimpleDateFormat("dd MMMM yyyy").parse("09 March 2022"))
            .version(100)
            .build();

        List<FeeVersionDto> feeVersionDtos = new ArrayList<>();
        feeVersionDtos.add(feeVersionDto);

        FeeVersion feeVersion = FeeVersion.feeVersionWith()
            .approvedBy("approvedBy")
            .author("author")
            .description("description")
            .directionType(DirectionType.directionWith().name("direction").build())
            .amount(new FlatAmount(new BigDecimal(100)))
            .memoLine("memoLine")
            .naturalAccountCode("naturalAccountCode")
            .siRefId("siRefId")
            .status(FeeVersionStatus.approved)
            .statutoryInstrument("statuatoryInstrument")
            .validFrom(new SimpleDateFormat("dd MMMM yyyy").parse("09 March 2015"))
            .validTo(new SimpleDateFormat("dd MMMM yyyy").parse("09 March 2022"))
            .version(100)
            .build();

        List<FeeVersion> feeVersions = new ArrayList<>();
        feeVersions.add(feeVersion);

        Fee2Dto fee2Dto = Fee2Dto.fee2DtoWith()
            .applicantTypeDto(ApplicantTypeDto.applicantTypeDtoWith().name("name").build())
            .channelTypeDto(ChannelTypeDto.channelTypeDtoWith().name("name").build())
            .code("code")
            .currentVersion(feeVersionDto)
            .eventTypeDto(EventTypeDto.eventTypeDtoWith().name("name").build())
            .feeType("FEETYPE")
            .feeVersionDtos(feeVersionDtos)
            .jurisdiction1Dto(Jurisdiction1Dto.jurisdiction1TypeDtoWith().name("name").build())
            .jurisdiction2Dto(Jurisdiction2Dto.jurisdiction2TypeDtoWith().name("name").build())
            .matchingVersion(feeVersionDto)
            .maxRange(new BigDecimal(100))
            .minRange(new BigDecimal(100))
            .rangeUnit("rangeUnit")
            .serviceTypeDto(ServiceTypeDto.serviceTypeDtoWith().name("name").build())
            .unspecifiedClaimAmount(true)
            .feeVersionDtos(feeVersionDtos)
            .keyword("keyword")
            .build();

        doReturn(feeVersions).when(feeSearchService).search(ArgumentMatchers.any(SearchFeeDto.class), ArgumentMatchers.any(SearchFeeVersionDto.class));

        when(feeDtoMapper.toFeeDto(ArgumentMatchers.any(FeeVersion.class))).thenReturn(fee2Dto);
    }
}
