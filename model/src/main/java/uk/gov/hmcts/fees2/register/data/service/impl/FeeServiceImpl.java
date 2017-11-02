package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by tarun on 30/10/2017.
 */

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private Fee2Repository fee2Repository;

    public Fee save(Fee fee) {
        return fee2Repository.save(fee);
    }

    @Override
    /** Magic method that "googles" fees */
    public List<Fee> lookup(LookupFeeDto dto) {

        return fee2Repository.findAll(
            (rootFee, criteriaQuery, criteriaBuilder) ->
                buildPredicate(rootFee)
            );

    }

    private Predicate buildPredicate(Root<Fee> feeRoot) {

        //Predicate p =
        return null;
    }

}
