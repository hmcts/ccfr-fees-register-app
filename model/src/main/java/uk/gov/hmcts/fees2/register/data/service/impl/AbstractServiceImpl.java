package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.repository.AbstractRepository;
import uk.gov.hmcts.fees2.register.data.service.AbstractService;

import java.util.List;

/**
 *
 * AbstractService layer methods used to obtain and modify Repository
 *
 * @author Tarun Palisetty
 */

@Service
public abstract class AbstractServiceImpl<T> implements AbstractService<T> {

    private AbstractRepository<T, Long> abstractRepository;

    @Autowired
    public AbstractServiceImpl(AbstractRepository abstractRepository) {
        this.abstractRepository = abstractRepository;
    }

    /**
     * @param name name
     * @return return
     */
    @Override
    public T findByNameOrThrow(String name) {
        return abstractRepository.findByNameOrThrow(name);
    }

    /**
     *
     * @return return
     */
    @Override
    public List<T> findAll() {
        return abstractRepository.findAll();
    }
}
