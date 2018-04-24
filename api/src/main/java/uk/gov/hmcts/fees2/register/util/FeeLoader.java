package uk.gov.hmcts.fees2.register.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import sun.security.acl.PrincipalImpl;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeLoaderJsonMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

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

    @Value("${reform.environment}")
    private String environment;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            FeeLoaderJsonMapper feeLoaderMapper = loadFromResource(feesJsonInputFile);

            if (feeLoaderMapper.getFixedFees().size() > 0) {
                List<LoaderFixedFeeDto> fixedFees = feeLoaderMapper.getFixedFees();
                fixedFees.forEach(f -> {
                    Fee fee = feeDtoMapper.toFee(f, null);

                    if (environment != null && environment.equals("local")) {
                        fee.setCode(f.getNewCode());
                        feeService.save(fee);
                    } else {
                        try {
                            if (f.getUnspecifiedClaimAmount() == null) {
                                f.setUnspecifiedClaimAmount(false);
                            }

                            if (feeService.get(f.getCode()) == null) {
                                feeService.save(fee);
                                LOG.info("Fixed fee with code " +f.getNewCode()+ " inserted into database.");
                            } else {
                                try {
                                    feeService.updateFeeLoaderData(fee, f.getNewCode());
                                } catch (DataIntegrityViolationException ue) {
                                    LOG.info("Update failed for the fee code: {}", f.getNewCode());
                                }
                            }
                        } catch (FeeNotFoundException fe) {
                            LOG.debug("Fee with code is not found:", fe);
                        }
                    }
                });
            }

            if (feeLoaderMapper.getRangedFees().size() > 0) {
                List<LoaderRangedFeeDto> rangedFees = feeLoaderMapper.getRangedFees();
                rangedFees.forEach(r -> {
                    Fee fee = feeDtoMapper.toFee(r, null);

                    if (environment != null && environment.equals("local")) {
                        fee.setCode(r.getNewCode());
                        feeService.save(fee);
                    } else {
                        try {
                            if (feeService.get(r.getCode()) == null) {
                                feeService.save(fee);
                                LOG.info("Ranged fee with code " + r.getNewCode() + " inserted into database.");
                            } else {
                                try {
                                    feeService.updateFeeLoaderData(fee, r.getNewCode());
                                } catch (DataIntegrityViolationException ue) {
                                    LOG.info("Update failed for the fee code: {}", r.getNewCode());
                                }
                            }
                        }
                        catch (FeeNotFoundException fe) {
                            LOG.debug("Fee not found exception: {}", fe);
                        }
                    }
                });
            }


        } catch (IOException  | NullPointerException ex) {
            LOG.error("Error is loading cmc fee json loader");
            throw new Exception("Error in loading fee into the database.", ex);
        }
    }

    private FeeLoaderJsonMapper loadFromResource(String location) throws IOException {
        InputStream fileAsInputStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsInputStream, FeeLoaderJsonMapper.class);
    }

}
