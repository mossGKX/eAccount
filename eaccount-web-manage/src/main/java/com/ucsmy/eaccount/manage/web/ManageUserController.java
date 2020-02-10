package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.core.utils.RSAUtils;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.eaccount.manage.entity.ManageUserProfile;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ucs_zhongtingyuan on 2017/4/10.
 */
@RestController
@RequestMapping("sys/user")
public class ManageUserController extends BasicController<ManageUserProfile, ManageUserProfileService> {

    @Autowired
    private ManageUserAccountService accountService;

    @RequestMapping( value = "updatePassword", method = RequestMethod.POST )
    public RetMsg updatePassword(@RequestBody ManageUserAccount entity) {
        ManageUserAccount account = accountService.getById(entity.getId());
        if(account == null) {
            return RetMsg.error("用户不存在");
        }
        account.setPassword(RSAUtils.decryptBySession(entity.getPassword()).getData());
        return accountService.updatePassword(account);
    }

    @RequestMapping( value = "shotOff", method = RequestMethod.POST )
    public RetMsg shotOff(@RequestParam String username) {
        return accountService.shotOff(username);
    }

    @RequestMapping( value = "updateStatus/{id}", method = RequestMethod.POST )
    public RetMsg updateStatus(@PathVariable(value = "id") String id) {
        return accountService.updateStatus(id);
    }

}
