package uk.gov.hmcts.fees2.register.data;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FeeTest {

    @Test
    public void isDraftReturnsFalseWhenFeeHasAtLeastOneApprovedVersion() {
        Fee fee = new FixedFee();
        FeeVersion fv1 = new FeeVersion();
        fv1.setStatus(FeeVersionStatus.draft);

        FeeVersion fv2 = new FeeVersion();
        fv2.setStatus(FeeVersionStatus.approved);

        List<FeeVersion> feeVersions = new ArrayList<>();
        feeVersions.add(fv1);
        feeVersions.add(fv2);

        fee.setFeeVersions(feeVersions);

        assertFalse(fee.isDraft());
    }

    @Test
    public void isDraftReturnsTrueWhenFeeHasOnlyDraftVersions() throws Exception {
        Fee fee = new FixedFee();
        FeeVersion fv1 = new FeeVersion();
        fv1.setStatus(FeeVersionStatus.draft);

        FeeVersion fv2 = new FeeVersion();
        fv2.setStatus(FeeVersionStatus.draft);

        List<FeeVersion> feeVersions = new ArrayList<>();
        feeVersions.add(fv1);
        feeVersions.add(fv2);

        fee.setFeeVersions(feeVersions);

        assertTrue(fee.isDraft());
    }

}
