package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;
import static uk.gov.hmcts.fees.register.functional.service.DownloadHelper.downloadTheReport;

@RunWith(SpringIntegrationSerenityRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class ReportDownloadTest extends IntegrationTestBase {


    @Test
    public void test_download_report_for_an_editor_creation_and_download() throws Exception {

        //First Check the Number of Records in the Excel as an Admin
        Response responseBeforeFeeCreated = downloadTheReport(userBootstrap.getAdmin());
        int rowCountBeforeNewFeeCreated = getRowCountFromDownload(responseBeforeFeeCreated.getBody().asByteArray());

        //Now Create a Fee....
        Response responseForCreateFee = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = responseForCreateFee.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Next Check the Number of Records after create in the Excel...
        Response responseAfterFeeCreated = downloadTheReport(userBootstrap.getApprover());
        int rowCountAfterNewFeeCreated = getRowCountFromDownload(responseBeforeFeeCreated.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated + 1, rowCountAfterNewFeeCreated);

        // Admin deletes the fee - success,The Editor should use the Excel as the old record count as it is....
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        Response responseAfterFeeDeleted = downloadTheReport(userBootstrap.getEditor());
        int rowCountAfterNewFeeDeleted = getRowCountFromDownload(responseAfterFeeDeleted.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated, rowCountAfterNewFeeDeleted);
    }

    private static final int getRowCountFromDownload(byte[] rawDocument) throws IOException {
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        Sheet workSheet = workbook.getSheetAt(0);
        return workSheet.getLastRowNum() - workSheet.getFirstRowNum();
    }
}
