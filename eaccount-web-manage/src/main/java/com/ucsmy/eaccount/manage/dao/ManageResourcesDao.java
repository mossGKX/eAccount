package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageResources;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageResourcesDao extends BasicDao<ManageResources> {

    /**
     * 根据菜单ID获取资源
     * @param menuId 菜单ID
     */
    List<ManageResources> findByMenuId(@Param("menuId") String menuId);
}
