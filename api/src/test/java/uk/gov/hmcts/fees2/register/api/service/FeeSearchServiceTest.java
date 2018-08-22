package uk.gov.hmcts.fees2.register.api.service;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
public class FeeSearchServiceTest {

    private String author = "test-author";

    @Autowired
    FeeSearchService feeSearchService;

    @Autowired
    FeeService feeService;

    @Autowired
    FeeVersionService feeVersionService;

    @Autowired
    FeeDtoMapper feeDtoMapper;

    @Test
    @Transactional
    public void givenActiveFeeExist_thenFeeShouldBeReturnedInSearchResults() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        DateTime fromTime = new DateTime().minusDays(1);
        DateTime toTime = new DateTime().plusDays(1);
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            true, null, null, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> savedFee.getCode().equals(fee2Dto.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenExpiredFeeExists_thenFeeShouldBeReturnedInSearchResults() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        DateTime fromTime = new DateTime().minusDays(2);
        DateTime toTime = new DateTime().minusDays(1);
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, true, null, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeExistsAndAuthorIsSet_thenSearchingByAuthorShouldReturnGivenFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        DateTime fromTime = new DateTime().minusDays(2);
        DateTime toTime = new DateTime().minusDays(1);
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setAuthor(author);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(author, null,
            null, null, null, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeExistsAndApprovedByIsSet_thenSearchingByApprovedByShouldReturnGivenFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        DateTime fromTime = new DateTime().minusDays(2);
        DateTime toTime = new DateTime().minusDays(1);
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setApprovedBy(author);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, author,
            null, null, null, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeWithGivenStatusExists_thenSearchingByGivenStatusShouldReturnTheFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        DateTime fromTime = new DateTime().minusDays(2);
        DateTime toTime = new DateTime().minusDays(1);
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.pending_approval);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, null, FeeVersionStatus.pending_approval, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeWithGivenDescriptionExists_thenSearchingByGivenDescriptionShouldReturnTheFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setValidFrom(null);
        feeVersion.setValidTo(null);
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setDescription("This is a fee version object");

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, null, null, " fee version object", null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeWithGivenSiRefIdExists_thenSearchingByGivenSiRefIdShouldReturnTheFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setValidFrom(null);
        feeVersion.setValidTo(null);
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setSiRefId("4.a");

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, null, null, null, "4", null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeWithGivenFeeVersionAmountExists_thenSearchingByGivenFeeVersionAmountShouldReturnTheFee() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setValidFrom(null);
        feeVersion.setValidTo(null);
        feeVersion.setStatus(FeeVersionStatus.approved);
        Amount amount = new FlatAmount();
        amount.setAmountValue(new BigDecimal(5));
        feeVersion.setAmount(amount);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, null, null, null, null, new BigDecimal(5));

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }

    @Test
    @Transactional
    public void givenFeeWithGivenApprovedFeeVersionExistsThenSearchingByGivenFeeVersionShouldReturnTheFeeWithSetMatchingVersion() {
        Fee fee = new FixedFee();
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setValidFrom(new DateTime().minusDays(1).toDate());
        feeVersion.setValidTo(new DateTime().plusDays(1).toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);
        String memoLine = UUID.randomUUID().toString();
        feeVersion.setMemoLine(memoLine);

        feeVersion.setFee(fee);
        fee.setFeeVersions(Collections.singletonList(feeVersion));

        feeService.saveAndGenerateFeeCode(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            true, false, null, null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> {
                if(fee2Dto != null && fee2Dto.getCode() != null) {
                    return fee2Dto.getCode().equals(savedFee.getCode());
                }
                return false;
            })
            .collect(Collectors.toList());

        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
        assertTrue("The matching fee version should be the saved one.", result.get(0).getMatchingVersion().getMemoLine().equals(memoLine));
    }
}
