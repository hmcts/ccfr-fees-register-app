package uk.gov.hmcts.fees.register.functional.fixture;

import org.apache.commons.lang3.RandomStringUtils;

public class FixedFeeFixture {

    public static String aFixedFee() {
        String keyword = RandomStringUtils.randomAlphanumeric(10);

        String jsonString =  "{\n" +
            "  \"applicant_type\": \"all\",\n" +
            "  \"channel\": \"online\",\n" +
            "  \"event\": \"issue\",\n" +
            "  \"jurisdiction1\": \"tribunal\",\n" +
            "  \"jurisdiction2\": \"gambling tribunal\",\n" +
            "  \"service\": \"gambling\",\n" +
            "  \"keyword\": \"%s\",\n" +
            "  \"unspecified_claim_amount\": true,\n" +
            "  \"version\": {\n" +
            "    \"version\": 0,\n" +
            "    \"valid_from\": \"2018-09-17T14:17:48.504Z\",\n" +
            "    \"valid_to\": \"2018-10-17T14:17:48.504Z\",\n" +
            "    \"description\": \"Test fee - Filing an application for a divorce\",\n" +
            "    \"status\": \"draft\",\n" +
            "    \"flat_amount\": {\n" +
            "      \"amount\": 300\n" +
            "    },\n" +
            "    \"memo_line\": \"Test memo line\",\n" +
            "    \"statutory_instrument\": \"2016 No. 402\",\n" +
            "    \"si_ref_id\": \"Test 1.2\",\n" +
            "    \"natural_account_code\": \"Test nac\",\n" +
            "    \"fee_order_name\": \"The Civil Proceedings\",\n" +
            "    \"direction\": \"enhanced\"\n" +
            "  }\n" +
            "}";
        return String.format(jsonString, keyword);
    }
}
