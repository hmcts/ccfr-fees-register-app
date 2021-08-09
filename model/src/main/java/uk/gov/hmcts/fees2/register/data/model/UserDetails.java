package uk.gov.hmcts.fees2.register.data.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class UserDetails {

    private String sub;

    private String uid;

    private String[] roles;

    private String name;

    private String given_name;

    private String family_name;

}
