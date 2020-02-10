package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.bean.ManageLogInfo;
import org.springframework.stereotype.Repository;

/**
 * manage_log_info
 * Created by ucs_gaokx on 2017/5/8.
 */
@Repository
public interface ManageLogInfoMapper {

    /**
     * 添加logInfo
     * @param manageLogInfo
     * @return
     */
    int addManageLogInfo(ManageLogInfo manageLogInfo);
}
