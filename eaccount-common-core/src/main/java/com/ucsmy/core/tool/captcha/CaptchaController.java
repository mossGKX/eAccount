package com.ucsmy.core.tool.captcha;

import com.ucsmy.core.vo.RetMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

public class CaptchaController {

    private CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping(value = "captcha")
    @ResponseBody
    public RetMsg captcha(String securityCode) throws IOException {
        return RetMsg.success(captchaService.getCaptcha(securityCode));
    }

}
