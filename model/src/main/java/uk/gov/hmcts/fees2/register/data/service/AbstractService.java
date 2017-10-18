package uk.gov.hmcts.fees2.register.data.service;

import java.util.List;

/**
 * AbstractService interface
 *
 * @author Tarun Palisetty
 */

public interface AbstractService<T> {

    T findByNameOrThrow(String name);

    List<T> findAll();
}
