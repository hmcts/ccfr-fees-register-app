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
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class ReportControllerTest extends BaseIntegrationTest {

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }


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

    @Test
    public void test_excel_streaming_positive_scenario() throws Exception {

        MvcResult mvcResult = restActions
            .get("/report/download")
            .andExpect(status().isOk())
            .andReturn();

        byte[] rawDocument = mvcResult.getResponse().getContentAsByteArray();
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        checkTheHeaderRecord(workbook, workbook.getSheetAt(0));
        checkTheDataTypeOfTheRecords(workbook, workbook.getSheetAt(0));
        checkTheDataDateTypeFormatOfTheRecords(workbook, workbook.getSheetAt(0));

    }

    private void checkTheHeaderRecord(Workbook workbook, Sheet workSheet) {

        //Check that the Header
        Row hssfRow = workSheet.getRow(0);
        Iterator<Cell> cellIterator = hssfRow.cellIterator();
        int counter = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
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
            HSSFCell hssfCell = (HSSFCell) cell;
            HSSFCellStyle hssfCellStyle = hssfCell.getCellStyle();
            HSSFFont hssfFont = hssfCellStyle.getFont(workbook);
            assertTrue(hssfFont.getBold());
            assertEquals(this.headerValues.get(counter), cell.getStringCellValue());
            counter++;
        }
    }

    private void checkTheDataTypeOfTheRecords(Workbook workbook, Sheet workSheet) throws Exception {
        int firstRowNum = workSheet.getFirstRowNum();
        int lastRowNumber = workSheet.getLastRowNum();

        for (int i = firstRowNum; i <= lastRowNumber; i++) {
            //Check that the Header
            Row hssfRow = workSheet.getRow(i);
            Iterator<Cell> cellIterator = hssfRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        break;
                    default:
                        throw new IllegalStateException("Invalid Data Format in the Excel Sheets...");
                }
            }
        }
    }

    private void checkTheDataDateTypeFormatOfTheRecords(Workbook workbook, Sheet workSheet) {
        int firstRowNum = workSheet.getFirstRowNum();
        int lastRowNumber = workSheet.getLastRowNum();

        for (int i = firstRowNum; i <= lastRowNumber; i++) {
            //Check that the Header
            Row hssfRow = workSheet.getRow(i);
            Cell cell= hssfRow.getCell(21);
            System.out.println("The value of the cell : "+cell.getStringCellValue());
            //TODO - Check the Date Data Type of the Date........

        }
    }
}
