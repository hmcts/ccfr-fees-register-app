package uk.gov.hmcts.fees.register.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.model.Category;
import uk.gov.hmcts.fees.register.model.EntityNotFoundException;
import uk.gov.hmcts.fees.register.model.Fee;
import uk.gov.hmcts.fees.register.model.FeesRegister;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

    private FeesRegisterRepository feesRegisterRepository;

    @Autowired
    public FeesRegisterController(FeesRegisterRepository feesRegisterRepository) {
        this.feesRegisterRepository = feesRegisterRepository;
    }

    @GetMapping()
    public FeesRegister getAllFees() {
        return getFeesRegister();
    }


    @GetMapping("/categories")
    public List<Category> getCategories() {
        return getFeesRegister().getCategories();
    }

    @GetMapping("/categories/{id}")
    @ResponseBody
    public Category getCategory(@PathVariable(value = "id") String categoryId) {

        return getFeesRegister()
            .getClaimCategory(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found, id: " + categoryId));
    }

    @ApiOperation(value = "Find appropriate fees amount for given claim.",
        notes = "This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees). All input and output amounts are in pence.  ", response = Fee.class)
    @GetMapping("/categories/{id}/ranges/{amount}/fees")
    public Fee getCategoryRange(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @ApiParam(value = "This is claim amount in pence", required = true) @PathVariable(value = "amount") int amount) {

        return getCategory(id)
            .findRange(amount)
            .orElseThrow(() -> new EntityNotFoundException("Range not found, amount: " + amount))
            .getFee();
    }


    @ApiOperation(value = "Find appropriate flat fees for given fee id.",
        notes = "This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees) and flat fee id. ", response = Fee.class)
    @GetMapping("/categories/{id}/flat/{feeId}")
    public Fee getFlatFeeInACategory(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @ApiParam(value = "This is flat fee in a category", required = true) @PathVariable(value = "feeId") String feeId) {

        return getCategory(id)
            .findFlatFee(feeId)
            .orElseThrow(() -> new EntityNotFoundException("Flat fees not found, feeId: " + feeId));
    }

    @ApiOperation(value = "Find all flat fees for given category.",
        notes = "This endpoint returns all flat fees for given category(e.g. onlinefees or hearingfees). ", response = Fee.class)
    @GetMapping("/categories/{id}/flat")
    public List<Fee> getAllFlatFeesInACategory(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id) {

        List<Fee> flatFees = getCategory(id).getFlatFees();

        if(null == flatFees)
            throw new EntityNotFoundException("Flat fees not found, category: " + id);

        return flatFees;

    }


    private FeesRegister getFeesRegister() {
        return feesRegisterRepository.getFeesRegister();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> handleEntityNotFoundException() {
        return new ResponseEntity<>(NOT_FOUND);
    }
}
