package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.ManageResources;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageResourcesService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys/resources")
public class ManageResourcesController extends BasicController<ManageResources, ManageResourcesService> {

    @RequestMapping( value = "getByMenuId/{id}", method = RequestMethod.POST )
    public RetMsg getByMenuId(@PathVariable("id") String menuId) {
        return RetMsg.success(service.getByMenuId(menuId));
    }

}
