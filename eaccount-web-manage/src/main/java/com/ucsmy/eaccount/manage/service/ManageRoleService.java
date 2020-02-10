package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.eaccount.manage.entity.ManageRole;
import com.ucsmy.core.vo.RetMsg;

public interface ManageRoleService extends BasicService<ManageRole> {
    RetMsg updateMenu(ManageRole role);
}
