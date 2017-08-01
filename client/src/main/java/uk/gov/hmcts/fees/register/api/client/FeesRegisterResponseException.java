package uk.gov.hmcts.fees.register.api.client;


import lombok.Getter;

@Getter
public class FeesRegisterResponseException extends RuntimeException {

    private final int status;
    private final String reason;

    public FeesRegisterResponseException(int status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
