package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageMenuDao extends BasicDao<ManageMenu> {
    /**
     * 更新资源
     */
    void updateResources(ManageMenu menu);

    /**
     * 根据角色ID获取菜单数据
     */
    List<ManageMenu> findByRoleId(@Param("roleId") String roleId);

    /**
     * 获取全部菜单和资源数据（初始化权限）
     */
    List<ManageMenu> findMenuAndResources();
}
