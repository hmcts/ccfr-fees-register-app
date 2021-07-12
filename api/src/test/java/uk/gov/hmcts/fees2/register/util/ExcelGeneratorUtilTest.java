package uk.gov.hmcts.fees2.register.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelGeneratorUtilTest {

    @Test
    public void testExportToExcelSheetDetails() throws ParseException {

        final List<Fee2Dto> reportDataList = new ArrayList<>();

        reportDataList.add(UtilityTest.buildFee2Dto());

        final Workbook actual = ExcelGeneratorUtil.exportToExcel(reportDataList);

        assertEquals("Sheet1", actual.getSheetAt(0).getSheetName());
        assertEquals(1, actual.getSheetAt(0).getLastRowNum());

        // Verify Column headers
        assertEquals("Code", actual.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void testExportToExcelCellContents() throws ParseException {

        final List<Fee2Dto> reportDataList = new ArrayList<>();

        reportDataList.add(UtilityTest.buildFee2Dto());

        final Workbook actual = ExcelGeneratorUtil.exportToExcel(reportDataList);

        // Verify Column headers
        assertEquals("Description", actual.getSheetAt(0).getRow(0).getCell(1).getStringCellValue());
        assertEquals("Amount", actual.getSheetAt(0).getRow(0).getCell(2).getStringCellValue());
        assertEquals("Statutory Instrument", actual.getSheetAt(0).getRow(0).getCell(3).getStringCellValue());
        assertEquals("SI Ref ID", actual.getSheetAt(0).getRow(0).getCell(4).getStringCellValue());
        assertEquals("Fee Order Name", actual.getSheetAt(0).getRow(0).getCell(5).getStringCellValue());
        assertEquals("Service", actual.getSheetAt(0).getRow(0).getCell(6).getStringCellValue());
        assertEquals("Jurisdiction1", actual.getSheetAt(0).getRow(0).getCell(7).getStringCellValue());
        assertEquals("Jurisdiction2", actual.getSheetAt(0).getRow(0).getCell(8).getStringCellValue());
        assertEquals("Event", actual.getSheetAt(0).getRow(0).getCell(9).getStringCellValue());
        assertEquals("Range from", actual.getSheetAt(0).getRow(0).getCell(10).getStringCellValue());
        assertEquals("Range to", actual.getSheetAt(0).getRow(0).getCell(11).getStringCellValue());
        assertEquals("Unit", actual.getSheetAt(0).getRow(0).getCell(12).getStringCellValue());
        assertEquals("Fee type", actual.getSheetAt(0).getRow(0).getCell(13).getStringCellValue());
        assertEquals("Amount Type", actual.getSheetAt(0).getRow(0).getCell(14).getStringCellValue());
        assertEquals("%", actual.getSheetAt(0).getRow(0).getCell(15).getStringCellValue());
        assertEquals("Channel", actual.getSheetAt(0).getRow(0).getCell(16).getStringCellValue());
        assertEquals("Keyword", actual.getSheetAt(0).getRow(0).getCell(17).getStringCellValue());
        assertEquals("Applicant Type", actual.getSheetAt(0).getRow(0).getCell(18).getStringCellValue());
        assertEquals("Version", actual.getSheetAt(0).getRow(0).getCell(19).getStringCellValue());
        assertEquals("Direction", actual.getSheetAt(0).getRow(0).getCell(20).getStringCellValue());
        assertEquals("Valid From", actual.getSheetAt(0).getRow(0).getCell(21).getStringCellValue());
        assertEquals("Valid To", actual.getSheetAt(0).getRow(0).getCell(22).getStringCellValue());
        assertEquals("Memo", actual.getSheetAt(0).getRow(0).getCell(23).getStringCellValue());
        assertEquals("Status", actual.getSheetAt(0).getRow(0).getCell(24).getStringCellValue());
        assertEquals("Natural Account Code", actual.getSheetAt(0).getRow(0).getCell(25).getStringCellValue());

    }


    @Test
    public void testExportToExcelDateRecords() throws ParseException {

        final List<Fee2Dto> reportDataList = new ArrayList<>();

        reportDataList.add(UtilityTest.buildFee2Dto());

        final Workbook actual = ExcelGeneratorUtil.exportToExcel(reportDataList);

        assertEquals("05 May 2021", actual.getSheetAt(0).getRow(1).getCell(21).getStringCellValue());
        assertEquals("06 June 2021", actual.getSheetAt(0).getRow(1).getCell(22).getStringCellValue());
    }

        @Test
    public void testExportToExcelRecords() throws ParseException {

        final List<Fee2Dto> reportDataList = new ArrayList<>();

        reportDataList.add(UtilityTest.buildFee2Dto());

        final Workbook actual = ExcelGeneratorUtil.exportToExcel(reportDataList);

        // Verify record values
        assertEquals("AAA", actual.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
        assertEquals("GGG", actual.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
        assertEquals("£111", actual.getSheetAt(0).getRow(1).getCell(2).getStringCellValue());
        assertEquals("OOO", actual.getSheetAt(0).getRow(1).getCell(3).getStringCellValue());
        assertEquals("NNN", actual.getSheetAt(0).getRow(1).getCell(4).getStringCellValue());
        assertEquals("III", actual.getSheetAt(0).getRow(1).getCell(5).getStringCellValue());
        assertEquals("UUU", actual.getSheetAt(0).getRow(1).getCell(6).getStringCellValue());
        assertEquals("QQQ", actual.getSheetAt(0).getRow(1).getCell(7).getStringCellValue());
        assertEquals("RRR", actual.getSheetAt(0).getRow(1).getCell(8).getStringCellValue());
        assertEquals("PPP", actual.getSheetAt(0).getRow(1).getCell(9).getStringCellValue());
        assertEquals("555", actual.getSheetAt(0).getRow(1).getCell(10).getStringCellValue());
        assertEquals("444", actual.getSheetAt(0).getRow(1).getCell(11).getStringCellValue());
        assertEquals("TTT", actual.getSheetAt(0).getRow(1).getCell(12).getStringCellValue());
        assertEquals("DDD", actual.getSheetAt(0).getRow(1).getCell(13).getStringCellValue());
        assertEquals("Flat", actual.getSheetAt(0).getRow(1).getCell(14).getStringCellValue());
        assertEquals("222", actual.getSheetAt(0).getRow(1).getCell(15).getStringCellValue());
        assertEquals("CCC", actual.getSheetAt(0).getRow(1).getCell(16).getStringCellValue());
        assertEquals("SSS", actual.getSheetAt(0).getRow(1).getCell(17).getStringCellValue());
        assertEquals("BBB", actual.getSheetAt(0).getRow(1).getCell(18).getStringCellValue());
        assertEquals(1, actual.getSheetAt(0).getRow(1).getCell(19).getNumericCellValue(), 0);
        assertEquals("HHH", actual.getSheetAt(0).getRow(1).getCell(20).getStringCellValue());
        assertEquals("JJJ", actual.getSheetAt(0).getRow(1).getCell(23).getStringCellValue());
        assertEquals("Discontinued fees", actual.getSheetAt(0).getRow(1).getCell(24).getStringCellValue());
        assertEquals("KKK", actual.getSheetAt(0).getRow(1).getCell(25).getStringCellValue());
    }

}
