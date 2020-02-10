package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.vo.RetMsg;
import org.springframework.stereotype.Service;
import com.ucsmy.eaccount.manage.dao.ManageRoleDao;
import com.ucsmy.eaccount.manage.entity.ManageRole;
import com.ucsmy.eaccount.manage.service.ManageRoleService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageRoleServiceImpl extends BasicServiceImpl<ManageRole, ManageRoleDao> implements ManageRoleService {

    @Override
    @Transactional
    public RetMsg updateMenu(ManageRole role) {
        dao.updateMenu(role);
        return RetMsg.success("更新权限成功");
    }
}
