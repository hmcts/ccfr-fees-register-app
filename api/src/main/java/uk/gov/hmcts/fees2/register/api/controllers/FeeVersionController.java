package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@Validated
@RequestMapping("/fees-register")
public class FeeVersionController {

    private static final Logger LOG = LoggerFactory.getLogger(FeeVersionController.class);

    private final FeeVersionService feeVersionService;

    private final FeeDtoMapper mapper;

    @Autowired
    public FeeVersionController(FeeVersionService feeVersionService, FeeDtoMapper mapper) {
        this.feeVersionService = feeVersionService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Deletes a fee version for the given fee code if its on draft state")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the fee version for the given fee code."),
    })
    @DeleteMapping("/fees/{code}/version/{version}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeeVersion(
        @PathVariable("code") String code,
        @PathVariable("version") Integer version) {
        feeVersionService.deleteDraftVersion(code, version);
    }

    @ApiOperation(value = "Create fee version")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/fees/{feeCode}/version")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVersion(@PathVariable("feeCode") String feeCode, @RequestBody @Validated final FeeVersionDto request) {
        feeVersionService.save(mapper.toFeeVersion(request), feeCode);
    }

}
