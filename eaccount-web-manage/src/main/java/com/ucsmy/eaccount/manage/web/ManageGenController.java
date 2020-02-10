package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.core.bean.ManageGenCode;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageGenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys/gen")
public class ManageGenController extends BasicController<ManageGenCode, ManageGenService> {

    @RequestMapping( value = "getAllTables", method = RequestMethod.POST )
    public RetMsg getAllTables() {
        return RetMsg.success(service.getAllTables());
    }

}
