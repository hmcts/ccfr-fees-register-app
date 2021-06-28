package uk.gov.hmcts.fees2.register.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.controllers.exceptions.FeesException;

import java.util.List;

import static org.apache.poi.ss.usermodel.IndexedColors.BLACK;

public final class ExcelGeneratorUtil {

    public static Workbook exportToExcel(final List<Fee2Dto> fee2DtoList) {
        final String[] cols =
                {"Code", "Description", "Amount", "Statutory Instrument", "SI Ref ID", "Fee Order Name", "Service", "Jurisdiction1", "Jurisdiction2", "Event", "Range from", "Range to", "Unit", "Fee type", "Amount Type", "%", "Channel", "Keyword", "Applicant Type", "Version", "Direction",
                        "Valid From", "Valid To", "Memo", "Status", "Natural Account Code"};

        try (final Workbook workbook = new HSSFWorkbook()) {
            final CreationHelper createHelper = workbook.getCreationHelper();

            final Sheet sheet = workbook.createSheet();

            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(BLACK.getIndex());

            final CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            final Row headerRow = sheet.createRow(0);

            // Header
            buildReport(fee2DtoList, cols, sheet, headerCellStyle, headerRow);

            // CellStyle for Age
            final CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            return workbook;
        } catch (final Exception ex) {
            throw new FeesException(ex);
        }
    }

    private static void buildReport(final List<Fee2Dto> fee2DtoList, final String[] cols,
                                    final Sheet sheet,
                                    final CellStyle headerCellStyle, final Row headerRow) {

        int rowIdx = 1;

        for (int col = 0; col < cols.length; col++) {
            final Cell cell = headerRow.createCell(col);
            cell.setCellValue(cols[col]);
            cell.setCellStyle(headerCellStyle);
        }

        for (final Fee2Dto fee2Dto : fee2DtoList) {

            final Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(fee2Dto.getCode());

            final List<FeeVersionDto> feeVersionDtoList = fee2Dto.getFeeVersionDtos();

            for (final FeeVersionDto feeVersionDto : feeVersionDtoList) {

                row.createCell(1).setCellValue(feeVersionDto.getDescription());
                row.createCell(2).setCellValue(feeVersionDto.getAmount().toString());
                row.createCell(3).setCellValue(feeVersionDto.getStatutoryInstrument());
                row.createCell(4).setCellValue(feeVersionDto.getSiRefId());
                row.createCell(5).setCellValue(feeVersionDto.getFeeOrderName());
                row.createCell(14).setCellValue(null != feeVersionDto.getFlatAmount() ? feeVersionDto.getFlatAmount()
                        .toString() : ""); //AmountType??
                row.createCell(15).setCellValue(
                        null != feeVersionDto.getPercentageAmount() ? feeVersionDto.getPercentageAmount()
                                .toString() : "");
                row.createCell(19).setCellValue(feeVersionDto.getVersion());
                row.createCell(20).setCellValue(feeVersionDto.getDirection());
                row.createCell(21).setCellValue(feeVersionDto.getValidFrom());
                row.createCell(22).setCellValue(feeVersionDto.getValidTo());
                row.createCell(23).setCellValue(feeVersionDto.getMemoLine());
                row.createCell(24).setCellValue(feeVersionDto.getStatutoryInstrument());  //status??
                row.createCell(25).setCellValue(feeVersionDto.getNaturalAccountCode());
            }
            row.createCell(6).setCellValue(fee2Dto.getServiceTypeDto().getName());
            row.createCell(7).setCellValue(fee2Dto.getJurisdiction1Dto().getName());
            row.createCell(8).setCellValue(fee2Dto.getJurisdiction2Dto().getName());
            row.createCell(9).setCellValue(fee2Dto.getEventTypeDto().getName());
            row.createCell(10).setCellValue(null != fee2Dto.getMinRange() ? fee2Dto.getMinRange().toString() : "");
            row.createCell(11).setCellValue(null != fee2Dto.getMaxRange() ? fee2Dto.getMaxRange().toString() : "");
            row.createCell(12).setCellValue(fee2Dto.getRangeUnit());
            row.createCell(13).setCellValue(fee2Dto.getFeeType());
            row.createCell(16)
                    .setCellValue(null != fee2Dto.getChannelTypeDto() ? fee2Dto.getChannelTypeDto().getName() : "");
            row.createCell(17).setCellValue(fee2Dto.getKeyword());
            row.createCell(18)
                    .setCellValue(null != fee2Dto.getApplicantTypeDto() ? fee2Dto.getApplicantTypeDto().getName() : "");
        }
        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
