package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

import java.util.List;

public interface FeeVersionService {

    boolean deleteDraftVersion(String feeCode, Integer version);

    boolean approve(String code, Integer version);

    List<FeeVersion> getUnapprovedVersions();

    void save(FeeVersion version, String feeCode);


}
