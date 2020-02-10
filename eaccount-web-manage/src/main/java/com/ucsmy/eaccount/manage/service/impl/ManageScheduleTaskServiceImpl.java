package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.utils.UUIDGenerator;
import com.ucsmy.eaccount.manage.dao.ManageIpScheduleTaskDao;
import com.ucsmy.eaccount.manage.entity.ManageIpScheduleTask;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageScheduleTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务Service
 *
 * @author ucs_gaokx
 * @since 2017/9/7
 */
@Service
public class ManageScheduleTaskServiceImpl extends BasicServiceImpl<ManageIpScheduleTask, ManageIpScheduleTaskDao> implements ManageScheduleTaskService {

    private static final String STATUS_STOP = "0";
    private static final String STATUS_START = "1";

    @Override
    @Transactional
    public RetMsg save(ManageIpScheduleTask entity) {
        entity.setId(UUIDGenerator.generate());
        entity.setStatus(STATUS_STOP);
        return super.save(entity);
    }

    @Override
    public RetMsg updateHandle(ManageIpScheduleTask pageEntity, ManageIpScheduleTask databaseEntity) {
        if (pageEntity.getStatus() == null && !STATUS_STOP.equals(databaseEntity.getStatus())) {
            return RetMsg.error("定时任务还在启用中，不能修改");
        }
        if (pageEntity.getStatus() == null && dao.isTaskCodeExist(pageEntity.getTaskCode(), pageEntity.getId()) > 0) {
            return RetMsg.error("任务码已存在");
        }
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