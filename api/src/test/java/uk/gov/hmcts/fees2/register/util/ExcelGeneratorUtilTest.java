package uk.gov.hmcts.fees2.register.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelGeneratorUtilTest {

    @Test
    public void testExportToExcel() throws ParseException {

        final List<Fee2Dto> reportDataList = new ArrayList<>();

        reportDataList.add(buildFee2Dto());

        final Workbook actual = ExcelGeneratorUtil.exportToExcel(reportDataList);

//        Assert.assertTrue(actual.getFontAt(0).getBold());
//        assertEquals(BLACK.getIndex(), actual.getFontAt(0).getColor());

        assertEquals("Sheet0", actual.getSheetAt(0).getSheetName());
        assertEquals(1, actual.getSheetAt(0).getLastRowNum());

        // Verify Column headers
        assertEquals("Code", actual.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
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

        // Verify record values
        assertEquals("AAA", actual.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
        assertEquals("GGG", actual.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
        assertEquals("111", actual.getSheetAt(0).getRow(1).getCell(2).getStringCellValue());
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
//        assertEquals("FlatAmountDto(amount=111)", actual.getSheetAt(0).getRow(1).getCell(14).getStringCellValue());
//        assertEquals("PercentageAmountDto(percentage=222)",
//                actual.getSheetAt(0).getRow(1).getCell(15).getStringCellValue());
        assertEquals("CCC", actual.getSheetAt(0).getRow(1).getCell(16).getStringCellValue());
        assertEquals("SSS", actual.getSheetAt(0).getRow(1).getCell(17).getStringCellValue());
        assertEquals("BBB", actual.getSheetAt(0).getRow(1).getCell(18).getStringCellValue());
        assertEquals(1, actual.getSheetAt(0).getRow(1).getCell(19).getNumericCellValue(), 0);
        assertEquals("HHH", actual.getSheetAt(0).getRow(1).getCell(20).getStringCellValue());
//        assertEquals("05-05-2021", actual.getSheetAt(0).getRow(1).getCell(21).getDateCellValue());
//        assertEquals("06-06-2021", actual.getSheetAt(0).getRow(1).getCell(22).getDateCellValue());
        assertEquals("JJJ", actual.getSheetAt(0).getRow(1).getCell(23).getStringCellValue());
//        assertEquals("draft", actual.getSheetAt(0).getRow(1).getCell(24).getStringCellValue());
        assertEquals("KKK", actual.getSheetAt(0).getRow(1).getCell(25).getStringCellValue());
    }

    private FeeVersionDto buildFeeVersionDto() throws ParseException {
        final FeeVersionDto feeVersionDto = new FeeVersionDto();
        feeVersionDto.setVersion(1);
        feeVersionDto.setApprovedBy("EEE");
        feeVersionDto.setAuthor("FFF");
        feeVersionDto.setDescription("GGG");
        feeVersionDto.setDirection("HHH");
        feeVersionDto.setFeeOrderName("III");

        final FlatAmountDto flatAmountDto = new FlatAmountDto();
        flatAmountDto.setAmount(new BigDecimal("111"));
        feeVersionDto.setFlatAmount(flatAmountDto);

        feeVersionDto.setMemoLine("JJJ");
        feeVersionDto.setNaturalAccountCode("KKK");

        final PercentageAmountDto percentageAmountDto = new PercentageAmountDto();
        percentageAmountDto.setPercentage(new BigDecimal("222"));
        feeVersionDto.setPercentageAmount(percentageAmountDto);

        feeVersionDto.setReasonForReject("LLL");
        feeVersionDto.setReasonForUpdate("MMM");
        feeVersionDto.setSiRefId("NNN");

        feeVersionDto.setStatus(FeeVersionStatusDto.draft);
        feeVersionDto.setStatutoryInstrument("OOO");
//        feeVersionDto.setValidFrom(new SimpleDateFormat("dd/MM/yyyy").format(new Date("05/05/2021")));
        feeVersionDto.setValidTo(new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2021"));

        final VolumeAmountDto volumeAmountDto = new VolumeAmountDto();
        volumeAmountDto.setAmount(new BigDecimal("333"));
        feeVersionDto.setVolumeAmount(volumeAmountDto);

        return feeVersionDto;
    }

    private Fee2Dto buildFee2Dto() throws ParseException {

        final Fee2Dto fee2Dto = new Fee2Dto();

        fee2Dto.setCode("AAA");

        final ApplicantTypeDto applicantTypeDto = new ApplicantTypeDto();

        applicantTypeDto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
        applicantTypeDto.setName("BBB");
        applicantTypeDto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("02/02/2021"));
        fee2Dto.setApplicantTypeDto(applicantTypeDto);

        final ChannelTypeDto channelTypeDto = new ChannelTypeDto();
        channelTypeDto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("03/03/2021"));
        channelTypeDto.setName("CCC");
        channelTypeDto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("04/04/2021"));
        fee2Dto.setChannelTypeDto(channelTypeDto);

        fee2Dto.setFeeType("DDD");

        final List<FeeVersionDto> feeVersionDtoList = new ArrayList<>();
        feeVersionDtoList.add(buildFeeVersionDto());
        fee2Dto.setFeeVersionDtos(feeVersionDtoList);

        final EventTypeDto eventTypeDto = new EventTypeDto();
        eventTypeDto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("07/07/2021"));
        eventTypeDto.setName("PPP");
        eventTypeDto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("08/08/2021"));
        fee2Dto.setEventTypeDto(eventTypeDto);

        final Jurisdiction1Dto jurisdiction1Dto = new Jurisdiction1Dto();
        jurisdiction1Dto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("09/09/2021"));
        jurisdiction1Dto.setName("QQQ");
        jurisdiction1Dto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2021"));
        fee2Dto.setJurisdiction1Dto(jurisdiction1Dto);

        final Jurisdiction2Dto jurisdiction2Dto = new Jurisdiction2Dto();
        jurisdiction2Dto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2021"));
        jurisdiction2Dto.setName("RRR");
        jurisdiction2Dto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
        fee2Dto.setJurisdiction2Dto(jurisdiction2Dto);

        fee2Dto.setCurrentVersion(buildFeeVersionDto());
        fee2Dto.setKeyword("SSS");
        fee2Dto.setMatchingVersion(buildFeeVersionDto());
        fee2Dto.setMaxRange(new BigDecimal("444"));
        fee2Dto.setMinRange(new BigDecimal("555"));
        fee2Dto.setRangeUnit("TTT");

        final ServiceTypeDto serviceTypeDto = new ServiceTypeDto();
        serviceTypeDto.setCreationTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
        serviceTypeDto.setName("UUU");
        serviceTypeDto.setLastUpdated(new SimpleDateFormat("dd/MM/yyyy").parse("02/02/2020"));
        fee2Dto.setServiceTypeDto(serviceTypeDto);

        fee2Dto.setUnspecifiedClaimAmount(true);

        return fee2Dto;
    }

}
