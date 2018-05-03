package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.repository.FeeVersionRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FeeVersionServiceImpl implements FeeVersionService {

    private final FeeVersionRepository feeVersionRepository;

    private final Fee2Repository feeRepository;

    @Autowired
    public FeeVersionServiceImpl(FeeVersionRepository feeVersionRepository, Fee2Repository feeRepository) {
        this.feeVersionRepository = feeVersionRepository;
        this.feeRepository = feeRepository;
    }

    @Override
    @Transactional
    public boolean approve(String code, Integer version, String name) {

        FeeVersion ver = feeVersionRepository.findByFee_CodeAndVersion(code, version);

        if (ver == null) {
            throw new FeeVersionNotFoundException(code);
        }

        ver.setApprovedBy(name);

        ver.setStatus(FeeVersionStatus.approved);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteDraftVersion(String feeCode, Integer version) {

        FeeVersion feeVersion = feeVersionRepository.findByFee_CodeAndVersion(feeCode, version);

        if (!feeVersion.getStatus().equals(FeeVersionStatus.draft)) {
            throw new BadRequestException("Non draft fee versions can not be deleted by this operation");
        }

        if (feeVersion.getFee().getFeeVersions().size() == 1) {
            feeRepository.delete(feeVersion.getFee());
        } else {
            feeVersionRepository.delete(feeVersion);
        }

        return false;
    }

    @Override
    public List<FeeVersion> getUnapprovedVersions() {
        return feeVersionRepository.findByStatus(FeeVersionStatus.pending_approval);
    }

    @Override
    public List<FeeVersion> getFeesVersionByStatus(FeeVersionStatus feeVersionStatus) {
        return feeVersionRepository.findByStatus(feeVersionStatus);
    }

    @Override
    public List<FeeVersion> getDraftVersions(String author) {
        if (author == null) {
            return feeVersionRepository.findByStatus(FeeVersionStatus.draft);
        } else {
            return feeVersionRepository.findByStatusAndAuthor(FeeVersionStatus.draft, author);
        }
    }

    @Override
    @Transactional
    public FeeVersion save(FeeVersion version, String feeCode) {
        Fee fee = feeRepository.findByCodeOrThrow(feeCode);
        version.setFee(fee);
        feeVersionRepository.save(version);
        return version;
    }

    @Override
    @Transactional
    public void changeStatus(String feeCode, Integer version, FeeVersionStatus newStatus, String user) {
        FeeVersion feeVersion = feeVersionRepository.findByFee_CodeAndVersion(feeCode, version);

        if (feeVersion.getStatus() == FeeVersionStatus.approved) {
            throw new BadRequestException("Approved fees cannot change their status");
        }

        if (newStatus == FeeVersionStatus.approved) {

            if (feeVersion.getStatus() != FeeVersionStatus.pending_approval) {
                throw new BadRequestException("Only fees pending approval can be approved");
            }

            feeVersion.setApprovedBy(user);

            setValidToOnPreviousFeeVersion(feeVersion);
        }

        feeVersion.setStatus(newStatus);
    }


    @Override
    public Integer getMaxFeeVersion(String feeCode) {
        return feeVersionRepository.getMaxFeeVersion(feeCode);
    }

    private void setValidToOnPreviousFeeVersion(FeeVersion newFeeVersion) {

        if (newFeeVersion.getValidFrom() != null) {

            FeeVersion currentVersion = newFeeVersion.getFee().getCurrentVersion(true);

            if (currentVersion != null && currentVersion.getValidTo() == null) {

                currentVersion.setValidTo(newFeeVersion.getValidFrom());
            }

        }

    }

    @Override
    @Transactional
    public void updateVersion(String feeCode, Integer versionId, BigDecimal amount) {
        FeeVersion version = feeVersionRepository.findByFee_CodeAndVersion(feeCode, versionId);
        version.getAmount().setAmountValue(amount);

    }

}
