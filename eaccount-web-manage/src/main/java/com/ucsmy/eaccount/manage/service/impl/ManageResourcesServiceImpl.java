package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.eaccount.manage.dao.ManageResourcesDao;
import com.ucsmy.eaccount.manage.entity.ManageResources;
import com.ucsmy.eaccount.manage.service.ManageResourcesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageResourcesServiceImpl extends BasicServiceImpl<ManageResources, ManageResourcesDao> implements ManageResourcesService {
    @Override
    @com.ucsmy.core.tool.log.annotation.Logger(printSQL = true)
    public List<ManageResources> getByMenuId(String menuId) {
        return dao.findByMenuId(menuId);
    }
}
