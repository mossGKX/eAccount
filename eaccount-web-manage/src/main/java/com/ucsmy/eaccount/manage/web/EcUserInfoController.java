package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.entity.EcUserInfo;
import com.ucsmy.eaccount.manage.service.EcUserInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ec/user")
public class EcUserInfoController extends BasicController<EcUserInfo, EcUserInfoService> {

    @RequestMapping( value = "updateStatus/{id}", method = RequestMethod.POST )
    public RetMsg updateStatus(@PathVariable(value = "id") String id) {
        return service.updateStatus(id);
    }

    @Override
    public RetMsg delete(@PathVariable(value = "id") String id) {
        return super.delete(id);
    }
}
