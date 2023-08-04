package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.ReasonDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.security.Principal;

import static java.util.Optional.ofNullable;

@Tag(name= "FeesRegister")
@RestController
@Validated
public class FeeVersionController {

    private final FeeVersionService feeVersionService;

    private final FeeDtoMapper mapper;

    @Autowired
    public FeeVersionController(final FeeVersionService feeVersionService, final FeeDtoMapper mapper) {
        this.feeVersionService = feeVersionService;
        this.mapper = mapper;
    }

    @Operation(summary = "Deletes a fee version for the given fee code if its on draft state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the fee version for the given fee code."),
    })
    @DeleteMapping("/fees/{code}/versions/{version}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeeVersion(
            @PathVariable("code") final String code,
            @PathVariable("version") final Integer version) {
        feeVersionService.deleteDraftVersion(code, version);
    }

    @Operation(summary = "Create fee version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
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

    @Operation(summary = "Edit a fee version for the given fee code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully edited the fee version for the given fee code."),
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

    @Operation(summary = "Approve a fee version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fee version is Approved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable("feeCode") final String feeCode, @PathVariable("version") final Integer version,
                        final Principal principal) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.approved, getUserName(principal));
    }

    @Operation(summary = "Reject a fee version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fee version is Rejected"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable("feeCode") final String feeCode, @PathVariable("version") final Integer version,
                       @RequestBody(required = false) final ReasonDto reasonDto, final Principal principal) {
        if (null != reasonDto) {
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, getUserName(principal),
                    (null != reasonDto.getReasonForReject()) ? Encode
                            .forHtml(reasonDto.getReasonForReject()) : "");
        } else {
            feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.draft, getUserName(principal), null);
        }
    }

    @Operation(summary = "Submit a fee version to approval")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fee version is submitted to approval"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/fees/{feeCode}/versions/{version}/submit-for-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitForReview(@PathVariable("feeCode") final String feeCode,
                                @PathVariable("version") final Integer version) {
        feeVersionService.changeStatus(feeCode, version, FeeVersionStatus.pending_approval, null);
    }

    private String getUserName(final Principal principal) {
        return ofNullable(principal)
                .map(Principal::getName)
                .orElse(null);
    }
}
