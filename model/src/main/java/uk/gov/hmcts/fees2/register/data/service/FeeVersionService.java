package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;
import java.util.List;

public interface FeeVersionService {

    boolean deleteDraftVersion(String feeCode, Integer version);

    boolean approve(String code, Integer version, String name);

    List<FeeVersion> getUnapprovedVersions();

    List<FeeVersion> getDraftVersions(String author);

    List<FeeVersion> getFeesVersionByStatus(FeeVersionStatus feeVersionStatus);

    FeeVersion save(FeeVersion version, String feeCode);

    void changeStatus(String code, Integer version, FeeVersionStatus status, String name);

    void changeStatus(String code, Integer version, FeeVersionStatus status, String name, String reason);

    Integer getMaxFeeVersion(String feeCode);

    void updateVersion(String feeCode, Integer versionId, Integer newVersionId, BigDecimal amount, FeeVersion feeVersion);

    public FeeVersion getFeeVersion(String feeCode, Integer version) ;

    public FeeVersion saveFeeVersion(FeeVersion version);
}
