package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.core.vo.RetMsg;

public interface ManageUserAccountService extends BasicService<ManageUserAccount> {

    RetMsg updatePassword(ManageUserAccount entity);

    ManageUserAccount getByUserName(String userName);

    /**
     * 通过用户名，进行下线操作
     */
    RetMsg shotOff(String username);

    /**
     * 更改账号状态
     */
    RetMsg updateStatus(String id);

}
