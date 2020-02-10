package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.core.utils.StringUtils;
import com.ucsmy.core.utils.TreeUtils;
import com.ucsmy.eaccount.manage.dao.ManageMenuDao;
import com.ucsmy.eaccount.manage.entity.ManageMenu;
import com.ucsmy.eaccount.manage.entity.ManageResources;
import com.ucsmy.eaccount.manage.ext.AntdPageInfo;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.service.ManageMenuService;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ManageMenuServiceImpl extends BasicServiceImpl<ManageMenu, ManageMenuDao> implements ManageMenuService {

    @Override
    public List<ManageMenu> get() {
        return TreeUtils.createTree(super.get());
    }

    @Override
    public PageInfo<ManageMenu> get(ManageMenu entity, Pageable pageable) {
        PageInfo<ManageMenu> info = new AntdPageInfo<>();
        info.setData(TreeUtils.createTree(super.get(entity)));
        return info;
    }

    @Override
    @Transactional
    public RetMsg updateResources(ManageMenu menu) {
        dao.updateResources(menu);
        return RetMsg.success("修改资源成功");
    }

    @Override
    public List<ManageMenu> getByRoleId(String roleId) {
        return TreeUtils.createTree(dao.findByRoleId(roleId));
    }

    @Override
    public void initSpringSecurityPermission(ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry urlRegistry) {
        Map<String, Set<String>> map = new HashMap<>();
        for(ManageMenu menu : dao.findMenuAndResources()) {
            if(StringUtils.isEmpty(menu.getSn()))
                continue;
            for(ManageResources resources : menu.getResources()) {
                if(!map.containsKey(resources.getUrl()))
                    map.put(resources.getUrl(), new HashSet<>());
                map.get(resources.getUrl()).add(menu.getSn());
            }
            for(Map.Entry<String, Set<String>> entry : map.entrySet()) {
                urlRegistry = ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)urlRegistry.antMatchers(entry.getKey())).hasAnyRole(entry.getValue().toArray(new String[entry.getValue().size()]));
            }
        }
        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) urlRegistry.antMatchers("/**")).authenticated();
    }
}
