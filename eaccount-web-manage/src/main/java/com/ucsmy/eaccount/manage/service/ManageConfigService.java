package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.eaccount.manage.entity.ManageConfig;
import com.ucsmy.core.vo.RetMsg;

public interface ManageConfigService extends BasicService<ManageConfig> {
    /**
     * 更改状态
     */
    RetMsg updateStatus(String id);
}
