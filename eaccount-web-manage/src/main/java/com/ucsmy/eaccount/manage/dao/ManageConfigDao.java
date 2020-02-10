package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageConfigDao extends BasicDao<ManageConfig> {
    int updateStatus(@Param("id") String id);

    List<ManageConfig> findByGroupName(@Param("groupName") String groupName);

    ManageConfig findByGroupNameAndName(@Param("groupName") String groupName, @Param("name") String name);
}
