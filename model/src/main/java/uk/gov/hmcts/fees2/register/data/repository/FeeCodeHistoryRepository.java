package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.fees2.register.data.model.FeeCodeHistory;

public interface FeeCodeHistoryRepository extends JpaRepository<FeeCodeHistory, Integer> {

}
