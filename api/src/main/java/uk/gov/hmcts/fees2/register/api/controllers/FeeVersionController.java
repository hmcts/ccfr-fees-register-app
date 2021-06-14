package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.ReasonDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.security.Principal;

import static java.util.Optional.ofNullable;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@Validated
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
    @DeleteMapping("/fees/{code}/versions/{version}")
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
    @PostMapping("/fees/{feeCode}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVersion(@PathVariable("feeCode") String feeCode,
                              @RequestBody @Validated final FeeVersionDto request,
                              Principal principal) {
        Integer newVersion = feeVersionService.getMaxFeeVersion(feeCode);
        request.setVersion(newVersion + 1);

        feeVersionService.save(mapper.toFeeVersion(request, principal != null ? principal.getName() : null), feeCode);
    }

    @ApiOperation(value = "Approve a fee version")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Fee version is Approved"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable("feeCode") String feeCode, @PathVariable("version") Integer version, Principal principal) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.approved, ofNullable(principal)
            .map(p -> p.getName())
            .orElse(null));
    }

    @ApiOperation(value = "Reject a fee version")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Fee version is Rejected"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable("feeCode") String feeCode, @PathVariable("version") Integer version, @RequestBody(required = false) ReasonDto reasonDto) {
        if (null != reasonDto.getReasonForReject())
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, null, reasonDto.getReasonForReject());
        else
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, null);
    }

    @ApiOperation(value = "Submit a fee version to approval")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Fee version is submitted to approval"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/submit-for-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitForReview(@PathVariable("feeCode") String feeCode, @PathVariable("version") Integer version) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.pending_approval, null);
    }
}
