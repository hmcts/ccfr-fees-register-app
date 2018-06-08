package uk.gov.hmcts.fees2.register.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import sun.security.acl.PrincipalImpl;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeLoaderJsonMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

/**
 * Created by tarun on 10/11/2017.
 */

@Component
public class FeeLoader implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(FeeLoader.class);

    private Principal AUTHOR = new PrincipalImpl("LOADER");

    @Value("classpath:${fees.loader.json}")
    private String feesJsonInputFile;

    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    private FeeVersionService feeVersionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        loadFees();
    }


    public void loadFees() throws Exception {
        FeeLoaderJsonMapper feeLoaderMapper = loadFile();

        loadFixedFees(feeLoaderMapper);
        loadRangedFees(feeLoaderMapper);
    }

    private void loadRangedFees(FeeLoaderJsonMapper feeLoaderMapper) {

        if (feeLoaderMapper.getRangedFees().size() > 0) {
            List<LoaderRangedFeeDto> rangedFees = feeLoaderMapper.getRangedFees();
            rangedFees.forEach(r -> {
                saveRangedFee(r);
            });
        }
    }

    private void saveRangedFee(LoaderRangedFeeDto r) {
        Fee fee = feeDtoMapper.toFee(r, null);

        try {
            if (feeService.get(r.getCode()) == null) {
                feeService.save(fee);
                LOG.info("Ranged fee with code " + r.getNewCode() + " inserted into database.");
            } else {
                try {
                    fee.setCode(r.getCode());
                    feeService.updateLoaderFee(fee, r.getNewCode());
                } catch (DataIntegrityViolationException ue) {
                    LOG.error("Update failed for the fee code: {}", r.getNewCode());
                }
            }

        } catch (FeeNotFoundException fe) {
            LOG.debug("Fee with code is not found: {}", fe);

            // Saving as a new fee.
            fee.setCode(r.getNewCode());
            feeService.saveLoaderFee(fee);
        }

        updateFeeVersion(r.getNewCode(), r.getVersion());
    }

    private void loadFixedFees(FeeLoaderJsonMapper feeLoaderMapper) {

        if (feeLoaderMapper.getFixedFees().size() > 0) {
            List<LoaderFixedFeeDto> fixedFees = feeLoaderMapper.getFixedFees();
            fixedFees.forEach(f -> {

               saveFixedFee(f);
            });
        }

    }

    private void saveFixedFee(LoaderFixedFeeDto f) {

        Fee fee = feeDtoMapper.toFee(f, null);

        if (f.getUnspecifiedClaimAmount() == null) {
            f.setUnspecifiedClaimAmount(false);
        }

        try {
            if (feeService.get(f.getCode()) == null) {
                feeService.save(fee);
                LOG.info("Fixed fee with code " + f.getNewCode() + " inserted into database.");
            } else {
                try {
                    fee.setCode(f.getCode());
                    feeService.updateLoaderFee(fee, f.getNewCode());
                } catch (DataIntegrityViolationException ue) {
                    LOG.error("Fee Update failed for the fee code: {}", f.getNewCode());
                }
            }

        } catch (FeeNotFoundException fe) {
            LOG.debug("Fee with code is not found: {}", fe);

            // Saving as a new fee.
            fee.setCode(f.getNewCode());
            feeService.saveLoaderFee(fee);

        }

        updateFeeVersion(f.getNewCode(), f.getVersion());
    }

    private FeeLoaderJsonMapper loadFile() throws Exception {
        try {
            return loadFromResource(feesJsonInputFile);

        } catch (IOException | NullPointerException ex) {
            LOG.error("Error is loading fee json loader");
            throw new Exception("Error in loading fee into the database.", ex);
        }
    }

    private FeeLoaderJsonMapper loadFromResource(String location) throws IOException {
        InputStream fileAsInputStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsInputStream, FeeLoaderJsonMapper.class);
    }

    private void updateFeeVersion(String code, LoaderFeeVersionDto feeVersionDto) {
        if (feeVersionDto.getAmount() != null) {
            feeVersionService.updateVersion(code, feeVersionDto.getVersion(), feeVersionDto.getNewVersion(), feeVersionDto.getValidFrom(),
                feeVersionDto.getAmount(), feeVersionDto.getDirection(), feeVersionDto.getDescription(), feeVersionDto.getMemoLine(),
                feeVersionDto.getNaturalAccountCode(), feeVersionDto.getFeeOrderName(), feeVersionDto.getStatutoryInstrument(), feeVersionDto.getSiRefId());
        }
    }

}
