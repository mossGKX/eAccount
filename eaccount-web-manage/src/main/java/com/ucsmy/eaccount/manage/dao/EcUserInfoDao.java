package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.EcUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EcUserInfoDao extends BasicDao<EcUserInfo> {

    /**
     * 更新状态
     *
     * @param id id
     * @return
     */
    int updateStatus(@Param("id") String id);

    /**
     * 查找登录名、账户、用户编号是否已存在
     * @param entity
     * @return
     */
    int findExistUser(@Param("entity") EcUserInfo entity);
}
