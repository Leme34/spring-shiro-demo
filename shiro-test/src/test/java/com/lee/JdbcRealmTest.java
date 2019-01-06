package com.lee;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 认证器-JdbcRealmTest ：读取数据库进行认证
 */
public class JdbcRealmTest {

    //创建数据源
    private DruidDataSource dataSource = new DruidDataSource();
    //初始化Druid数据源，非静态语句代码块（静态代码块--》非静态代码块--》构造方法），每new一次就执行一次
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/yueqian2");
        dataSource.setUsername("root");
        dataSource.setPassword("***");
    }

    /**
     * 测试认证和授权
     */
    @Test
    public void testAuthentication() {
        //设置数据源，使用JdbcRealm提供的默认查询语句
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);

        //一、 1、构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2、获取security主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3、创建用户认证token
        UsernamePasswordToken token = new UsernamePasswordToken("lee", "123");

        //4、主体提交(登录)认证请求
        subject.login(token);

        System.out.println("认证状态 = " + subject.isAuthenticated());

        //二、验证角色授权
        //必须开启权限认证才能使用默认sql语句查询数据库的权限信息
        jdbcRealm.setPermissionsLookupEnabled(true);
//        try {
//            subject.checkRoles("admin");
//            System.out.println("通过角色校验!");
//
//            //校验角色权限
//            subject.checkPermissions("user:delete","user:update");
//            System.out.println("通过角色权限校验!");
//        }catch (AuthorizationException e){
//            e.printStackTrace();
//        }

        //退出登录
        subject.logout();
        System.out.println("认证状态 = " + subject.isAuthenticated());

    }

}
