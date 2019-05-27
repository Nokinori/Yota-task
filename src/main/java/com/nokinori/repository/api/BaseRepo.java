package com.nokinori.repository.api;

import com.nokinori.repository.entities.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository for all entities extended from {@link BaseEntity}.
 *
 * @param <T>  type of entity.
 * @param <ID> type of Id.
 */
@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity, ID> extends CrudRepository<T, ID> {

}
