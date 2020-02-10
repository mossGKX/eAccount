package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.eaccount.manage.dao.ManageUserProfileDao;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.eaccount.manage.entity.ManageUserProfile;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageUserAccountService;
import com.ucsmy.eaccount.manage.service.ManageUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ManageUserProfileServiceImpl extends BasicServiceImpl<ManageUserProfile, ManageUserProfileDao> implements ManageUserProfileService {

    @Autowired
    private ManageUserAccountService accountService;

    @Override
    protected RetMsg saveHandle(ManageUserProfile entity) {
        super.saveHandle(entity);
        entity.setCreateDate(new Date());
        copyToAccount(entity);
        return accountService.save(entity.getAccount());
    }

    @Override
    protected RetMsg updateHandle(ManageUserProfile pageEntity, ManageUserProfile databaseEntity) {
        pageEntity.setUpdateDate(new Date());
        copyToAccount(pageEntity);
        RetMsg retMsg = accountService.update(pageEntity.getAccount());
        if(retMsg.isSuccess()) {
            return super.updateHandle(pageEntity, databaseEntity);
        }
        return retMsg;
    }

    @Override
    @Transactional
    public RetMsg delete(String id) {
        RetMsg retMsg = accountService.delete(id);
        if(!retMsg.isSuccess()) {
            return retMsg;
        }
        return super.delete(id);
    }

    private void copyToAccount(ManageUserProfile entity) {
        ManageUserAccount account = entity.getAccount();
        account.setId(entity.getId());
        account.setEmail(entity.getEmail());
        account.setMobilephone(entity.getMobilephone());
        account.setCreateTime(entity.getCreateDate());
    }

}
