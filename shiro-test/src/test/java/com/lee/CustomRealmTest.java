package com.lee;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

    /**
     * 测试认证和授权
     */
    @Test
    public void testAuthentication() {
        //创建自定义realm对象
        CustomRealm customRealm = new CustomRealm();
        //对明文密码加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");  //使用md5算法
        matcher.setHashIterations(1);         //使用算法1次加密
        customRealm.setCredentialsMatcher(matcher);

        //一、 1、构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //2、获取security主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3、创建用户认证token
        UsernamePasswordToken token = new UsernamePasswordToken("lee", "123");

        //4、主体提交(登录)认证请求
        subject.login(token);

        System.out.println("认证状态 = " + subject.isAuthenticated());

        //二、验证角色和权限
        try {
            subject.checkRoles("admin", "user");
            System.out.println("通过角色校验!");

            //校验角色权限
            subject.checkPermissions("user:insert", "user:update");
            System.out.println("通过角色权限校验!");
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }

        //退出登录
        subject.logout();
        System.out.println("认证状态 = " + subject.isAuthenticated());

    }

}