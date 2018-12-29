package me.nvliu.management.web.service.impl;

import me.nvliu.management.web.dao.UserMapper;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserMapper userMapper;
    @Override
    public User getUserById(Integer id) {
        return null;
    }
}
