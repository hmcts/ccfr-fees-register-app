package uk.gov.hmcts.fees2.register.api.controllers;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class ReportControllerTest extends BaseIntegrationTest {

    private static final String CURRENCY_FORMAT = "^(£)([0-9]*).([0-9]{2})$";

    private final List<String> headerValues = Collections
            .unmodifiableList(Arrays.asList("Code",
                    "Description",
                    "Amount",
                    "Statutory Instrument",
                    "SI Ref ID",
                    "Fee Order Name",
                    "Service",
                    "Jurisdiction1",
                    "Jurisdiction2",
                    "Event",
                    "Range from",
                    "Range to",
                    "Unit",
                    "Fee type",
                    "Amount Type",
                    "%",
                    "Channel",
                    "Keyword",
                    "Applicant Type",
                    "Version",
                    "Direction",
                    "Valid From",
                    "Valid To",
                    "Memo",
                    "Status",
                    "Natural Account Code"));

    private static final boolean checkDateFormat(final String dateInput) throws Exception {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        final LocalDate dateTime = LocalDate.parse(dateInput, formatter);
        return true;
    }

    private static final boolean checkCurrencyFormat(final String currencyInput) throws Exception {
        final Matcher currencyFormatMatcher
                = Pattern.compile(
                CURRENCY_FORMAT,
                Pattern.CASE_INSENSITIVE
        ).matcher(currencyInput);
        return currencyFormatMatcher.matches();
    }

    @Override
    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }

    @Test
    public void test_excel_streaming_positive_404_scenario() throws Exception {

        final MvcResult mvcResult = restActions
                .get("/repor/download")
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void test_excel_streaming_positive_scenario() throws Exception {

        final MvcResult mvcResult = restActions
                .get("/report/download")
                .andExpect(status().isOk())
                .andReturn();

        final byte[] rawDocument = mvcResult.getResponse().getContentAsByteArray();
        final Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        checkTheHTTPResponseHeader(mvcResult.getResponse());
        checkTheHeaderRecord(workbook, workbook.getSheetAt(0));
        checkTheDataTypeOfTheRecords(workbook, workbook.getSheetAt(0));
        checkTheDataDateTypeFormatOfTheRecords(workbook, workbook.getSheetAt(0));
    }

    private void checkTheHTTPResponseHeader(final MockHttpServletResponse response) {
        assertEquals("application/vnd.ms-excel", response.getContentType());
        assertTrue(response.containsHeader("Content-Disposition"));
        assertTrue(response.getHeader("Content-Disposition").startsWith("attachment; filename=Fee_Register_"));
    }

    private void checkTheHeaderRecord(final Workbook workbook, final Sheet workSheet) {

        //Check that the Header
        final Row hssfRow = workSheet.getRow(0);
        final Iterator<Cell> cellIterator = hssfRow.cellIterator();
        int counter = 0;
        while (cellIterator.hasNext()) {
            final Cell cell = cellIterator.next();
            /*switch (cell.getCellType()) {
                case STRING:
                    System.out.print(cell.getStringCellValue());
                    break;
                case BOOLEAN:
                    System.out.print(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    System.out.print(cell.getNumericCellValue());
                    break;
            }*/
            final HSSFCell hssfCell = (HSSFCell) cell;
            final HSSFCellStyle hssfCellStyle = hssfCell.getCellStyle();
            final HSSFFont hssfFont = hssfCellStyle.getFont(workbook);
            assertTrue(hssfFont.getBold());
            assertEquals(this.headerValues.get(counter), cell.getStringCellValue());
            counter++;
        }
    }

    private void checkTheDataTypeOfTheRecords(final Workbook workbook, final Sheet workSheet) throws Exception {
        final int firstRowNum = workSheet.getFirstRowNum();
        final int lastRowNumber = workSheet.getLastRowNum();

        for (int i = firstRowNum; i <= lastRowNumber; i++) {
            //Check that the Header
            final Row hssfRow = workSheet.getRow(i);
            final Iterator<Cell> cellIterator = hssfRow.cellIterator();
            while (cellIterator.hasNext()) {
                final Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        break;
                    default:
                        throw new IllegalStateException("Invalid Data Format in the Excel Sheets...");
                }
            }
        }
    }

    private void checkTheDataDateTypeFormatOfTheRecords(final Workbook workbook, final Sheet workSheet) {

        final int firstRowNum = workSheet.getFirstRowNum();
        final int lastRowNumber = workSheet.getLastRowNum();

        for (int i = firstRowNum; i <= lastRowNumber; i++) {
            //Check that the Header
            final Row hssfRow = workSheet.getRow(i);
            final Cell cell = hssfRow.getCell(21);
            System.out.println("The value of the cell : " + cell.getStringCellValue());
            //TODO - Check the Date Data Type of the Date........
        }
    }

    public static class HelperLogicTests {

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




