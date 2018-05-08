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
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.util.Collections;
import java.util.List;
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

        feeService.save(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            true, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("Search should be able to find active fees.", result.size() == 1);
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

        feeService.save(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, true, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("Search should be able to find expired fees.", result.size() == 1);
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

        feeService.save(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(author, null,
            null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("Search should be able to find fees by author.", result.size() == 1);
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

        feeService.save(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, author,
            null, null, null);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("Search should be able to find fees by approvedBy.", result.size() == 1);
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

        feeService.save(fee);

        Fee savedFee = feeService.get(fee.getCode());

        SearchFeeDto searchFeeCriteria = new SearchFeeDto();
        SearchFeeVersionDto searchFeeVersionCriteria = new SearchFeeVersionDto(null, null,
            null, null, FeeVersionStatus.pending_approval);

        List<FeeVersion> searchedFees = feeSearchService.search(searchFeeCriteria, searchFeeVersionCriteria);
        List<Fee2Dto> result = searchedFees
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .filter(fee2Dto -> fee2Dto.getCode().equals(savedFee.getCode()))
            .collect(Collectors.toList());

        assertTrue("Search should be able to find fees by fee version status.", result.size() == 1);
        assertTrue("The retrieved fee should be the saved one.", result.get(0).getCode().equals(savedFee.getCode()));
    }
}
