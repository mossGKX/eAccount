package com.ucsmy.core.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by ucs_gaokx on 2017/7/12.
 */
@Data
public class LoginUser extends BaseNode implements UserDetails {
    private String username;
    private String password;
    private Byte status;
    private long lastLoginTime;
    private List<MenuTree> menus;
    private Set<String> roles;

    @Builder
    public LoginUser(String id, String username, String password, Byte status, List<MenuTree> menus, Set<String> roles) {
        super(id);
        this.username = username;
        this.password = password;
        this.status = status;
        this.lastLoginTime = System.currentTimeMillis();
        this.menus = menus;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(p -> list.add(new SimpleGrantedAuthority("ROLE_".concat(p))));
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 0;
    }
}
