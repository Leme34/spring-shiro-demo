package com.lee;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Realm
 */
public class CustomRealm extends AuthorizingRealm {
    //加密盐值
    public static final String SALT = "ad123das0-idn";

    //模拟数据库中的数据
    private Map<String, String> userMap = new HashMap<>();
    //自定义realm名称
    private final String REALM_NAME = "customRealm";

    {
        //设置自定义realm名称
        super.setName(REALM_NAME);
        //先添加用户信息,存入md5加密后的密码
        Md5Hash md5Hash = new Md5Hash("123",CustomRealm.SALT);
        userMap.put("lee", md5Hash.toString());
    }

    /**
     * 授权逻辑
     *
     * @param principals 用户认证信息
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1、从用户认证信息中获取用户名
        String userName = (String) principals.getPrimaryPrincipal();
        //2、根据用户名查询数据库或缓存中此用户的角色
        Set<String> roleSet = getRoleSetByUserName(userName);
        //3、根据用户名查询数据库或缓存中此用户的角色权限
        Set<String> permissionSet = getPermissionSetByUserName(userName);

        //返回根据用户名从数据库查出的角色和权限new出来的AuthorizationInfo
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissionSet);
        authorizationInfo.setRoles(roleSet);
        return authorizationInfo;
    }

    //模拟 根据用户名查询数据库或缓存中此用户的角色
    private Set<String> getRoleSetByUserName(String userName){
        Set<String> roleSet = new HashSet<>();
        //模拟数据库查出的权限
        roleSet.add("admin");
        roleSet.add("user");
        return roleSet;
    }

    //模拟 根据用户名查询数据库或缓存中此用户的角色权限
    private Set<String> getPermissionSetByUserName(String userName) {
        Set<String> permissionSet = new HashSet<>();
        permissionSet.add("user:insert");
        permissionSet.add("user:update");
        return permissionSet;
    }

    /**
     * 认证逻辑
     *
     * @param token 主体传入的认证信息
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1、从主体传入的认证信息中获取用户名
        String userName = (String) token.getPrincipal();
        //2、查询数据库密码
        String password = getPasswordByUserName("lee");
        //若密码为空(用户名不存在),则返回的AuthenticationInfo为空
        if (password == null) {
            return null;
        }
        //3、返回数据库查出的用户名密码new出来的AuthenticationInfo
        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(userName, password, REALM_NAME);
        //设置加密盐值
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(CustomRealm.SALT));
        return authenticationInfo;
    }

    //模拟 根据用户名查询数据库中的密码
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }
}
