package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;
import static uk.gov.hmcts.fees.register.functional.service.DownloadHelper.downloadTheReport;
import static uk.gov.hmcts.fees.register.functional.service.FeeService.createAFee;
import static uk.gov.hmcts.fees.register.functional.service.FeeService.getAFee;
import static uk.gov.hmcts.fees.register.functional.service.FeeService.getLatestFeeVersion;

@RunWith(SpringIntegrationSerenityRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class ReportDownloadTest extends IntegrationTestBase {

    private static final String CURRENCY_FORMAT = "^(£)([0-9]*).([0-9]{2})$";

    @Test
    @Ignore("Ignoring this test as the Design is still being revised and formalised ")
    public void test_download_report_for_data_formats() throws Exception {
        Response response = createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        //Ammending only the Amount
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //First Check the Number of Records in the Excel as an Admin
        Response responseBeforeFeeCreated = downloadTheReport(userBootstrap.getAdmin());
        byte[] rawDocument = responseBeforeFeeCreated.getBody().asByteArray();
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        checkTheDataTypeOfTheRecords(workbook, workbook.getSheetAt(0));
        checkTheDataDateAndCurrencyTypeFormatOfTheRecords(workbook, workbook.getSheetAt(0));

        // Admin deletes the fee - success,The Editor should use the Excel as the old record count as it is....
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void test_negative_scenario_with_draft_fees_not_in_report() throws Exception {

        //First Check the Number of Records in the Excel as an Admin
        Response responseBeforeFeeCreated = downloadTheReport(userBootstrap.getEditor());
        int rowCountBeforeNewFeeCreated = getRowCountFromDownload(responseBeforeFeeCreated.getBody().asByteArray());

        //Now Create a Fee....
        Response responseForCreateFee = createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = responseForCreateFee.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();
        System.out.println("The value of Created FEE Code : " + feeCode);

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        //Next Check the Number of Records after create in the Excel...
        Response responseAfterFeeCreated = downloadTheReport(userBootstrap.getApprover());
        int rowCountAfterNewFeeCreated = getRowCountFromDownload(responseAfterFeeCreated.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated, rowCountAfterNewFeeCreated);

        // Admin deletes the fee - success,The Editor should use the Excel as the old record count as it is....
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        Response responseAfterFeeDeleted = downloadTheReport(userBootstrap.getEditor());
        int rowCountAfterNewFeeDeleted = getRowCountFromDownload(responseAfterFeeDeleted.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated, rowCountAfterNewFeeDeleted);
    }


    @Test
    public void test_positive_scenario_with_submitted_fees_in_report() throws Exception {

        //First Check the Number of Records in the Excel as an Admin
        Response responseBeforeFeeCreated = downloadTheReport(userBootstrap.getEditor());
        int rowCountBeforeNewFeeCreated = getRowCountFromDownload(responseBeforeFeeCreated.getBody().asByteArray());

        //Now Create a Fee....
        Response responseForCreateFee = createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = responseForCreateFee.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();
        System.out.println("The value of Created FEE Code : " + feeCode);

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        //Getting the latest version
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Next Check the Number of Records after create in the Excel...
        Response responseAfterFeeCreated = downloadTheReport(userBootstrap.getApprover());
        int rowCountAfterNewFeeCreated = getRowCountFromDownload(responseAfterFeeCreated.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated + 1, rowCountAfterNewFeeCreated);

        // Admin deletes the fee - success,The Editor should use the Excel as the old record count as it is....
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        Response responseAfterFeeDeleted = downloadTheReport(userBootstrap.getEditor());
        int rowCountAfterNewFeeDeleted = getRowCountFromDownload(responseAfterFeeDeleted.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated, rowCountAfterNewFeeDeleted);
    }


    @Test
    public void test_positive_scenario_with_approved_fees_in_report() throws Exception {

        //First Check the Number of Records in the Excel as an Admin
        Response responseBeforeFeeCreated = downloadTheReport(userBootstrap.getEditor());
        int rowCountBeforeNewFeeCreated = getRowCountFromDownload(responseBeforeFeeCreated.getBody().asByteArray());

        //Now Create a Fee....
        Response responseForCreateFee = createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = responseForCreateFee.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();
        System.out.println("The value of Created FEE Code : " + feeCode);

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        //Ammending only the Amount
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Next Check the Number of Records after create in the Excel...
        Response responseAfterFeeCreated = downloadTheReport(userBootstrap.getApprover());
        int rowCountAfterNewFeeCreated = getRowCountFromDownload(responseAfterFeeCreated.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated + 1, rowCountAfterNewFeeCreated);

        // Admin deletes the fee - success,The Editor should use the Excel as the old record count as it is....
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        Response responseAfterFeeDeleted = downloadTheReport(userBootstrap.getEditor());
        int rowCountAfterNewFeeDeleted = getRowCountFromDownload(responseAfterFeeDeleted.getBody().asByteArray());
        assertEquals(rowCountBeforeNewFeeCreated, rowCountAfterNewFeeDeleted);
    }

    private void checkTheDataTypeOfTheRecords(final Workbook workbook, final Sheet workSheet) throws Exception {

        final int firstRowNum = workSheet.getFirstRowNum();
        final int lastRowNumber = workSheet.getLastRowNum();
        System.out.println("The Row Count for the Excel Spreadhseet : " + (lastRowNumber - firstRowNum));

        for (int i = firstRowNum; i <= lastRowNumber; i++) {
            //Check that the Header
            final Row hssfRow = workSheet.getRow(i);
            final Iterator<Cell> cellIterator = hssfRow.cellIterator();
            int cellCount = 0;
            while (cellIterator.hasNext()) {
                final Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        break;
                    case NUMERIC:
                        break;
                    case BLANK:
                        break;
                    default:
                        System.out.println("This is the problematic Cell Type : " + cell.getCellType());
                        System.out.println("This is the problematic Row Num : " + i);
                        System.out.println("This is the problematic CellCount : " + cellCount++);
                        throw new IllegalStateException("Invalid Data Format in the Excel Sheets...");
                }
            }
        }
    }

    private static final int getRowCountFromDownload(byte[] rawDocument) throws IOException {
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        Sheet workSheet = workbook.getSheetAt(0);
        return workSheet.getLastRowNum() - workSheet.getFirstRowNum();
    }

    private void checkTheDataDateAndCurrencyTypeFormatOfTheRecords(final Workbook workbook, final Sheet workSheet)
        throws Exception {

        final int firstRowNum = workSheet.getFirstRowNum();
        final int lastRowNumber = workSheet.getLastRowNum();

        int cellCount = 0;
        for (int i = firstRowNum + 1; i <= lastRowNumber; i++) {
            //Check that the Header
            final Row hssfRow = workSheet.getRow(i);
            final Cell amountCell = hssfRow.getCell(2);
            System.out.println("The value of the cell Object : " + amountCell);
            System.out.println("The value of the cell Type : " + amountCell.getCellType());
            System.out.println("The value of the cell : " + amountCell.getStringCellValue());
            if (!checkCurrencyFormat(amountCell.getStringCellValue())) {
                System.out.println("This is the problematic Row Num : " + i);
                throw new IllegalStateException("Data Format not of Currency Type");
            }
            final Cell validFromCell = hssfRow.getCell(21);
            System.out.println("The value of the Valid From Cell : " + validFromCell.getStringCellValue());
            if (!checkDateFormat(validFromCell.getStringCellValue())) {
                System.out.println("This is the problematic Row Num : " + i);
                System.out.println("This is the problematic CellCount : " + cellCount);
                throw new IllegalStateException("Date Format not of Date Type");
            }
            final Cell validToCell = hssfRow.getCell(22);
            System.out.println("The value of the Valid To Cell : " + validToCell.getStringCellValue());
            if (!checkDateFormat(validToCell.getStringCellValue())) {
                System.out.println("This is the problematic Row Num : " + i);
                System.out.println("This is the problematic CellCount : " + cellCount);
                throw new IllegalStateException("Date Format not of Date Type");
            }
            cellCount++;
        }
    }

    private static final boolean checkCurrencyFormat(final String currencyInput) throws Exception {
        final Matcher currencyFormatMatcher
            = Pattern.compile(
            CURRENCY_FORMAT,
            Pattern.CASE_INSENSITIVE
        ).matcher(currencyInput);
        return currencyFormatMatcher.matches();
    }

    private static final boolean checkDateFormat(final String dateInput) throws Exception {
        if (Objects.isNull(dateInput) || dateInput.trim().equals("")) {
            return true;
        }
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        final LocalDate dateTime = LocalDate.parse(dateInput, formatter);
        return true;
    }

    public static class ReportDownloadLogicTests {

        @Test
        public void test_check_date_valid_format() throws Exception {
            assertTrue(checkDateFormat("20 November 1945"));
            assertTrue(checkDateFormat("01 May 1945"));
        }

        @Test(expected = DateTimeParseException.class)
        public void test_check_date_invalid_format_1() throws Exception {
            assertTrue(checkDateFormat("45 January 1945"));
        }

        @Test(expected = DateTimeParseException.class)
        public void test_check_date_invalid_format_2() throws Exception {
            assertTrue(checkDateFormat("1 May 1945"));
        }

        @Test(expected = DateTimeParseException.class)
        public void test_check_date_invalid_format_3() throws Exception {
            assertTrue(checkDateFormat("30 Jul 1945"));
        }

        @Test
        public void test_check_currency_valid_format() throws Exception {
            assertTrue(checkCurrencyFormat("£100.01"));
            assertTrue(checkCurrencyFormat("£50000000023.01"));
        }
    }
}
