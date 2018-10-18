package xyz.baoanj.contacts.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import xyz.baoanj.contacts.entity.Permission;
import xyz.baoanj.contacts.entity.Role;
import xyz.baoanj.contacts.entity.UserInfo;
import xyz.baoanj.contacts.service.UserInfoService;

import javax.annotation.Resource;
import java.util.Set;

public class ContactShiroRealm extends AuthorizingRealm {
    @Resource
    private UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();

        Set<Role> roles = userInfoService.getRolesOfUser(userInfo.getUserId());
        for (Role role : roles) {
            Set<Permission> permissions = userInfoService.getPermissionsOfRole(role.getName());
            role.setPermissions(permissions);
        }

        for (Role role : roles) {
            authorizationInfo.addRole(role.getName());
            for (Permission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getName());
            }
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        UserInfo user = userInfoService.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException();
        }

        // shiro+redis BUG: user.setRoles(roles);
        // 估计是不能序列化这种复杂的结构
//        Set<Role> roles = userInfoService.getRolesOfUser(user.getUserId());
//        for (Role role : roles) {
//            Set<Permission> permissions = userInfoService.getPermissionsOfRole(role.getName());
//            role.setPermissions(permissions);
//        }
//        user.setRoles(roles);

        ByteSource salt = ByteSource.Util.bytes(user.getSalt());

        // 每次用户进行login操作时，就会调用doGetAuthenticationInfo方法，
        // Shiro就自动帮我们校验账户密码是否匹配。
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }
}
