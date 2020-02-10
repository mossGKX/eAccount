package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.ManageConfig;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageConfigService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys/config")
public class ManageConfigController extends BasicController<ManageConfig, ManageConfigService> {

    @RequestMapping( value = "updateStatus/{id}", method = RequestMethod.POST )
    public RetMsg updateStatus(@PathVariable(value = "id") String id) {
        return service.updateStatus(id);
    }

}
