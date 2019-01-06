package com.lee.service;

import com.lee.bean.User;

import java.util.List;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     */
    User getUserByUserName(String userName);

    /**
     * 根据用户名查询数据库中此用户的角色
     */
    List<String> getRoleListByUserName(String userName);

    /**
     * 根据用户名查询数据库中此用户的角色权限
     */
    List<String> getPermissionListByRoleName(String roleName);

}
