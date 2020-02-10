package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.ManageIpScheduleTask;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageScheduleTaskService;
import org.springframework.web.bind.annotation.*;


/**
 * 定时任务Controller
 * Created by ucs_gaokx on 2017/4/18.
 */
@RestController
@RequestMapping("sys/schedule")
public class ManageScheduleTaskController extends BasicController<ManageIpScheduleTask, ManageScheduleTaskService> {

    @RequestMapping( value = "updateStatus/{id}", method = RequestMethod.POST )
    public RetMsg updateStatus(@PathVariable(value = "id") String id) {
        return service.updateStatus(id);
    }

}
