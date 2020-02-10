package com.ucsmy.eaccount.config;

import com.ucsmy.core.bean.LoginUser;
import com.ucsmy.core.bean.MenuTree;
import com.ucsmy.core.tool.captcha.CaptchaService;
import com.ucsmy.core.utils.*;
import com.ucsmy.eaccount.config.properties.UcsmyProperties;
import com.ucsmy.eaccount.manage.entity.ManageMenu;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageMenuService;
import com.ucsmy.eaccount.manage.service.ManageUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Spring Security配置
 * Created by gaokx
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERNAME = "username";
    private static final String LOGIN_URL = "/login";
    private static final String ERROR_MSG = "用户名或密码错误，登陆失败";
    private static final String ERROR_CAPTCHA = "验证码错误";
    private final static String LOGIN_ERROR = "ucsmy:manage:system:login:error:num:";// 记录登陆错误次数key

    @Autowired
    private ManageUserAccountService userAccountService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private SessionProperties sessionProperties;

    @Autowired
    private UcsmyProperties properties;

    @Autowired
    private ManageMenuService menuService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry urlRegistry =
                http.cors().and().authorizeRequests()
                        .antMatchers("/resources/**", "/index.js*", "/login*", "/403*", "/captcha*")
                        .permitAll();

        // 注册资源
        menuService.initSpringSecurityPermission(urlRegistry);

        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) urlRegistry.antMatchers("/**")).authenticated();

        // 登陆
        http.formLogin()
                .loginPage(LOGIN_URL)
                .successHandler(loginSuccess())
                .failureHandler(loginFailure())
                .permitAll();

        // 推出登陆
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl(properties.getRedirect().getApiUrl() + LOGIN_URL)
                .and()
                .httpBasic();

        http.exceptionHandling().accessDeniedHandler(accessDenied());

        // 必须配置，才能获取登录用户信息
        http.sessionManagement()
                .maximumSessions(Integer.MAX_VALUE)
                .sessionRegistry(sessionRegistry(null))
                .expiredUrl(properties.getRedirect().getApiUrl() + LOGIN_URL);

        super.configure(http);
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccess() {
        return (request, response, authentication) -> {
            // 清理错误次数
            String username = request.getParameter(USERNAME);
            RedisUtils.delete(getLoginErrorKey(username));

            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> retData = SpringSecurityUtils.getWebUserData();
            retData.put("id", ((CsrfToken) request.getAttribute("_csrf")).getToken());
            retData.put("rsa", RSAUtils.getRsaPubKeyBySession(request.getSession()));
            response.getWriter().write(JsonUtils.formatObjectToJson(RetMsg.success(retData)));
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailure() {
        return (request, response, authentication) -> {
            String username = request.getParameter(USERNAME);
            RetMsg retMsg = RetMsg.error(authentication.getMessage());
            // 登陆失败3次以上开启验证码
            if(StringUtils.isNotEmpty(username) && RedisUtils.incr(getLoginErrorKey(username), 30) > 2) {
                retMsg.setCode(1);
            }
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.formatObjectToJson(retMsg));
        };
    }

    @Bean
    public AccessDeniedHandler accessDenied() {
        return (request, response, accessDeniedException) -> {
            if (!response.isCommitted()) {
                if(StringUtils.isNotEmpty(request.getContentType()) &&
                        MediaType.valueOf(request.getContentType()).includes(MediaType.TEXT_HTML)) {
                    response.sendRedirect(properties.getRedirect().getPageUrl() + "/403");
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            accessDeniedException.getMessage());
                }
            }
        };
    }

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry(RedisOperationsSessionRepository sessionRepository) {
        SpringSecurityUtils.setSessionRepository(sessionRepository);
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) sessionRepository);
    }

    @Bean
    public AbstractUserDetailsAuthenticationProvider loginAuthenticationProvider() {
        return new AbstractUserDetailsAuthenticationProvider() {

            @Override
            protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                // 密码校验放在retrieveUser方法上校验
            }

            @Override
            @Transactional
            protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

                if (StringUtils.isEmpty(username))
                    throw new BadCredentialsException(ERROR_MSG);

                // 图片验证码校验
                Integer errorNum = RedisUtils.get(getLoginErrorKey(username));
                if(errorNum != null && errorNum > 2 && !captchaService.checkCaptcha(request)) {
                    throw new BadCredentialsException(ERROR_CAPTCHA);
                }

                if (authentication.getCredentials() == null)
                    throw new BadCredentialsException(ERROR_MSG);
                String password = authentication.getCredentials().toString();
                if (StringUtils.isEmpty(password))
                    throw new BadCredentialsException(ERROR_MSG);
                // 密码解密
                RetMsg<String> retMsg;
                if(!(retMsg = RSAUtils.decryptBySession(password)).isSuccess() || StringUtils.isEmpty((password = retMsg.getData())))
                    throw new BadCredentialsException(ERROR_MSG);

                // 获取用户及匹配密码
                ManageUserAccount user = userAccountService.getByUserName(username);
                if(user == null)
                    throw new BadCredentialsException(ERROR_MSG);
                if (!StringUtils.passwordEncrypt(user.getAccount(), password, user.getSalt()).equals(user.getPassword()))
                    throw new BadCredentialsException(ERROR_MSG);

                // 生成权限/菜单数据
                Set<String> roles = new HashSet<>();
                List<ManageMenu> manageMenus;
                if("admin".equals(user.getAccount())) {
                    manageMenus = menuService.get();
                } else if(user.getRole() == null) {
                    manageMenus = new ArrayList<>();
                } else {
                    manageMenus = menuService.getByRoleId(user.getRole().getId());
                }
                return LoginUser.builder()
                        .id(user.getId())
                        .username(user.getAccount())
                        .status(user.getStatus())
                        .menus(buildMenu(manageMenus, roles))
                        .roles(roles)
                        .build();
            }
        };
    }

    // 创建菜单
    private static List<MenuTree> buildMenu(List<ManageMenu> manageMenus, Set<String> snsSet) {
        List<MenuTree> menus = new ArrayList<>();
        manageMenus.forEach(module -> {
            if(StringUtils.isNotEmpty(module.getSn()))
                snsSet.add(module.getSn());
            if(StringUtils.isNotEmpty(module.getIcon())) {
                menus.add(MenuTree.builder()
                        .id(module.getId())
                        .label(module.getLabel())
                        .icon(module.getIcon())
                        .path(module.getPath())
                        .children(module.getChildren() == null ? new ArrayList<>() : buildMenu(module.getChildren(), snsSet))
                        .build());
            }
        });
        return menus;
    }

    private String getLoginErrorKey(String username) {
        String namespace = sessionProperties.getRedis().getNamespace();
        if(StringUtils.isEmpty(namespace)) {
            return LOGIN_ERROR.concat(username);
        } else {
            return LOGIN_ERROR.concat(sessionProperties.getRedis().getNamespace()).concat(":").concat(username);
        }
    }
}
