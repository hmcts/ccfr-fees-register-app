package uk.gov.hmcts.fees2.register.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.advice.exception.BadRequestException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tarun on 10/11/2017.
 */

@Component
public class FeeLoader implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(FeeLoader.class);

    @Value("classpath:${ranged.fees.loader}")
    private String rangedFeesJsonInputFile;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeeController feeController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            List<CreateRangedFeeDto> feeDtos = loadFromResource(rangedFeesJsonInputFile);

            feeDtos.forEach(fee -> {
               try {
                   feeController.createRangedFee(fee, null);
                   LOG.info("Fee with code " + fee.getCode() + " inserted into database.");
               } catch (BadRequestException be) {
                   LOG.info(FeeDtoMapper.CODE_ALREADY_IN_USE + " : " + fee.getCode());
               }

            });

        } catch (IOException  | NullPointerException ex) {
            LOG.error("Error is loading cmc fee json loader");
        }


    }

    private List<CreateRangedFeeDto> loadFromResource(String location) throws IOException {
        InputStream fileAsInputStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, CreateRangedFeeDto.class));
    }
}
