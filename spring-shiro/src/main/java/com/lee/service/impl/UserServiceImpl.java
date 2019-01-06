package com.lee.service.impl;

import com.lee.bean.User;
import com.lee.dao.UserDao;
import com.lee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUserName(String userName) {
        return userDao.queryUserByUserName(userName);
    }

    @Override
    public List<String> getRoleListByUserName(String userName) {
        return userDao.queryRoleListByUserName(userName);
    }

    @Override
    public List<String> getPermissionListByRoleName(String roleName) {
        return userDao.queryPermissionListByRoleName(roleName);
    }
}
