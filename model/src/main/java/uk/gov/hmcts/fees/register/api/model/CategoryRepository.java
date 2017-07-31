package uk.gov.hmcts.fees.register.api.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{
    Optional<Category> findById(Integer integer);
}
