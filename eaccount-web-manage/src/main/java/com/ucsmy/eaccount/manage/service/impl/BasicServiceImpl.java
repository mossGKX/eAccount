package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.bean.BaseNode;
import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.eaccount.manage.ext.AntdPageInfo;

public class BasicServiceImpl<E extends BaseNode, D extends BasicDao<E>> extends com.ucsmy.core.service.BasicServiceImpl<E, D> {

    @Override
    public PageInfo<E> get(E entity, Pageable pageable) {
        return new AntdPageInfo<>(super.get(entity, pageable), pageable);
    }
}
