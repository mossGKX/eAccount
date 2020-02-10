package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.eaccount.manage.dao.ManageConfigDao;
import com.ucsmy.eaccount.manage.entity.ManageConfig;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageConfigServiceImpl extends BasicServiceImpl<ManageConfig, ManageConfigDao> implements ManageConfigService {
    @Override
    protected RetMsg saveHandle(ManageConfig entity) {
        if(dao.findByGroupNameAndName(entity.getGroupName(), entity.getName()) != null)
            return RetMsg.error("参数已存在");
        return super.saveHandle(entity);
    }

    @Override
    protected RetMsg updateHandle(ManageConfig pageEntity, ManageConfig databaseEntity) {
        // groupName或name被修改，查库判断是不是已存在参数
        if(!(pageEntity.getGroupName().equals(databaseEntity.getGroupName()) && pageEntity.getName().equals(databaseEntity.getName()))
                && dao.findByGroupNameAndName(pageEntity.getGroupName(), pageEntity.getName()) != null)
            return RetMsg.error("参数已存在");
        return super.updateHandle(pageEntity, databaseEntity);
    }

    @Override
    @Transactional
    public RetMsg updateStatus(String id) {
        int i = dao.updateStatus(id);
        if(i == 0) {
            return RetMsg.error("更改状态失败");
        }
        return RetMsg.success("更改状态成功");
    }
}