package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.eaccount.manage.entity.ManageMenu;
import com.ucsmy.core.vo.RetMsg;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.List;

public interface ManageMenuService extends BasicService<ManageMenu> {
    RetMsg updateResources(ManageMenu menu);

    List<ManageMenu> getByRoleId(String roleId);

    /**
     * 初始化SpringSecurity权限数据
     */
    void initSpringSecurityPermission(ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry urlRegistry);
}
