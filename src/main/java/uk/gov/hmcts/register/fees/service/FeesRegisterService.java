package uk.gov.hmcts.register.fees.service;

import java.util.List;
import uk.gov.hmcts.register.fees.model.Category;
import uk.gov.hmcts.register.fees.model.Fee;
import uk.gov.hmcts.register.fees.model.FeesRegister;

public interface FeesRegisterService {

    FeesRegister getFeesRegister();

    List<Category> getAllCategories();

    Fee getFeeDetails(String id);

    Fee getFeeDetails(String id, int claimAmount);
}
