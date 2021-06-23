package uk.gov.hmcts.fees2.register.api.contract;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "reasonDtoWith")
public class ReasonDto {

    private String reasonForReject;
}
