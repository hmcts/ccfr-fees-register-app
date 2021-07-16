package uk.gov.hmcts.fees.register.functional.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.functional.idam.models.User;

@Component
public class DownloadHelper {

    public static Response downloadTheReport(User user) {
        return RestAssured.given()
            .header(HttpHeaders.AUTHORIZATION, user.getAuthorisationToken())
            .when()
            .get("/report/download");
    }
}
