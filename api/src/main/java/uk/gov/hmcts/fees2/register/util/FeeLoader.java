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
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;

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
                   if (fee.getUnspecifiedClaimAmount() != null && fee.getUnspecifiedClaimAmount().equals(true)) {
                       CreateFixedFeeDto fixedFee = populateFixedFeeDto(fee);
                       feeController.createFixedFee(fixedFee, null);
                       LOG.info("Fixed fee with code " +fee.getCode()+ " loaded into database.");
                   } else {
                       feeController.createRangedFee(fee, null);
                       LOG.info("Ranged fee with code " +fee.getCode()+ " loaded into database.");
                   }
               } catch (BadRequestException be) {
                   LOG.info("Fee code " + fee.getCode() + " already in use");
               }

            });

        } catch (IOException  | NullPointerException ex) {
            LOG.error("Error is loading cmc fee json loader");
            throw new IOException("Error in loading fee into the database.");
        }


    }

    private List<CreateRangedFeeDto> loadFromResource(String location) throws IOException {
        InputStream fileAsInputStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, CreateRangedFeeDto.class));
    }

    private CreateFixedFeeDto populateFixedFeeDto(CreateRangedFeeDto rangedFeeDto) {
        CreateFixedFeeDto fixedFee = new CreateFixedFeeDto();
        fixedFee.setCode(rangedFeeDto.getCode());
        fixedFee.setChannel(rangedFeeDto.getChannel());
        fixedFee.setDirection(rangedFeeDto.getDirection());
        fixedFee.setEvent(rangedFeeDto.getEvent());
        fixedFee.setJurisdiction1(rangedFeeDto.getJurisdiction1());
        fixedFee.setJurisdiction2(rangedFeeDto.getJurisdiction2());
        fixedFee.setService(rangedFeeDto.getService());
        fixedFee.setUnspecifiedClaimAmount(rangedFeeDto.getUnspecifiedClaimAmount());
        fixedFee.setVersion(rangedFeeDto.getVersion());

        return fixedFee;
    }
}
