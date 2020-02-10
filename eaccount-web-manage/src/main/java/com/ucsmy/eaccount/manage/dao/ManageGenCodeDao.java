package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.core.bean.ManageGenCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageGenCodeDao extends BasicDao<ManageGenCode> {
    // 查询当前数据库所有的表
    List<String> findAllTables();
}
