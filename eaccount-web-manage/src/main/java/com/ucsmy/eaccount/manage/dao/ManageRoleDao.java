package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageRole;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageRoleDao extends BasicDao<ManageRole> {
    /**
     * 更新菜单
     */
    void updateMenu(ManageRole role);
}
