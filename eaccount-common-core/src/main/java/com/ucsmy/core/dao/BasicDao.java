package com.ucsmy.core.dao;

import com.ucsmy.core.bean.BaseNode;
import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasicDao<E extends BaseNode> {
    int save(E entity);

    int update(E entity);

    int delete(@Param("id") String id);

    E findById(@Param("id") String id);

    List<E> find();

    List<E> find(@Param("entity") E entity);

    PageInfo<E> find(Pageable pageable);

    PageInfo<E> find(@Param("entity") E entity, Pageable pageable);
}
