package uk.gov.hmcts.fees.register.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.functional.config.TestConfigProperties;
import uk.gov.hmcts.fees.register.functional.idam.IdamService;
import uk.gov.hmcts.fees.register.functional.idam.models.User;

@Component
public class UserBootstrap {

    private static final String FREG_EDITOR = "freg-editor";
    private static final String FREG_APPROVER = "freg-approver";
    private static final String FREG_ADMIN = "freg-admin";

    private final IdamService idamService;
    private final TestConfigProperties testConfig;

    private User editor;
    private User approver;
    private User admin;

    @Autowired
    public UserBootstrap(IdamService userService, TestConfigProperties testConfig) {
        this.idamService = userService;
        this.testConfig = testConfig;
    }

    @PostConstruct
    public void initialize() {
        RestAssured.baseURI = testConfig.getBaseTestUrl();
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(
                ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> new ObjectMapper())
            );
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        editor = idamService.createUserWith(FREG_EDITOR);
        approver = idamService.createUserWith(FREG_APPROVER);
        admin = idamService.createUserWith(FREG_EDITOR, FREG_APPROVER, FREG_ADMIN);
    }

    public User getEditor() {
        return editor;
    }

    public User getApprover() {
        return approver;
    }

    public User getAdmin() {
        return admin;
    }

    @PreDestroy
    public void preDestroy() {
        idamService.deleteUser(editor.getEmail());
        idamService.deleteUser(approver.getEmail());
        idamService.deleteUser(admin.getEmail());
    }
}
