package uk.gov.hmcts.fees2.register.api.controllers.base;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockUtils {

    private static final String AUTHOR = "TEST";
    @Autowired
    protected ObjectMapper objectMapper;


    public List<FixedFeeDto> getCsvImportFees() throws IOException {
        String  csvFees = "[\n" +
            "  {\n" +
            "    \"code\": \"X0MOCK1\",\n" +
            "    \"version\": {\n" +
            "     \"version\": \"1\",\n" +
            "     \"validFrom\": \"2017-11-06T16:33:37.040Z\",\n" +
            "     \"validTo\": \"2020-11-06T16:33:37.040Z\",\n" +
            "     \"description\": \"Testing1\",\n" +
            "     \"status\": \"draft\",\n" +
            "   \"direction\": \"enhanced\",\n" +
            "   \"memoLine\": \"Test memo line\",\n" +
            "   \"feeOrderName\": \"CMC online fee order name\",\n" +
            "   \"naturalAccountCode\": \"Natural code 001\",\n" +
            "      \"flatAmount\": {\n" +
            "      \"amount\": \"150\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"jurisdiction1\": \"family\",\n" +
            "   \"jurisdiction2\": \"court of protection\",\n" +
            "   \"service\": \"divorce\",\n" +
            "   \"channel\": \"default\",\n" +
            "   \"event\": \"issue\"\n" +
            "  }\n" +
            "]";

        TypeReference<List<FixedFeeDto>> fixedFeeDtos = new TypeReference<List<FixedFeeDto>>(){};
        return objectMapper.readValue(csvFees, fixedFeeDtos);
    }

    public String getFeeJson() {
        return "[\n" +
            "  {\n" +
            "    \"code\": \"X0MOCK1\",\n" +
            "    \"version\": {\n" +
            "     \"version\": \"1\",\n" +
            "     \"validFrom\": \"2017-11-06T16:33:37.040Z\",\n" +
            "     \"validTo\": \"2020-11-06T16:33:37.040Z\",\n" +
            "     \"description\": \"Testing1\",\n" +
            "     \"status\": \"draft\",\n" +
            "   \"direction\": \"enhanced\",\n" +
            "   \"memoLine\": \"Test memo line\",\n" +
            "   \"feeOrderName\": \"CMC online fee order name\",\n" +
            "   \"naturalAccountCode\": \"Natural code 001\",\n" +
            "      \"flatAmount\": {\n" +
            "      \"amount\": \"150\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"jurisdiction1\": \"family\",\n" +
            "   \"jurisdiction2\": \"court of protection\",\n" +
            "   \"service\": \"civil money claims\",\n" +
            "   \"channel\": \"default\",\n" +
            "   \"event\": \"issue\"\n" +
            "  }\n" +
            "]";
    }

    public Fee getFixedFee(String code) {
        Fee fee = new FixedFee();
        fee.setCode(code);
        fee.setChannelType(new ChannelType("default", null, null));
        fee.setEventType(new EventType("issue", null, null));
        fee.setJurisdiction1(new Jurisdiction1("family", null, null));
        fee.setJurisdiction2(new Jurisdiction2("court of protection", null, null));
        fee.setService(new ServiceType("civil money claims", null, null));
        fee.setFeeVersions(getFeeVersions());

        return fee;
    }

    public List<FeeVersion> getFeeVersions() {
        List<FeeVersion> feeVersions = new ArrayList<>();
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(getFlatAmount());
        feeVersion.setDescription("Testing1");
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setVersion(1);
        feeVersion.setDirectionType(new DirectionType("enhanced", null, null));
        feeVersion.setFeeOrderName("CMC online fee order name");
        feeVersion.setNaturalAccountCode("Natural code 001");
        feeVersions.add(feeVersion);

        return feeVersions;
    }

    public FlatAmount getFlatAmount() {
        return new FlatAmount(new BigDecimal("150.00"));
    }

    public FixedFeeDto getFixedFeeDto() {

        FlatAmountDto flatAmountDto = new FlatAmountDto(new BigDecimal("150.00"));
        FeeVersionDto feeVersionDto = new FeeVersionDto(new Integer("1"),
            null,
            null, "Testing1", FeeVersionStatus.draft, flatAmountDto, null, null, AUTHOR, AUTHOR,
            "Test memo line", null, null, "Natural code 001", "CMC online fee order name",
            "enhanced");
        FixedFeeDto fixedFeeDto = new FixedFeeDto("X0MOCK1", null, feeVersionDto, "family",
            "court of protection", "civil money claims", "default", "issue", "all",false);

        return fixedFeeDto;
    }

}
