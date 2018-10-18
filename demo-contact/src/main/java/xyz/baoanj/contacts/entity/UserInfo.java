package xyz.baoanj.contacts.entity;

import java.io.Serializable;
import java.util.Set;

// shiro+redis BUG: implements Serializable
public class UserInfo implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String salt;
    private Set<Role> roles;

    public UserInfo() {}

    public UserInfo(String userId, String username, String password, String salt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
