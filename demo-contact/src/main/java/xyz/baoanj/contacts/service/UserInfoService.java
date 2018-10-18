package xyz.baoanj.contacts.service;

import xyz.baoanj.contacts.entity.Permission;
import xyz.baoanj.contacts.entity.Role;
import xyz.baoanj.contacts.entity.UserInfo;

import java.util.Set;

public interface UserInfoService {
    public UserInfo findByUsername(String username);

    public Set<Role> getRolesOfUser(String userId);

    public Set<Permission> getPermissionsOfRole(String roleName);

    public void insertUser(String username, String password, String role);
}
