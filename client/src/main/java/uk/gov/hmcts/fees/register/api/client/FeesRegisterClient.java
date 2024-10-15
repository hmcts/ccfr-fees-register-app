package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@SuppressFBWarnings("HTTP_PARAMETER_POLLUTION")
public class FeesRegisterClient implements FeesRegister {


    private final HttpClient httpClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FeesRegisterClient(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public FeesRegisterDto getFeesRegister() {

        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register");
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), FeesRegisterDto.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }


    }

    @Override
    public List<CategoryDto> getCategories() {
        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register/categories");
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), List.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register/categories/" + categoryId);
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), CategoryDto.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }
    }

    @Override
    public FeesDto getFeesByCategoryAndAmount(String categoryId, int amount) {
        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register/categories/" + categoryId + "/range/" + amount);
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), FeesDto.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }
    }

    @Override
    public FeesDto getFeesByCategoryAndFeeId(String categoryId, String feeId) {
        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register/categories/" + categoryId + "/flat/" + feeId);
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), FeesDto.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }
    }

    @Override
    public List<FeesDto> getFlatFeesByCategory(String categoryId) {
        try {
            HttpGet request = new HttpGet(baseUrl + "/fees-register/categories/" + categoryId + "/flat");
            return httpClient.execute(request, httpResponse -> {
                checkStatusIs2xx(httpResponse);
                return objectMapper.readValue(httpResponse.getEntity().getContent(), List.class);
            });
        } catch (IOException e) {
            throw new FeesRegisterClientException(e);
        }
    }

    private void checkStatusIs2xx(HttpResponse httpResponse) throws IOException {
        if (httpResponse.getCode() != HttpStatus.SC_OK) {

            throw new FeesRegisterResponseException(httpResponse.getCode(), httpResponse.getReasonPhrase());
        }
    }
}
