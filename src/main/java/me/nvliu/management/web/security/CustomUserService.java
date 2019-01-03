package me.nvliu.management.web.security;

import me.nvliu.management.web.dao.MenuMapper;
import me.nvliu.management.web.dao.UserMapper;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userMapper.findByUserName(username);
        if (user != null) {
            List<Menu> permissions = menuMapper.getByUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Menu permission : permissions) {
                if (permission != null && permission.getName()!=null) {
                    GrantedAuthority grantedAuthority = new UrlGrantedAuthority(permission.getUrl());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            user.setAuthorities(grantedAuthorities);
            return user;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }
}
