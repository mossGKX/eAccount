package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.eaccount.manage.entity.ManageResources;

import java.util.List;

public interface ManageResourcesService extends BasicService<ManageResources> {
    List<ManageResources> getByMenuId(String menuId);
}
