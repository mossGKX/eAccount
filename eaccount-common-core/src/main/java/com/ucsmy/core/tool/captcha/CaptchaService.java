package com.ucsmy.core.tool.captcha;

import com.google.code.kaptcha.Producer;
import com.ucsmy.core.tool.security.SecurityService;
import com.ucsmy.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);
    private final static int INVALID_TIME = 5 * 60;

    private SecurityService securityService;
    private Producer captchaProducer;

    public CaptchaService(SecurityService securityService, Producer captchaProducer) {
        this.securityService = securityService;
        this.captchaProducer = captchaProducer;
    }

    public CaptchaBean getCaptcha(String securityCode) {
        if(StringUtils.isNotEmpty(securityCode))
            securityService.deleteSecurityCode(securityCode);
        String capText = captchaProducer.createText();
        securityCode = securityService.genSecurityCode(capText, INVALID_TIME, SecurityService.Business.CAPTCHA);
        BufferedImage image = captchaProducer.createImage(capText);
        return new CaptchaBean(securityCode, getImageBase64(image));
    }

    public boolean checkCaptcha(String securityCode, String text) {
        if(text == null)
            return false;
        SecurityService.Security<String> security = securityService.getSecurityCode(securityCode, SecurityService.Business.CAPTCHA);
        securityService.deleteSecurityCode(securityCode);
        return security != null && text.equalsIgnoreCase(security.getData());
    }

    public boolean checkCaptcha(HttpServletRequest request) {
        return checkCaptcha(request.getParameter("securityCode"), request.getParameter("captcha"));
    }

    private String getImageBase64(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", out);
            return new String(Base64Utils.encode(out.toByteArray()));
        } catch (IOException e) {
            LOGGER.error("验证码图片流输出异常", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("关闭验证码图片输出流异常", e);
            }
        }
        return "";
    }

    class CaptchaBean {
        private String securityCode;// 安全码
        private String image;// 验证码base64图片

        public CaptchaBean(String securityCode, String image) {
            this.securityCode = securityCode;
            this.image = image;
        }

        public String getSecurityCode() {
            return securityCode;
        }

        public void setSecurityCode(String securityCode) {
            this.securityCode = securityCode;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
