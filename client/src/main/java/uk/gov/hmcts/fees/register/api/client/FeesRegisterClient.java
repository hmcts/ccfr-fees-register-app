package uk.gov.hmcts.fees.register.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class FeesRegisterClient implements IFeesRegisterClient {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public FeesRegisterClient(RestTemplateBuilder restTemplateBuilder, @Value("${fees.register.url}") String url) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
    }

    public FeesRegisterDto getFeesRegister() {
        ResponseEntity<FeesRegisterDto> response =
            restTemplate
                .getForEntity(url + "/cmc", FeesRegisterDto.class);
        throwExceptionOnError(response);
        return response.getBody();
    }


    public List<CategoryDto> getCategoriesInFeesRegister() {
        ResponseEntity<List> response =
            restTemplate
                .getForEntity(url + "/cmc/categories", List.class);

        throwExceptionOnError(response);
        return response.getBody();
    }

    public CategoryDto getCategoryById(String categoryId) {
        ResponseEntity<CategoryDto> response =
            restTemplate
                .getForEntity(url + "/cmc/categories/{id}", CategoryDto.class, categoryId);
        throwExceptionOnError(response);
        return response.getBody();
    }


    public FeesDto getFeesForClaimAmountForAGivenCategory(String categoryId, int amount) {
        ResponseEntity<FeesDto> response =
            restTemplate
                .getForEntity(url + "/cmc/categories/{id}/range/{amount}", FeesDto.class, categoryId, amount);
        throwExceptionOnError(response);
        return response.getBody();
    }

    public FeesDto getFlatFeesForFeeIdInACategory(String categoryId, String feeId) {
        ResponseEntity<FeesDto> response =
             response =
                restTemplate
                    .getForEntity(url + "/cmc/categories/{id}/flat/{feeId}", FeesDto.class, categoryId, feeId);

        throwExceptionOnError(response);
        return response.getBody();
    }

    public List<FeesDto> getAllFlatFeesInACategory(String categoryId) {
        ResponseEntity<List> response =
            restTemplate
                .getForEntity(url + "/cmc/categories/{id}/flat", List.class, categoryId);

        throwExceptionOnError(response);
        return response.getBody();
    }


    private void throwExceptionOnError(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new FeesRegisterResponseException(response.getStatusCodeValue(), response.getStatusCode().getReasonPhrase());
        }
    }


}
