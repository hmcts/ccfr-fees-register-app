package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class IdamUserInfo {

    private String id;

    private String forename;

    private String surname;

    private String email;

    private boolean active;

    private boolean stale;

    private List<String> roles;

    private String lastModified;

    private String createDate;

}
