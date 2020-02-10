package com.ucsmy.core.configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.ucsmy.core.tool.captcha.CaptchaController;
import com.ucsmy.core.tool.captcha.CaptchaProperties;
import com.ucsmy.core.tool.captcha.CaptchaService;
import com.ucsmy.core.tool.security.SecurityService;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

/**
 * Created by cui on 2017/6/1.
 */
public interface CaptchaConfig {

    @Bean
    default CaptchaProperties captchaProperties() {
        return new CaptchaProperties();
    }

    @Bean
    default Producer captchaProducer(CaptchaProperties captcha) {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", captcha.getBorder());
        properties.setProperty("kaptcha.border.color", captcha.getBorderColor());
        properties.setProperty("kaptcha.border.thickness", String.valueOf(captcha.getBorderThickness()));
        properties.setProperty("kaptcha.textproducer.font.color", captcha.getFontColor());
        properties.setProperty("kaptcha.textproducer.font.names", captcha.getFontNames());
        properties.setProperty("kaptcha.textproducer.font.size", String.valueOf(captcha.getFontSize()));
        properties.setProperty("kaptcha.image.width", String.valueOf(captcha.getImageWidth()));
        properties.setProperty("kaptcha.image.height", String.valueOf(captcha.getImageHeight()));
        properties.setProperty("kaptcha.textproducer.char.length", String.valueOf(captcha.getCharLength()));
        properties.setProperty("kaptcha.textproducer.char.space", String.valueOf(captcha.getCharSpace()));
        properties.setProperty("kaptcha.textproducer.char.string", captcha.getCharString());
        properties.setProperty("kaptcha.noise.color", captcha.getNoiseColor());
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }

    @Bean
    default CaptchaService captchaService(SecurityService securityService, Producer producer) {
        return new CaptchaService(securityService, producer);
    }

    @Bean
    default CaptchaController captchaController(CaptchaService captchaService) {
        return new CaptchaController(captchaService);
    }
}
