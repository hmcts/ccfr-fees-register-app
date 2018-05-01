package uk.gov.hmcts.fees2.register.api;

import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;

public class FeeTestFixtures {

    public static final String CODE = "testCode";

    public static String aRangeFeePayload() {
        return "{\n" +
            "  \"applicant\": \"string\",\n" +
            "  \"applicant_type\": \"string\",\n" +
            "  \"channel\": \"string\",\n" +
            "  \"code\": \"testCode\",\n" +
            "  \"event\": \"string\",\n" +
            "  \"jurisdiction1\": \"string\",\n" +
            "  \"jurisdiction2\": \"string\",\n" +
            "  \"max_range\": 0,\n" +
            "  \"min_range\": 0,\n" +
            "  \"range_unit\": \"string\",\n" +
            "  \"service\": \"string\",\n" +
            "  \"unspecified_claim_amount\": true,\n" +
            "  \"version\": {\n" +
            "    \"version\": 0,\n" +
            "    \"valid_from\": \"2018-04-18T14:15:13.102Z\",\n" +
            "    \"valid_to\": \"2018-04-18T14:15:13.103Z\",\n" +
            "    \"description\": \"string\",\n" +
            "    \"status\": \"draft\",\n" +
            "    \"flat_amount\": {\n" +
            "      \"amount\": 0\n" +
            "    },\n" +
            "    \"percentage_amount\": {\n" +
            "      \"percentage\": 0\n" +
            "    },\n" +
            "    \"volume_amount\": {\n" +
            "      \"amount\": 0\n" +
            "    },\n" +
            "    \"author\": \"string\",\n" +
            "    \"approvedBy\": \"string\",\n" +
            "    \"memo_line\": \"string\",\n" +
            "    \"statutory_instrument\": \"string\",\n" +
            "    \"si_ref_id\": \"string\",\n" +
            "    \"natural_account_code\": \"string\",\n" +
            "    \"fee_order_name\": \"string\",\n" +
            "    \"direction\": \"string\"\n" +
            "  }\n" +
            "}";
    }

    public static String aFixedFeePayload() {
        return "{\n" +
            "  \"applicant_type\": \"string\",\n" +
            "  \"channel\": \"string\",\n" +
            "  \"code\": \"string\",\n" +
            "  \"event\": \"string\",\n" +
            "  \"jurisdiction1\": \"string\",\n" +
            "  \"jurisdiction2\": \"string\",\n" +
            "  \"service\": \"string\",\n" +
            "  \"unspecified_claim_amount\": true,\n" +
            "  \"version\": {\n" +
            "    \"version\": 0,\n" +
            "    \"valid_from\": \"2018-04-18T14:15:13.086Z\",\n" +
            "    \"valid_to\": \"2018-04-18T14:15:13.086Z\",\n" +
            "    \"description\": \"string\",\n" +
            "    \"status\": \"draft\",\n" +
            "    \"flat_amount\": {\n" +
            "      \"amount\": 0\n" +
            "    },\n" +
            "    \"percentage_amount\": {\n" +
            "      \"percentage\": 0\n" +
            "    },\n" +
            "    \"volume_amount\": {\n" +
            "      \"amount\": 0\n" +
            "    },\n" +
            "    \"author\": \"string\",\n" +
            "    \"approvedBy\": \"string\",\n" +
            "    \"memo_line\": \"string\",\n" +
            "    \"statutory_instrument\": \"string\",\n" +
            "    \"si_ref_id\": \"string\",\n" +
            "    \"natural_account_code\": \"string\",\n" +
            "    \"fee_order_name\": \"string\",\n" +
            "    \"direction\": \"string\"\n" +
            "  }\n" +
            "}";
    }

    public static String aFeeVersionPayload() {
        return "{\n" +
            "  \"version\": 0,\n" +
            "  \"valid_from\": \"2018-04-18T14:15:13.130Z\",\n" +
            "  \"valid_to\": \"2018-04-18T14:15:13.130Z\",\n" +
            "  \"description\": \"string\",\n" +
            "  \"status\": \"draft\",\n" +
            "  \"flat_amount\": {\n" +
            "    \"amount\": 0\n" +
            "  },\n" +
            "  \"percentage_amount\": {\n" +
            "    \"percentage\": 0\n" +
            "  },\n" +
            "  \"volume_amount\": {\n" +
            "    \"amount\": 0\n" +
            "  },\n" +
            "  \"author\": \"string\",\n" +
            "  \"approvedBy\": \"string\",\n" +
            "  \"memo_line\": \"string\",\n" +
            "  \"statutory_instrument\": \"string\",\n" +
            "  \"si_ref_id\": \"string\",\n" +
            "  \"natural_account_code\": \"string\",\n" +
            "  \"fee_order_name\": \"string\",\n" +
            "  \"direction\": \"string\"\n" +
            "}";
    }

    public static String bulkFeesPayload() {
        return "[\n" +
            "  {\n" +
            "    \"applicant_type\": \"string\",\n" +
            "    \"channel\": \"string\",\n" +
            "    \"code\": \"string\",\n" +
            "    \"event\": \"string\",\n" +
            "    \"jurisdiction1\": \"string\",\n" +
            "    \"jurisdiction2\": \"string\",\n" +
            "    \"service\": \"string\",\n" +
            "    \"unspecified_claim_amount\": true,\n" +
            "    \"version\": {\n" +
            "      \"version\": 0,\n" +
            "      \"valid_from\": \"2018-04-18T14:15:13.019Z\",\n" +
            "      \"valid_to\": \"2018-04-18T14:15:13.019Z\",\n" +
            "      \"description\": \"string\",\n" +
            "      \"status\": \"draft\",\n" +
            "      \"flat_amount\": {\n" +
            "        \"amount\": 0\n" +
            "      },\n" +
            "      \"percentage_amount\": {\n" +
            "        \"percentage\": 0\n" +
            "      },\n" +
            "      \"volume_amount\": {\n" +
            "        \"amount\": 0\n" +
            "      },\n" +
            "      \"author\": \"string\",\n" +
            "      \"approvedBy\": \"string\",\n" +
            "      \"memo_line\": \"string\",\n" +
            "      \"statutory_instrument\": \"string\",\n" +
            "      \"si_ref_id\": \"string\",\n" +
            "      \"natural_account_code\": \"string\",\n" +
            "      \"fee_order_name\": \"string\",\n" +
            "      \"direction\": \"string\"\n" +
            "    }\n" +
            "  }\n" +
            "]";
    }

    public static FixedFee aFixedFee() {
        FixedFee fixedFee = new FixedFee();
        fixedFee.setCode(CODE);
        return fixedFee;
    }
    public static RangedFee aRangedFee() {
        RangedFee rangedFee = new RangedFee();
        rangedFee.setCode(CODE);
        return rangedFee;
    }
}
