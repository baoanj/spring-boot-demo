package xyz.baoanj.contacts.service;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.baoanj.contacts.dao.UserRepository;
import xyz.baoanj.contacts.entity.Permission;
import xyz.baoanj.contacts.entity.Role;
import xyz.baoanj.contacts.entity.UserInfo;

import java.util.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserInfo findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Set<Role> getRolesOfUser(String userId) {
        List<Role> roleList = userRepository.getRolesOfUser(userId);
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roleList) {
            roleSet.add(role);
        }
        return roleSet;
    }

    @Override
    public Set<Permission> getPermissionsOfRole(String roleName) {
        List<Permission> permissionList = userRepository.getPermissionsOfRole(roleName);
        Set<Permission> permissionSet = new HashSet<>();
        for (Permission permission : permissionList) {
            permissionSet.add(permission);
        }
        return permissionSet;
    }

    public void insertUser(String username, String password, String role) {
        // 生成uuid
        String uuid = UUID.randomUUID().toString();

        String salt = "yan" + new Date().getTime();
        ByteSource saltByteSource = ByteSource.Util.bytes(salt);

        /**
         * MD5加密：
         * 使用SimpleHash类对原始密码进行加密
         * 第一个参数代表使用MD5方式加密
         * 第二个参数为原始密码
         * 第三个参数为盐值
         * 第四个参数为加密次数
         * 最后用toHex()方法将加密后的密码转成String
         * */
        String safePass = new SimpleHash("MD5", password, saltByteSource, 10).toHex();

        UserInfo user = new UserInfo();
        user.setUserId(uuid);
        user.setUsername(username);
        user.setPassword(safePass);
        user.setSalt(salt);

        userRepository.addUser(user);
        userRepository.addUserRole(uuid, role);
    }
}
