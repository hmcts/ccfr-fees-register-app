package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.controllers.exceptions.FeesException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.util.ExcelGeneratorUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.gov.hmcts.fees2.register.data.util.FeesDateUtil.getDateTimeForReportName;

@Api(value = "Report Controller")
@RestController
@SwaggerDefinition(tags = {@Tag(name = "ReportController",
        description = "Fee Register Report API to be used for generating Audit report")})
@AllArgsConstructor
@Validated
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private final FeeSearchService feeSearchService;

    @Autowired
    private final FeeDtoMapper feeDtoMapper;

    @ApiOperation("API to generate Report for Fee_Register System")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Report Generated"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/report/download")
    public ResponseEntity<byte[]> downloadReport(
            final HttpServletResponse response) throws IOException {

        LOG.info("Retrieving Fees");

        byte[] reportBytes = null;

        HSSFWorkbook workbook = null;

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            final SearchFeeDto searchFeeDto = new SearchFeeDto();
            searchFeeDto.setIsDraft(false);

            final HttpHeaders headers = new HttpHeaders();

            final String fileName = "Fee_Register_"
                    + getDateTimeForReportName(new Date(System.currentTimeMillis()))
                    + ".xls";

            final List<Fee2Dto> fee2DtoList = feeSearchService
                    .search(searchFeeDto)
                    .stream()
                    .map(feeDtoMapper::toFeeDto)
                    .collect(Collectors.toList());

            if (Optional.ofNullable(fee2DtoList).isPresent()) {
                LOG.info("No of Records exists : {}", fee2DtoList.size());
                workbook = (HSSFWorkbook) ExcelGeneratorUtil.exportToExcel(fee2DtoList);
            }

            if (workbook != null) {
                workbook.write(baos);
            }

            reportBytes = baos.toByteArray();

            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);

        } catch (final Exception ex) {
            throw new FeesException(ex);
        }
    }

}
