package com.ucsmy.core.utils;

import com.ucsmy.core.bean.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security工具类
 * Created by gaokx  on 2017/7/16.
 */
public class SpringSecurityUtils {

    private static RedisOperationsSessionRepository sessionRepository;
    private static CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public static void setSessionRepository(RedisOperationsSessionRepository sessionRepository) {
        SpringSecurityUtils.sessionRepository = sessionRepository;
    }

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getUserByContext() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return a != null && a.getPrincipal() instanceof LoginUser ? (LoginUser) a.getPrincipal() : LoginUser.builder().build();
    }

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getUserByContext(HttpServletRequest request) {
        if("org.springframework.security.web.servletapi.HttpServlet3RequestFactory$Servlet3SecurityContextHolderAwareRequestWrapper"
                .equals(request.getClass().getName())) {
            return getUserByContext();
        }
        ExpiringSession session = sessionRepository.getSession(cookieHttpSessionStrategy.getRequestedSessionId(request));
        SecurityContext context;
        Authentication authentication;
        if(session != null
                && (context = session.getAttribute(SPRING_SECURITY_CONTEXT)) != null
                && (authentication = context.getAuthentication()) != null
                && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return LoginUser.builder().build();
    }

    public static Map<String, Object> getWebUserData() {
        return getWebUserData(getUserByContext());
    }

    public static Map<String, Object> getWebUserData(HttpServletRequest request) {
        Map<String, Object> retData = getWebUserData(getUserByContext(request));
        retData.put("id", ((CsrfToken) request.getAttribute("_csrf")).getToken());
        retData.put("rsa", RSAUtils.getRsaPubKeyBySession(request.getSession()));
        return retData;
    }

    private static Map<String, Object> getWebUserData(LoginUser user) {
        Map<String, Object> retData = new HashMap<>();
        retData.put("username", user.getUsername());
        retData.put("sns", user.getRoles());
        retData.put("menus", user.getMenus());
        return retData;
    }
}
