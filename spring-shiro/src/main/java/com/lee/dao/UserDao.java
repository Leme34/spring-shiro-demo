package com.lee.dao;

import com.lee.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("select username,password from users where username = #{userName}")
    User queryUserByUserName(String userName);

    @Select("select role_name from user_roles where username = #{userName}")
    List<String> queryRoleListByUserName(String userName);

    @Select("select permission from role_permissions where role_name = #{roleName}")
    List<String> queryPermissionListByRoleName(String roleName);



}
