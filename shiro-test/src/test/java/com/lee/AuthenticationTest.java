package com.lee;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * 最简单的写死用户名密码的例子
 */
public class AuthenticationTest {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    private final String USER_NAME = "lee";
    private final String PASSWD = "123";

    /**
     * 先添加用户，simpleAccountRealm只能设置角色，不支持设置角色的权限
     */
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount(USER_NAME, PASSWD, "admin","user");
    }


    /**
     * 测试认证和授权
     */
    @Test
    public void testAuthentication() {
        //一、 1、构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2、获取security主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3、创建用户认证token
        UsernamePasswordToken token = new UsernamePasswordToken(USER_NAME, PASSWD);

        //4、主体提交(登录)认证请求
        subject.login(token);

        System.out.println("认证状态 = " + subject.isAuthenticated());

        //二、验证角色授权
        try {
            subject.checkRoles("admin","user");
            System.out.println("通过角色校验!");
        }catch (AuthorizationException e){
            e.printStackTrace();
        }

        //退出登录
        subject.logout();
        System.out.println("认证状态 = " + subject.isAuthenticated());
    }

}
