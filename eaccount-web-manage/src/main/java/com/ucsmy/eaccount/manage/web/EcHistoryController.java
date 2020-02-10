package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.EcHistory;
import com.ucsmy.eaccount.manage.service.EcHistoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ec/history")
public class EcHistoryController extends BasicController<EcHistory, EcHistoryService> {

}
