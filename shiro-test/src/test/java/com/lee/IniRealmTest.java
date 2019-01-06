package com.lee;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * 认证器-IniRealm ：读取用户信息文件认证
 */
public class IniRealmTest {

    //读取用户信息文件认证
    IniRealm iniRealm = new IniRealm("classpath:user.ini");

    /**
     * 测试认证和授权
     */
    @Test
    public void testAuthentication() {
        //一、 1、构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //2、获取security主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3、创建用户认证token
        UsernamePasswordToken token = new UsernamePasswordToken("lee", "123");

        //4、主体提交(登录)认证请求
        subject.login(token);

        System.out.println("认证状态 = " + subject.isAuthenticated());

        //二、验证角色授权
        try {
            subject.checkRoles("admin");
            System.out.println("通过角色校验!");

            //校验角色权限
            subject.checkPermissions("user:delete","user:update");
            System.out.println("通过角色权限校验!");
        }catch (AuthorizationException e){
            e.printStackTrace();
        }

        //退出登录
        subject.logout();
        System.out.println("认证状态 = " + subject.isAuthenticated());

    }

}
