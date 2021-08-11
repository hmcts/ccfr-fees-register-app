package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.ReasonDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.*;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.util.IdamUtil;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@Validated
public class FeeVersionController {

    private static final Logger LOG = LoggerFactory.getLogger(FeeVersionController.class);

    private final FeeVersionService feeVersionService;

    private final FeeDtoMapper mapper;

    private final IdamUtil idamUtil;

    @Autowired
    public FeeVersionController(final FeeVersionService feeVersionService, final FeeDtoMapper mapper,
                                IdamUtil idamUtil) {
        this.feeVersionService = feeVersionService;
        this.mapper = mapper;
        this.idamUtil = idamUtil;
    }

    @ApiOperation(value = "Deletes a fee version for the given fee code if its on draft state")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the fee version for the given fee code."),
    })
    @DeleteMapping("/fees/{code}/versions/{version}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeeVersion(
            @PathVariable("code") final String code,
            @PathVariable("version") final Integer version) {
        feeVersionService.deleteDraftVersion(code, version);
    }

    @ApiOperation(value = "Create fee version")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping("/fees/{feeCode}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVersion(@PathVariable("feeCode") final String feeCode,
                              @RequestBody @Validated final FeeVersionDto request,
                              final Principal principal) {
        final Integer newVersion = feeVersionService.getMaxFeeVersion(feeCode);
        request.setVersion(newVersion + 1);

        feeVersionService.save(mapper.toFeeVersion(request, principal != null ? principal.getName() : null), feeCode);
    }

    @ApiOperation(value = "Edit a fee version for the given fee code")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully edited the fee version for the given fee code."),
    })

    @PutMapping("/fees/{code}/versions/{version}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editFeeVersion(
            @PathVariable("code") final String code,
            @PathVariable("version") final Integer version,
            @RequestBody @Validated final FeeVersionDto request) {
        final var feeVersion = feeVersionService.getFeeVersion(code, version);
        feeVersionService.saveFeeVersion(mapper.mapDtotoFeeVersion(request, feeVersion));
    }

    @ApiOperation(value = "Approve a fee version")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Fee version is Approved"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable("feeCode") final String feeCode, @PathVariable("version") final Integer version) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.approved, idamUtil.getUserName());
    }

    @ApiOperation(value = "Reject a fee version")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Fee version is Rejected"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable("feeCode") final String feeCode, @PathVariable("version") final Integer version,
                       @RequestBody(required = false) final ReasonDto reasonDto) {
        if (null != reasonDto) {
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, idamUtil.getUserName(),
                    (null != reasonDto.getReasonForReject()) ? Encode
                            .forHtml(reasonDto.getReasonForReject()) : "");
        } else {
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, idamUtil.getUserName(), null);
        }
    }

    @ApiOperation(value = "Submit a fee version to approval")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Fee version is submitted to approval"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/submit-for-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitForReview(@PathVariable("feeCode") final String feeCode,
                                @PathVariable("version") final Integer version) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.pending_approval, null);
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    public ResponseEntity<Map<String,String>> unauthorizedRequest(UnauthorizedRequestException e){
        LOG.error("Unauthorized request: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String,String>> forbiddenRequest(ForbiddenException e){
        LOG.error("Forbidden request: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> notFoundException(UserNotFoundException e){
        LOG.error("Not Found Exception: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> badRequest(BadRequestException e){
        LOG.error("Bad request: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity internalServerException(InternalServerException e) {
        LOG.debug("Internal Server Error: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
