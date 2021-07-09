package uk.gov.hmcts.fees2.register.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(ExcelGeneratorUtil.class);
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

        final DateFormat df = new SimpleDateFormat("dd MMMM yyyy");

        for (int col = 0; col < cols.length; col++) {
            final Cell cell = headerRow.createCell(col);
            cell.setCellValue(cols[col]);
            cell.setCellStyle(headerCellStyle);
        }

//        for (final Fee2Dto fee2Dto : fee2DtoList) {

            LOG.info("1");
            if (null !=  fee2DtoList.get(0)) {

                final Row row = sheet.createRow(rowIdx++);

                final List<FeeVersionDto> feeVersionDtoList =  fee2DtoList.get(0).getFeeVersionDtos();

//                for (final FeeVersionDto feeVersionDto : feeVersionDtoList) {

                    LOG.info("2");
                    if (null != feeVersionDtoList.get(0) &&
                            FeeVersionStatus.approved.name().equals(feeVersionDtoList.get(0).getStatus().name())) {

                        LOG.info("3");
                        row.createCell(0).setCellValue( fee2DtoList.get(0).getCode());
                        row.createCell(1).setCellValue(feeVersionDtoList.get(0).getDescription());
                        row.createCell(2).setCellValue(
                                null != feeVersionDtoList.get(0).getAmount() ? "£" + feeVersionDtoList.get(0).getAmount().toString() : "");
                        row.createCell(3).setCellValue(null != feeVersionDtoList.get(0).getStatutoryInstrument() ? feeVersionDtoList.get(0)
                                .getStatutoryInstrument() : "");
                        row.createCell(4)
                                .setCellValue(null != feeVersionDtoList.get(0).getSiRefId() ? feeVersionDtoList.get(0).getSiRefId() : "");
                        row.createCell(5).setCellValue(
                                null != feeVersionDtoList.get(0).getFeeOrderName() ? feeVersionDtoList.get(0).getFeeOrderName() : "");
                        LOG.info("4");
                        row.createCell(6).setCellValue( fee2DtoList.get(0).getServiceTypeDto().getName());
                        row.createCell(7).setCellValue( fee2DtoList.get(0).getJurisdiction1Dto().getName());
                        row.createCell(8).setCellValue( fee2DtoList.get(0).getJurisdiction2Dto().getName());
                        row.createCell(9).setCellValue(
                                null !=  fee2DtoList.get(0).getEventTypeDto() ?  fee2DtoList.get(0).getEventTypeDto().getName() : "");
                        row.createCell(10)
                                .setCellValue(null !=  fee2DtoList.get(0).getMinRange() ?  fee2DtoList.get(0).getMinRange().toString() : "");
                        LOG.info("5");
                        row.createCell(11)
                                .setCellValue(null !=  fee2DtoList.get(0).getMaxRange() ?  fee2DtoList.get(0).getMaxRange().toString() : "");
                        row.createCell(12).setCellValue( fee2DtoList.get(0).getRangeUnit());
                        row.createCell(13).setCellValue( fee2DtoList.get(0).getFeeType());
                        row.createCell(14).setCellValue(getAmountType(feeVersionDtoList.get(0)));
                        row.createCell(15).setCellValue(
                                null != feeVersionDtoList.get(0).getPercentageAmount() ? feeVersionDtoList.get(0).getPercentageAmount()
                                        .toString() : "");
                        LOG.info("6");
                        row.createCell(16)
                                .setCellValue(null !=  fee2DtoList.get(0).getChannelTypeDto() ?  fee2DtoList.get(0).getChannelTypeDto()
                                        .getName() : "");
                        row.createCell(17).setCellValue(null !=  fee2DtoList.get(0).getKeyword() ?  fee2DtoList.get(0).getKeyword() : "");
                        row.createCell(18)
                                .setCellValue(
                                        null !=  fee2DtoList.get(0).getApplicantTypeDto() ?  fee2DtoList.get(0).getApplicantTypeDto()
                                                .getName() : "");
                        row.createCell(19).setCellValue(feeVersionDtoList.get(0).getVersion());
                        row.createCell(20)
                                .setCellValue(null != feeVersionDtoList.get(0).getDirection() ? feeVersionDtoList.get(0).getDirection() : "");
                        LOG.info("7");
                        row.createCell(21).setCellValue(
                                (null != feeVersionDtoList.get(0).getValidFrom()) ? df.format(feeVersionDtoList.get(0).getValidFrom()) : "");
                        row.createCell(22).setCellValue(
                                (null != feeVersionDtoList.get(0).getValidTo()) ? df.format(feeVersionDtoList.get(0).getValidTo()) : "");
                        row.createCell(23).setCellValue(feeVersionDtoList.get(0).getMemoLine());
                        row.createCell(24).setCellValue(getStatus(feeVersionDtoList.get(0)));
                        row.createCell(25).setCellValue(feeVersionDtoList.get(0).getNaturalAccountCode());
                        LOG.info("8");
                    }
                }
//            }
//        }
        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
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
