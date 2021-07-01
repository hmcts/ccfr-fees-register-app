package uk.gov.hmcts.fees.register.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

@RunWith(SpringIntegrationSerenityRunner.class)
@Ignore
public class ReportDownloadTest {

    @Test
    public void test_trial() throws Exception {

        /*try(FileInputStream fis
                = new FileInputStream((new File(System.getProperty("user.home")
                    + "/Reform/Test-Fees-Download/Fees-Download-Sample.xlsx")))) {
            //System.out.println("The File is loaded....");
            *//*Workbook workbook = new XSSFWorkbook(fis);
         *//**//*List<Name> names = (List<Name>) workbook.getAllNames();
            for (Name name : names) {
                System.out.println("The value of the name : "+name.getNameName());
            }*//**//*

            Sheet firstSheet = workbook.getSheetAt(0);*//*

            Workbook workbook = WorkbookFactory.create(new File(System.getProperty("user.home")
                + "/Reform/Test-Fees-Download/Sample CSV Download.xlsx"));

            // Retrieving the number of sheets in the Workbook
            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }*/

        final Workbook workbook = new HSSFWorkbook(new FileInputStream((new File(System.getProperty("user.home")
                + "/Reform/Test-Fees-Download/file_example_XLS_10.xls"))));
        final Sheet workSheet = workbook.getSheetAt(0);
        final Row hssfRow = workSheet.getRow(0);

        //HSSFCell hssCell = hssfRow.getCell(0);
        final Iterator<Cell> cellIterator = hssfRow.cellIterator();

        while (cellIterator.hasNext()) {
            final Cell cell = cellIterator.next();


            switch (cell.getCellType()) {
                case STRING:
                    System.out.print(cell.getStringCellValue());
                    break;
                case BOOLEAN:
                    System.out.print(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    System.out.print(cell.getNumericCellValue());
                    break;
            }
            final HSSFCell hssfCell = (HSSFCell) cell;
            final HSSFCellStyle hssfCellStyle = hssfCell.getCellStyle();
            final HSSFFont hssfFont = hssfCellStyle.getFont(workbook);
            //hssfFont.getBold();
            System.out.print(" - ");
            System.out.print("Assert the Bold : " + hssfFont.getBold());
        }

    }
}
