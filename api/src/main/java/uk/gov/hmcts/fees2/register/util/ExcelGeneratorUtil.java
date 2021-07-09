package uk.gov.hmcts.fees2.register.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.controllers.exceptions.FeesException;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.IndexedColors.BLACK;

public final class ExcelGeneratorUtil {

    private ExcelGeneratorUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String FLAT = "Flat";
    private static final String VOLUME = "Volume";
    private static final String PERCENTAGE = "Percentage";
    private static final String APPROVED_BUT_NOT_LIVE_FEES = "Approved but not live fees";
    private static final String DISCONTINUED_FEES = "Discontinued fees";
    private static final String LIVE_FEES = "Live Fees";

    public static Workbook exportToExcel(final List<Fee2Dto> fee2DtoList) {
        final String[] cols =
                {"Code", "Description", "Amount", "Statutory Instrument", "SI Ref ID", "Fee Order Name", "Service", "Jurisdiction1", "Jurisdiction2", "Event", "Range from", "Range to", "Unit", "Fee type", "Amount Type", "%", "Channel", "Keyword", "Applicant Type", "Version", "Direction",
                        "Valid From", "Valid To", "Memo", "Status", "Natural Account Code"};

        try (final Workbook workbook = new HSSFWorkbook()) {
            final CreationHelper createHelper = workbook.getCreationHelper();

            final Sheet sheet = workbook.createSheet("Sheet1");

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

        final DateFormat df = new SimpleDateFormat("dd MMMM yyyy");

        for (int col = 0; col < cols.length; col++) {
            final Cell cell = headerRow.createCell(col);
            cell.setCellValue(cols[col]);
            cell.setCellStyle(headerCellStyle);
        }

        for (final Fee2Dto fee2Dto : fee2DtoList) {

            if (null != fee2Dto) {

                final Row row = sheet.createRow(rowIdx++);

                final List<FeeVersionDto> feeVersionDtoList = fee2Dto.getFeeVersionDtos();

                for (final FeeVersionDto feeVersionDto : feeVersionDtoList) {

                    if (null != feeVersionDto &&
                            FeeVersionStatus.approved.name().equals(feeVersionDto.getStatus().name())) {

                        row.createCell(0).setCellValue(fee2Dto.getCode());
                        row.createCell(1).setCellValue(feeVersionDto.getDescription());
                        row.createCell(2).setCellValue(
                                null != feeVersionDto.getAmount() ? "£" + feeVersionDto.getAmount().toString() : "");
                        row.createCell(3).setCellValue(null != feeVersionDto.getStatutoryInstrument() ? feeVersionDto
                                .getStatutoryInstrument() : "");
                        row.createCell(4)
                                .setCellValue(null != feeVersionDto.getSiRefId() ? feeVersionDto.getSiRefId() : "");
                        row.createCell(5).setCellValue(
                                null != feeVersionDto.getFeeOrderName() ? feeVersionDto.getFeeOrderName() : "");
                        row.createCell(6).setCellValue(fee2Dto.getServiceTypeDto().getName());
                        row.createCell(7).setCellValue(fee2Dto.getJurisdiction1Dto().getName());
                        row.createCell(8).setCellValue(fee2Dto.getJurisdiction2Dto().getName());
                        row.createCell(9).setCellValue(
                                null != fee2Dto.getEventTypeDto() ? fee2Dto.getEventTypeDto().getName() : "");
                        row.createCell(10)
                                .setCellValue(null != fee2Dto.getMinRange() ? fee2Dto.getMinRange().toString() : "");
                        row.createCell(11)
                                .setCellValue(null != fee2Dto.getMaxRange() ? fee2Dto.getMaxRange().toString() : "");
                        row.createCell(12).setCellValue(fee2Dto.getRangeUnit());
                        row.createCell(13).setCellValue(fee2Dto.getFeeType());
                        row.createCell(14).setCellValue(getAmountType(feeVersionDto));
                        row.createCell(15).setCellValue(
                                null != feeVersionDto.getPercentageAmount() ? feeVersionDto.getPercentageAmount()
                                        .toString() : "");
                        row.createCell(16)
                                .setCellValue(null != fee2Dto.getChannelTypeDto() ? fee2Dto.getChannelTypeDto()
                                        .getName() : "");
                        row.createCell(17).setCellValue(null != fee2Dto.getKeyword() ? fee2Dto.getKeyword() : "");
                        row.createCell(18)
                                .setCellValue(
                                        null != fee2Dto.getApplicantTypeDto() ? fee2Dto.getApplicantTypeDto()
                                                .getName() : "");
                        row.createCell(19).setCellValue(feeVersionDto.getVersion());
                        row.createCell(20)
                                .setCellValue(null != feeVersionDto.getDirection() ? feeVersionDto.getDirection() : "");
                        row.createCell(21).setCellValue(
                                (null != feeVersionDto.getValidFrom()) ? df.format(feeVersionDto.getValidFrom()) : "");
                        row.createCell(22).setCellValue(
                                (null != feeVersionDto.getValidTo()) ? df.format(feeVersionDto.getValidTo()) : "");
                        row.createCell(23).setCellValue(feeVersionDto.getMemoLine());
                        row.createCell(24).setCellValue(getStatus(feeVersionDto));
                        row.createCell(25).setCellValue(feeVersionDto.getNaturalAccountCode());
                    }
                }
            }
        }
    }

    private static String getAmountType(final FeeVersionDto feeVersionDto) {
        if (null != feeVersionDto.getFlatAmount()) {
            return FLAT;
        } else if (null != feeVersionDto.getVolumeAmount()) {
            return VOLUME;
        } else if (null != feeVersionDto.getPercentageAmount()) {
            return PERCENTAGE;
        }
        return "";
    }

    private static String getStatus(final FeeVersionDto feeVersionDto) {
        if (null != feeVersionDto.getValidFrom() && feeVersionDto.getValidFrom().after(new Date())) {
            return APPROVED_BUT_NOT_LIVE_FEES;
        } else if (null != feeVersionDto.getValidTo() && feeVersionDto.getValidTo().before(new Date())) {
            return DISCONTINUED_FEES;
        } else {
            return LIVE_FEES;
        }
    }

}
