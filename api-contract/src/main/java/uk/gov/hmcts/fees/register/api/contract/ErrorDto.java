package uk.gov.hmcts.fees.register.api.contract;

import lombok.Data;

@Data
public class ErrorDto {
    private final String message;
}
