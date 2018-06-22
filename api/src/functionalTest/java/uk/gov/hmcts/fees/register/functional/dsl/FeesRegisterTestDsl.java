package uk.gov.hmcts.fees.register.functional.dsl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.*;
import uk.gov.hmcts.fees.register.functional.dto.ChargeableFeeWrapperDto;
import uk.gov.hmcts.fees.register.functional.tokens.UserTokenFactory;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Scope("prototype")
public class FeesRegisterTestDsl {
    private final ObjectMapper objectMapper;
    private final Map<String, String> headers = new HashMap<>();
    private final String baseUri;
    private Response response;
    private final UserTokenFactory userTokenFactory;

    @Autowired
    public FeesRegisterTestDsl(@Value("${base-urls.fees-register}") String baseUri, UserTokenFactory userTokenFactory) {
        this.objectMapper = new ObjectMapper();
        this.baseUri = baseUri;
        this.userTokenFactory = userTokenFactory;
    }

    public FeesRegisterGivenDsl given() {

        return new FeesRegisterGivenDsl();
    }

    public class FeesRegisterGivenDsl {

        public FeesRegisterGivenDsl userId(String id) {
                headers.put("Authorization", userTokenFactory.validTokenForUser(id));
                return this;
            }

        public FeesRegisterWhenDsl when() {

            return new FeesRegisterWhenDsl();
        }
    }

    public class FeesRegisterWhenDsl {
        private RequestSpecification newRequest() {
            return RestAssured.given().relaxedHTTPSValidation().baseUri(baseUri).contentType(ContentType.JSON).headers(headers);
        }

        public FeesRegisterWhenDsl createCategory(CategoryUpdateDto.CategoryUpdateDtoBuilder requestDto) {
            response = newRequest().body(requestDto.build()).put("/categories/kk143");
            return this;
        }

        public FeesRegisterWhenDsl createRangedFee(RangedFeeDto requestDto) {
            response = newRequest().body(requestDto).post("/fees-register/rangedfees");
            return this;
        }

        public FeesRegisterWhenDsl createFixedFee(uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto requestDto) {
            response = newRequest().body(requestDto).post("/fees-register/fixedfees");
            return this;
        }

     //   public FeesRegisterWhenDsl approvedFeeCode(ApproveFeeDto requestDto) {
       //     response = newRequest().body(requestDto).patch("/fees-register/fees/approve");
         //   return this;
        //}

        public FeesRegisterWhenDsl createFees(uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.FixedFeeDtoBuilder requestDto) {
            response = newRequest().body(requestDto.build()).put("/fees/krishna");
            return this;
        }

        public FeesRegisterWhenDsl createPercentage(PercentageFeeDto.PercentageFeeDtoBuilder requestDto) {
            response = newRequest().body(requestDto.build()).put("/fees/cmc-percentageTest");
            return this;
        }

        public FeesRegisterWhenDsl createRangeGroups(RangeGroupUpdateDto.RangeGroupUpdateDtoBuilder requestDto) {
            response = newRequest().body(requestDto.build()).put("/range-groups/cmc-testing");
            return this;
        }

         public FeesRegisterWhenDsl getCategoryByCode(String code) {
            response = newRequest().get("/categories/{code}", code);
            return this;
        }

        public FeesRegisterWhenDsl getLookUpResponse(String service, String jurisdiction1, String jurisdiction2, String channel, String event) {
            response = newRequest().get("/fees-register/fees/lookup?service={service}&jurisdiction1={jurisdiction1}&jurisdiction2={jurisdiction2}&channel={channel}&event={event}",
                    service, jurisdiction1, jurisdiction2, channel, event);
            return this;
        }

        public FeesRegisterWhenDsl getLookUpForCMCResponse(String service, String jurisdiction1, String jurisdiction2, String channel, String event, double amount_or_volume) {
            response = newRequest().get("/fees-register/fees/lookup?service={service}&jurisdiction1={jurisdiction1}&jurisdiction2={jurisdiction2}&channel={channel}&event={event}&amount_or_volume={amount_or_volume}",
                    service, jurisdiction1, jurisdiction2, channel, event, amount_or_volume);
            return this;
        }

        public FeesRegisterWhenDsl getLookUpForProbateResponse(String service, String jurisdiction1, String jurisdiction2, String channel, String event, String applicant_type, BigDecimal amount_or_volume) {
            response = newRequest().get("/fees-register/fees/lookup?service={service}&jurisdiction1={jurisdiction1}&jurisdiction2={jurisdiction2}&channel={channel}&event={event}&applicant_type={applicant_type}&amount_or_volume={amount_or_volume}",
                    service, jurisdiction1, jurisdiction2, channel, event,applicant_type, amount_or_volume);
            return this;
        }

        public FeesRegisterWhenDsl deleteFeeCode(String code) {
            response = newRequest().delete("/fees-register/fees/{code}", code);
            return this;
        }

        public FeesRegisterWhenDsl getAllCategories() {
            response = newRequest().get("/categories");
            return this;
        }

        public FeesRegisterWhenDsl getFeesByCode(String code) {
            response = newRequest().get("/fees/{code}", code);
            return this;
        }

        public FeesRegisterWhenDsl getFeesByCaluclations(String code, int value) {
            response = newRequest().get("/fees/{code}/calculations?value={value}", code, value);
            return this;
        }

        public FeesRegisterWhenDsl getFeeCodeAndVerifyStatus(String code) {
            response = newRequest().get("/fees-register/fees/{code}", code);
            return this;
        }

        public FeesRegisterWhenDsl getAllFees() {
            response = newRequest().get("/fees");
            return this;
        }
        public FeesRegisterWhenDsl getRangeGroupsByCode(String code) {
            response = newRequest().get("/range-groups/{code}", code);
            return this;
        }

        public FeesRegisterWhenDsl getRangeGroupByCaluclations(String code, int value) {
            response = newRequest().get("/range-groups/{code}/calculations?value={value}", code, value);
            return this;
        }

        public FeesRegisterWhenDsl getCMCPaperCalculationsUnspecified() {
            response = newRequest().get("/range-groups/cmc-paper/calculations/unspecified");
            return this;
        }

        public FeesRegisterWhenDsl getAllRangeGroups() {
            response = newRequest().get("/range-groups");
            return this;
        }

        public FeesRegisterWhenDsl getBuildInfo() {
            response = newRequest().get("/info");
            return this;
        }
        public FeesRegisterWhenDsl getAllAmountTypes() {
            response = newRequest().get("/amounttypes");
            return this;
        }

        public FeesRegisterWhenDsl getAllchannelTypes() {
            response = newRequest().get("/channel-types");
            return this;
        }
        public FeesRegisterWhenDsl getAllDirectionTypes() {
            response = newRequest().get("/direction-types");
            return this;
        }
        public FeesRegisterWhenDsl getAllEventTypes() {
            response = newRequest().get("/event-types");
            return this;
        }

        public FeesRegisterWhenDsl getAllFeeTypes() {
            response = newRequest().get("/feetypes");
            return this;
        }

        public FeesRegisterWhenDsl getAllJurisdictions1Types() {
            response = newRequest().get("/jurisdictions1");
            return this;
        }

        public FeesRegisterWhenDsl getAllJurisdictions2Types() {
            response = newRequest().get("/jurisdictions2");
            return this;
        }

        public FeesRegisterWhenDsl getAllServiceTypes() {
            response = newRequest().get("/service-types");
            return this;
        }


        public FeesRegisterThenDsl then() {
            return new FeesRegisterThenDsl();
        }

    }

    public class FeesRegisterThenDsl {

        private RequestSpecification newRequest() {
            return RestAssured.given().relaxedHTTPSValidation().baseUri(baseUri).contentType(ContentType.JSON).headers(headers);
        }

        public FeesRegisterThenDsl notFound() {
            response.then().statusCode(404);
            return this;
        }
        public FeesRegisterThenDsl deleteFeeCode1(String code) {
            response = newRequest().delete("/fees-register/fees/{code}", code);
            return this;
        }

        public FeesRegisterThenDsl getFeeCodeAndVerifyStatus1(String code) {
            response = newRequest().get("/fees-register/fees/{code}", code);
            return this;
        }

        public FeesRegisterThenDsl created(Consumer<CategoryDto> feesRegisterAssertions) {
            CategoryDto categoryDto = response.then().statusCode(200).extract().as(CategoryDto.class);
            feesRegisterAssertions.accept(categoryDto);
            return this;
        }

        public FeesRegisterThenDsl createdFees(Consumer<FeeDto> feesRegisterAssertions) {
            FeeDto feeDto = response.then().statusCode(200).extract().as(FeeDto.class);
            feesRegisterAssertions.accept(feeDto);
            return this;
        }

        public FeesRegisterThenDsl createdPercentage(Consumer<FeeDto> feesRegisterAssertions) {
            FeeDto feeDto = response.then().statusCode(200).extract().as(FeeDto.class);
            feesRegisterAssertions.accept(feeDto);
            return this;
        }

        public FeesRegisterThenDsl createdRangeGroups(Consumer<RangeGroupDto> feesRegisterAssertions) {
            RangeGroupDto RangeGroupDto = response.then().statusCode(200).extract().as(RangeGroupDto.class);
            feesRegisterAssertions.accept(RangeGroupDto);
            return this;
        }

        public FeesRegisterThenDsl createdRangedFee(Consumer<RangedFeeDto> feesRegisterAssertions) {
            RangedFeeDto RangedFeeDto = response.then().statusCode(201).extract().as(RangedFeeDto.class);
            feesRegisterAssertions.accept(RangedFeeDto);
            return this;
        }

        public FeesRegisterThenDsl badRequest() {
            response.then().statusCode(400);
            return this;
        }


        public FeesRegisterThenDsl ok() {
            response.then().statusCode(200);
            return this;
        }

        public FeesRegisterThenDsl isDeleted() {
            response.then().statusCode(204);
            return this;
        }

        public FeesRegisterThenDsl noContent() {
            response.then().statusCode(204);
            return this;
        }

        public FeesRegisterThenDsl isCreated() {
            response.then().statusCode(201);
            return this;
        }

        public <T> FeesRegisterThenDsl got(Class<T> type, Consumer<T> assertions) {
            T dto = response.then().statusCode(200).extract().as(type);
            assertions.accept(dto);
            return this;
        }

        @SneakyThrows
        public <T> FeesRegisterThenDsl gotChargeablePercentageFee(Consumer<ChargeableFeeWrapperDto> assertions) {
            InputStream responseInputStream = response.then().statusCode(200).extract().asInputStream();
            JavaType javaType = TypeFactory.defaultInstance().constructParametricType(ChargeableFeeWrapperDto.class, PercentageFeeDto.class);
            ChargeableFeeWrapperDto actual = objectMapper.readValue(responseInputStream, javaType);
            assertions.accept(actual);
            return this;
        }

        @SneakyThrows
        public <T> FeesRegisterThenDsl gotChargeableFixedFee(Consumer<ChargeableFeeWrapperDto> assertions) {
            InputStream responseInputStream = response.then().statusCode(200).extract().asInputStream();
            JavaType javaType = TypeFactory.defaultInstance().constructParametricType(ChargeableFeeWrapperDto.class, uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.class);
            ChargeableFeeWrapperDto actual = objectMapper.readValue(responseInputStream, javaType);
            assertions.accept(actual);
            return this;
        }
    }
}

