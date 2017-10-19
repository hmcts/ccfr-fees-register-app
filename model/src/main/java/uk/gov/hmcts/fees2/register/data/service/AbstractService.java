package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.repository.AbstractRepository;

import java.util.List;
import java.util.Optional;

/**
 * AbstractService interface
 *
 * @author Tarun Palisetty
 */

public interface AbstractService<T> {

    T findByNameOrThrow(String name);

    List<T> findAll();
}
