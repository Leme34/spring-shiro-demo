package com.lee.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;

/**
 * 自定义的AuthorizationFilter
 * 只需要该用户有一个角色 匹配 配置文件中允许访问的角色数组中的一个 则放行，否则跳转unauthorizedUrl
 */
public class AnyRolesFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        //取得请求登录的主体
        Subject subject = getSubject(request, response);
        //配置文件中允许访问的角色数组，例如：/testRole2 = anyRoles["demoRole","user"]
        String[] roles = (String[]) mappedValue;
        //若没有指定允许访问的角色数组,则全部放行
        if (roles == null || roles.length == 0) {
            return true;
        }
        //若用户的角色有一个 匹配 允许访问的角色数组中的一个，则放行，否则过滤
        return Arrays.stream(roles).anyMatch(r->subject.hasRole(r));
    }
}
