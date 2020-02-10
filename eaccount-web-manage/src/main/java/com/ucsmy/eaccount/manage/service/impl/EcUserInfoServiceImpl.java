package com.ucsmy.eaccount.manage.service.impl;


import com.ucsmy.core.utils.*;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.dao.EcUserInfoDao;
import com.ucsmy.eaccount.manage.entity.EcAccount;
import com.ucsmy.eaccount.manage.entity.EcUserAccountRel;
import com.ucsmy.eaccount.manage.entity.EcUserInfo;
import com.ucsmy.eaccount.manage.ext.enums.AccountTypeEnum;
import com.ucsmy.eaccount.manage.ext.enums.SerialNoPerfixEnum;
import com.ucsmy.eaccount.manage.ext.enums.UserTypeEnum;
import com.ucsmy.eaccount.manage.service.EcAccountService;
import com.ucsmy.eaccount.manage.service.EcUserAccountRelService;
import com.ucsmy.eaccount.manage.service.EcUserInfoService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EcUserInfoServiceImpl extends BasicServiceImpl<EcUserInfo, EcUserInfoDao> implements EcUserInfoService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EcUserInfoServiceImpl.class);

    @Autowired
    private EcAccountService ecAccountService;

    @Autowired
    private EcUserAccountRelService ecUserAccountRelService;

    @Override
    protected RetMsg saveHandle(EcUserInfo entity) {
        // 检查参数
        RetMsg validateInfo = HibernateValidateUtils.getError(entity);
        if (validateInfo.isError()) {
            return validateInfo;
        }
        if (!(UserTypeEnum.EMPLOYER.value.equals(entity.getUserType())
                || UserTypeEnum.MERCHANT.value.equals(entity.getUserType()))) {
            return RetMsg.error("无效的用户类型");
        }
        if(StringUtils.isEmpty(entity.getId())) {
            entity.setId(SerialNumberGenerator.generate(SerialNoPerfixEnum.EC_USERINFO_ID.prefix));
        }
        // 设置账号
        entity.setUserNo(SerialNumberGenerator.generate(SerialNoPerfixEnum.EC_USERNO.prefix));
        entity.setAccountNo(SerialNumberGenerator.generate(SerialNoPerfixEnum.EC_USERACCOUNT.prefix));
        // 检查账号、登录名是否存在
        int existUser = dao.findExistUser(entity);
        if (existUser > 0) {
            return RetMsg.error("登录名已存在");
        }
        // 密码解密
        RetMsg<String> pwdRet = RSAUtils.decryptBySession(entity.getPassword());
        RetMsg<String> payPwdRet = RSAUtils.decryptBySession(entity.getPayPwd());
        if (pwdRet.isError() || payPwdRet.isError()) {
            log.info("密码解密失败");
            return RetMsg.error("保存失败");
        }
        entity.setPassword(pwdRet.getData());
        entity.setPayPwd(payPwdRet.getData());
        // 加盐
        this.passwordHandle(entity);
        entity.setStatus(StringUtils.CommStatus.INUSE.value);
        entity.setCreateTime(new Date());
        EcAccount account = createEcAccount(entity);
        RetMsg saveMsg = ecUserAccountRelService.save(createUserAccountRel(account));
        if (saveMsg.isError()) {
            return saveMsg;
        }
        return ecAccountService.save(account);
    }

    @Transactional
    @Override
    public RetMsg updateStatus(String id) {
        int i = dao.updateStatus(id);
        if(i == 0) {
            return RetMsg.error("更改状态失败");
        }
        return RetMsg.success("更改状态成功");
    }

    @Transactional
    @Override
    public RetMsg update(EcUserInfo entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            return RetMsg.error("记录Id不能为空");
        }
        EcUserInfo databaseEntity = this.getById(entity.getId());
        if (databaseEntity == null) {
            return RetMsg.error("记录不存在");
        }
        if (entity.getId() == null) {
            return RetMsg.error("记录Id不能为空");
        }
        RetMsg<EcUserInfo> handleMsg = this.updateHandle(entity, databaseEntity);
        if(handleMsg.isError()) {
            return handleMsg;
        }
        int i = dao.update(handleMsg.getData());
        if(i == 0) {
            return RetMsg.error("修改失败");
        }
        return RetMsg.success("修改成功");
    }

    /**
     * 密码加盐处理
     */
    private void passwordHandle(EcUserInfo entity) {
        entity.setSalt(StringUtils.getRandom(6));
        entity.setPassword(StringUtils.passwordEncrypt(entity.getUserNo(), entity.getPassword(), entity.getSalt()));
        entity.setPayPwd(StringUtils.passwordEncrypt(entity.getAccountNo(), entity.getPayPwd(), entity.getSalt()));
    }

    /**
     * 商户和资方用户，只能创建默认资金账户
     * @param userInfo
     * @return
     */
    private EcAccount createEcAccount(EcUserInfo userInfo) {
        EcAccount entity = new EcAccount();
        entity.setId(SerialNumberGenerator.generate(SerialNoPerfixEnum.EC_ACCOUNT_ID.prefix));
        entity.setCreateTime(userInfo.getCreateTime());
        entity.setEditTime(userInfo.getCreateTime());
        entity.setVersion(1L);
        entity.setRemark("");
        entity.setStatus(userInfo.getStatus());
        entity.setAccountNo(userInfo.getAccountNo());
        entity.setAccountType(AccountTypeEnum.FUND.value);
        entity.setBalance((short) 0);
        entity.setUnbalance((short) 0);
        entity.setSecurityMoney((short) 0);
        entity.setTotalIncome((short) 0);
        entity.setTotalExpend((short) 0);
        entity.setTodayIncome((short) 0);
        entity.setTodayExpend((short) 0);
        entity.setSettAmount((short) 0);
        entity.setUserNo(userInfo.getUserNo());
        entity.setCreditLine((short) 0);
        return entity;
    }

    /**
     * 创建用户-账户映射entity
     * @param account
     * @return
     */
    private EcUserAccountRel createUserAccountRel(EcAccount account) {
        EcUserAccountRel entity = new EcUserAccountRel();
        entity.setAccountType(account.getAccountType());
        entity.setAccountNo(account.getAccountNo());
        entity.setUserNo(account.getUserNo());
        return entity;
    }
}