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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Ignore ("As this solution has been moved to the Front End based Excel Generation")
public class ReportControllerTest extends BaseIntegrationTest {

    private static final String FILE_NAME_FORMAT = "^(attachment; filename=Fee_Register_)([0-9]{6})_([0-9]{6}).(xls)$";

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

    @Override
    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }

    @Test
    public void test_excel_streaming_positive_scenario() throws Exception {

        MvcResult mvcResult = restActions
            .get("/report/download")
            .andExpect(status().isOk())
            .andReturn();

        byte[] rawDocument = mvcResult.getResponse().getContentAsByteArray();
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(rawDocument));
        checkTheHTTPResponseHeader(mvcResult.getResponse());
        checkTheHeaderRecord(workbook, workbook.getSheetAt(0));
    }

    @Test
    public void test_excel_streaming_positive_404_scenario() throws Exception {

        final MvcResult mvcResult = restActions
            .get("/repor/download")
            .andExpect(status().isNotFound())
            .andReturn();
    }

    private void checkTheHTTPResponseHeader(final MockHttpServletResponse response) {
        assertEquals("application/vnd.ms-excel", response.getContentType());
        assertTrue(response.containsHeader("Content-Disposition"));
        assertTrue(Pattern.compile(
            FILE_NAME_FORMAT,
            Pattern.CASE_INSENSITIVE
        ).matcher(response.getHeader("Content-Disposition")).matches());
    }

    private void checkTheHeaderRecord(final Workbook workbook, final Sheet workSheet) {

        //Check that the Header
        final Row hssfRow = workSheet.getRow(0);
        final Iterator<Cell> cellIterator = hssfRow.cellIterator();
        int counter = 0;
        while (cellIterator.hasNext()) {
            final Cell cell = cellIterator.next();
            final HSSFCell hssfCell = (HSSFCell) cell;
            final HSSFCellStyle hssfCellStyle = hssfCell.getCellStyle();
            final HSSFFont hssfFont = hssfCellStyle.getFont(workbook);
            assertTrue(hssfFont.getBold());
            assertEquals(this.headerValues.get(counter), cell.getStringCellValue());
            counter++;
        }
    }
}
