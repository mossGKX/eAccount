package com.ucsmy.core.service;

import com.ucsmy.core.bean.BaseNode;
import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.core.vo.RetMsg;

import java.util.List;

public interface BasicService<E extends BaseNode> {
    RetMsg save(E entity);

    RetMsg update(E entity);

    RetMsg delete(String id);

    E getById(String id);

    List<E> get();

    List<E> get(E entity);

    PageInfo<E> get(E entity, Pageable pageable);
}
