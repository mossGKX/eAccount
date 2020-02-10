package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.entity.EcUserInfo;

public interface EcUserInfoService extends BasicService<EcUserInfo> {

    /**
     * 更改账号状态
     */
    RetMsg updateStatus(String id);
}
