package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.ManageRole;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageRoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys/role")
public class ManageRoleController extends BasicController<ManageRole, ManageRoleService> {

    @RequestMapping( value = "updateMenu", method = RequestMethod.POST )
    public RetMsg updateMenu(@RequestBody ManageRole entity) {
        return service.updateMenu(entity);
    }

    @RequestMapping( value = "listAll", method = RequestMethod.POST )
    public RetMsg find() {
        return RetMsg.success(service.get());
    }

}
