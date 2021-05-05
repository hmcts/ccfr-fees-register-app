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
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    public FeeVersionServiceImpl(FeeVersionRepository feeVersionRepository, Fee2Repository feeRepository, DirectionTypeRepository directionTypeRepository) {
        this.feeVersionRepository = feeVersionRepository;
        this.feeRepository = feeRepository;
        this.directionTypeRepository = directionTypeRepository;
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
                if(sdf.format(currentVersion.getValidFrom()).equals(sdf.format(newFeeVersion.getValidFrom()))){
                    currentVersion.setValidTo(newFeeVersion.getValidFrom());
                }else {
                    currentVersion.setValidTo(FeesDateUtil.addPreviousDateEODTime(newFeeVersion.getValidFrom()));
                }
            }

        }

    }

    @Override
    public void updateVersion(String feeCode, Integer versionId, Integer newVersionId, BigDecimal amount, FeeVersion feeVersion) {
        FeeVersion version = feeVersionRepository.findByFee_CodeAndVersion(feeCode, versionId);

        if (version != null) {
            if (newVersionId != null) {
                version.setVersion(newVersionId);
            }
            version.getAmount().setAmountValue(amount);
            version.setValidFrom(feeVersion.getValidFrom());
            version.setDirectionType(directionTypeRepository.findByNameOrThrow(feeVersion.getDirectionType().getName().toLowerCase()));
            version.setDescription(feeVersion.getDescription());
            version.setMemoLine(feeVersion.getMemoLine());
            version.setNaturalAccountCode(feeVersion.getNaturalAccountCode());
            version.setFeeOrderName(feeVersion.getFeeOrderName());
            version.setStatutoryInstrument(feeVersion.getStatutoryInstrument());
            version.setSiRefId(feeVersion.getSiRefId());
        }
    }


}
