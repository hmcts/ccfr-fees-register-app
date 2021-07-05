package uk.gov.hmcts.fees2.register.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.DirectionTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.repository.FeeVersionRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.data.util.FeesDateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class FeeVersionServiceImpl implements FeeVersionService {
    private static final Logger LOG = LoggerFactory.getLogger(FeeVersionServiceImpl.class);

    private final FeeVersionRepository feeVersionRepository;

    private final Fee2Repository feeRepository;
    private final DirectionTypeRepository directionTypeRepository;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    public FeeVersionServiceImpl(final FeeVersionRepository feeVersionRepository, final Fee2Repository feeRepository,
                                 final DirectionTypeRepository directionTypeRepository) {
        this.feeVersionRepository = feeVersionRepository;
        this.feeRepository = feeRepository;
        this.directionTypeRepository = directionTypeRepository;
    }

    @Override
    @Transactional
    public boolean approve(final String code, final Integer version, final String name) {

        final FeeVersion ver = feeVersionRepository.findByFee_CodeAndVersion(code, version);

        if (ver == null) {
            throw new FeeVersionNotFoundException(code);
        }

        ver.setApprovedBy(name);

        ver.setStatus(FeeVersionStatus.approved);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteDraftVersion(final String feeCode, final Integer version) {

        final FeeVersion feeVersion = feeVersionRepository.findByFee_CodeAndVersion(feeCode, version);

        if (!feeVersion.getStatus().equals(FeeVersionStatus.draft)) {
            throw new BadRequestException("Non draft fee versions can not be deleted by this operation");
        }

        if (feeVersion.getFee().getFeeVersions().size() == 1) {
            feeRepository.delete(feeVersion.getFee());
        } else {
            feeVersion.getFee().getFeeVersions().remove(feeVersion);
            feeVersionRepository.delete(feeVersion);
        }

        return false;
    }

    @Override
    public List<FeeVersion> getUnapprovedVersions() {
        return feeVersionRepository.findByStatus(FeeVersionStatus.pending_approval);
    }

    @Override
    public List<FeeVersion> getFeesVersionByStatus(final FeeVersionStatus feeVersionStatus) {
        return feeVersionRepository.findByStatus(feeVersionStatus);
    }

    @Override
    public List<FeeVersion> getDraftVersions(final String author) {
        if (author == null) {
            return feeVersionRepository.findByStatus(FeeVersionStatus.draft);
        } else {
            return feeVersionRepository.findByStatusAndAuthor(FeeVersionStatus.draft, author);
        }
    }

    @Override
    @Transactional
    public FeeVersion save(final FeeVersion version, final String feeCode) {
        final Fee fee = feeRepository.findByCodeOrThrow(feeCode);
        version.setFee(fee);
        feeVersionRepository.save(version);
        return version;
    }


    @Override
    @Transactional
    public FeeVersion saveFeeVersion(final FeeVersion version) {
        feeVersionRepository.save(version);
        return version;
    }

    @Override
    @Transactional
    public FeeVersion getFeeVersion(final String feeCode, final Integer version) {
        return feeVersionRepository.findByFee_CodeAndVersion(feeCode, version);
    }

    @Override
    @Transactional
    public void changeStatus(final String feeCode, final Integer version, final FeeVersionStatus newStatus, final String user) {
        final FeeVersion feeVersion = getFeeVersion(feeCode, version, newStatus, user);
    }

    @Override
    @Transactional
    public void changeStatus(final String feeCode, final Integer version, final FeeVersionStatus newStatus, final String user, final String reason) {

        final FeeVersion feeVersion = getFeeVersion(feeCode, version, newStatus, user);
        feeVersion.setReasonForReject(reason);
        feeVersion.setApprovedBy(user);
    }

    private FeeVersion getFeeVersion(final String feeCode, final Integer version, final FeeVersionStatus newStatus, final String user) {
        final FeeVersion feeVersion = feeVersionRepository.findByFee_CodeAndVersion(feeCode, version);

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
        return feeVersion;
    }


    @Override
    public Integer getMaxFeeVersion(final String feeCode) {
        return feeVersionRepository.getMaxFeeVersion(feeCode);
    }

    private void setValidToOnPreviousFeeVersion(final FeeVersion newFeeVersion) {

        if (newFeeVersion.getValidFrom() != null) {

            final FeeVersion currentVersion = newFeeVersion.getFee().getCurrentVersion(true);

            if (currentVersion != null && currentVersion.getValidTo() == null) {
                if (sdf.format(currentVersion.getValidFrom()).equals(sdf.format(newFeeVersion.getValidFrom()))) {
                    currentVersion.setValidTo(newFeeVersion.getValidFrom());
                } else {
                    currentVersion.setValidTo(FeesDateUtil.addPreviousDateEODTime(newFeeVersion.getValidFrom()));
                }
            }

        }

    }

    @Override
    public void updateVersion(final String feeCode, final Integer versionId, final Integer newVersionId, final BigDecimal amount,
                              final FeeVersion feeVersion) {
        final FeeVersion version = feeVersionRepository.findByFee_CodeAndVersion(feeCode, versionId);

        if (version != null) {
            if (newVersionId != null) {
                version.setVersion(newVersionId);
            }
            version.getAmount().setAmountValue(amount);
            version.setValidFrom(feeVersion.getValidFrom());
            version.setDirectionType(
                    directionTypeRepository.findByNameOrThrow(feeVersion.getDirectionType().getName().toLowerCase()));
            version.setDescription(feeVersion.getDescription());
            version.setMemoLine(feeVersion.getMemoLine());
            version.setNaturalAccountCode(feeVersion.getNaturalAccountCode());
            version.setFeeOrderName(feeVersion.getFeeOrderName());
            version.setStatutoryInstrument(feeVersion.getStatutoryInstrument());
            version.setSiRefId(feeVersion.getSiRefId());
        }
    }


}
