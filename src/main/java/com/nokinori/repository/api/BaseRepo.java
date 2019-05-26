package com.nokinori.repository.api;

import com.nokinori.repository.entities.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity, ID> extends CrudRepository<T, ID> {

}
