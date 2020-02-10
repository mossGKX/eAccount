package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.entity.EcAccount;
import com.ucsmy.eaccount.manage.service.EcAccountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ec/account")
public class EcAccountController extends BasicController<EcAccount, EcAccountService> {

    @Override
    public RetMsg save(@RequestBody EcAccount entity) {
        return RetMsg.error();
    }

    @Override
    public RetMsg update(@RequestBody EcAccount entity) {
        return RetMsg.error();
    }

    @Override
    public RetMsg delete(@PathVariable(value = "id") String id) {
        return RetMsg.error();
    }
}
