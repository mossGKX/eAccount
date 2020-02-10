package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.utils.CheckoutUtils;
import com.ucsmy.core.utils.StringUtils;
import com.ucsmy.eaccount.config.properties.UcsmyProperties;
import com.ucsmy.eaccount.manage.dao.ManageUserAccountDao;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageUserAccountServiceImpl extends BasicServiceImpl<ManageUserAccount, ManageUserAccountDao> implements ManageUserAccountService {

    @Autowired
    private UcsmyProperties properties;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    protected RetMsg saveHandle(ManageUserAccount entity) {
        RetMsg retMsg = userNameHandle(entity);
        if(!retMsg.isSuccess()) {
            return retMsg;
        }
        entity.setStatus((byte) 0);
        entity.setPassword(properties.getInitPassword());
        this.passwordHandle(entity);
        return super.saveHandle(entity);
    }

    @Override
    protected RetMsg updateHandle(ManageUserAccount pageEntity, ManageUserAccount databaseEntity) {
        RetMsg retMsg = userNameHandle(pageEntity);
        if(!retMsg.isSuccess()) {
            return retMsg;
        }
        return super.updateHandle(pageEntity, databaseEntity);
    }

    @Override
    @Transactional
    public RetMsg updatePassword(ManageUserAccount entity) {
        if(!CheckoutUtils.isPassword(entity.getPassword())) {
            return RetMsg.error("密码格式不正确");
        }
        this.passwordHandle(entity);
        int i = dao.updatePassword(entity);
        if(i == 0) {
            return RetMsg.error("修改密码失败");
        }
        shotOff(entity.getAccount());
        return RetMsg.success("修改密码成功");
    }

    @Override
    public ManageUserAccount getByUserName(String userName) {
        if(StringUtils.isEmpty(userName)) {
            return null;
        }
        return dao.findByUserName(userName);
    }

    public RetMsg shotOff(String username) {
        int i = 0;
        for(SessionInformation si : sessionRegistry.getAllSessions(username, Boolean.FALSE)) {
            si.expireNow();// 注销用户
            i++;
        }
        return RetMsg.success(i == 0 ? "无用户在线" : "成功下线" + i + "位用户");
    }

    @Override
    @Transactional
    public RetMsg updateStatus(String id) {
        int i = dao.updateStatus(id);
        if(i == 0) {
            return RetMsg.error("更改状态失败");
        }

        ManageUserAccount account = this.getById(id);
        if(account.getStatus() == 1) {// 被禁用，用户下线
            shotOff(account.getAccount());
        }
        return RetMsg.success("更改状态成功");
    }

    private RetMsg userNameHandle(ManageUserAccount entity) {
        ManageUserAccount account;
        if((account = getByUserName(entity.getAccount())) != null && !account.getId().equals(entity.getId())) {
            return RetMsg.error("账号已存在");
        }
        if((account = getByUserName(entity.getEmail())) != null && !account.getId().equals(entity.getId())) {
            return RetMsg.error("邮箱已存在");
        }
        if((account = getByUserName(entity.getMobilephone())) != null && !account.getId().equals(entity.getId())) {
            return RetMsg.error("手机已存在");
        }
        return RetMsg.success();
    }

    /**
     * 密码加盐处理
     */
    private void passwordHandle(ManageUserAccount entity) {
        entity.setSalt(StringUtils.getRandom(6));
        entity.setPassword(StringUtils.passwordEncrypt(entity.getAccount(), entity.getPassword(), entity.getSalt()));
    }
}
