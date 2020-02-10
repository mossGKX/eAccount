package com.ucsmy.eaccount.manage.web;

import com.ucsmy.core.controller.BasicController;
import com.ucsmy.eaccount.manage.entity.EcUserPayInfo;
import com.ucsmy.eaccount.manage.service.EcUserPayInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ec/user/payInfo")
public class EcUserPayInfoController extends BasicController<EcUserPayInfo, EcUserPayInfoService> {

}
