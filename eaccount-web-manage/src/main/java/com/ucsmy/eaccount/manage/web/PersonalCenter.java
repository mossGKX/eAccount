package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.utils.RSAUtils;
import com.ucsmy.core.utils.SpringSecurityUtils;
import com.ucsmy.core.utils.StringUtils;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.eaccount.manage.entity.ManageUserProfile;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageUserAccountService;
import com.ucsmy.eaccount.manage.service.ManageUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 个人中心Controller
 * Created by cui-fate on 2017/7/16.
 */
@RestController
@RequestMapping("personal")
public class PersonalCenter {

    @Autowired
    private ManageUserProfileService profileService;

    @Autowired
    private ManageUserAccountService accountService;

    @PostMapping(value = "updatePassword")
    public Object updatePassword(@RequestParam String oldPassword, @RequestParam String password,
                                 @RequestParam String confirmPassword) {
        oldPassword = RSAUtils.decryptBySession(oldPassword).getData();
        password = RSAUtils.decryptBySession(password).getData();
        confirmPassword = RSAUtils.decryptBySession(confirmPassword).getData();

        if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword))
            return RetMsg.error("密码不能为空");

        ManageUserAccount userAccount = accountService.getById(SpringSecurityUtils.getUserByContext().getId());
        oldPassword = StringUtils.passwordEncrypt(userAccount.getAccount(), oldPassword, userAccount.getSalt());
        if (!oldPassword.equals(userAccount.getPassword()))
            return RetMsg.error("原密码不正确");
        if (!password.equals(confirmPassword))
            return RetMsg.error("新密码与确认密码输入不相同");
        userAccount.setPassword(password);
        return accountService.updatePassword(userAccount);
    }

    @PostMapping(value = "getUserInfo")
    public Object getUserInfo() {
        ManageUserProfile userProfile = profileService.getById(SpringSecurityUtils.getUserByContext().getId());
        return RetMsg.success(userProfile);
    }

    @PostMapping(value = "updateUserInfo")
    public Object updateUserInfo(@RequestBody ManageUserProfile profile) {
        ManageUserProfile userProfile = profileService.getById(SpringSecurityUtils.getUserByContext().getId());
        userProfile.setName(profile.getName());
        userProfile.setTelephone(profile.getTelephone());
        userProfile.setMobilephone(profile.getMobilephone());
        userProfile.setEmail(profile.getEmail());
        userProfile.setGender(profile.getGender());
        return profileService.update(userProfile);
    }
}
