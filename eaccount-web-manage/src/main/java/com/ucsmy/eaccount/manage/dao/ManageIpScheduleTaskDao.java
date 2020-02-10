package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageIpScheduleTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 定时任务Dao
 *
 * @author ucs_gaokx
 * @since 2017/9/7
 */
@Repository
public interface ManageIpScheduleTaskDao extends BasicDao<ManageIpScheduleTask> {

    /**
     * 任务码是否存在
     *
     * @param taskCode 任务码
     * @param id       排除的id
     * @return
     */
    int isTaskCodeExist(@Param("taskCode") String taskCode, @Param("id") String id);

    int updateStatus(@Param("id") String id);
}