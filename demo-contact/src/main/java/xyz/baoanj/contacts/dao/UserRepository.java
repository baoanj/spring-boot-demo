package xyz.baoanj.contacts.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.baoanj.contacts.entity.*;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class UserRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public UserInfo findByUsername(String username) {
        try {
            return jdbc.queryForObject(
                    "select user_id, username, password, md5_salt " +
                            "from xyz_user where username = '" + username + "'",
                    (ResultSet rs, int rowNum) -> {
                        UserInfo user = new UserInfo();
                        user.setUserId(rs.getString("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setSalt(rs.getString("md5_salt"));
                        return user;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addUser(UserInfo user) {
        jdbc.update(
                "insert into xyz_user (user_id, username, password, md5_salt)" +
                        "values (?, ?, ?, ?)",
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getSalt()
        );
    }

    public void addUserRole(String userId, String roleName) {
        jdbc.update(
                "insert into xyz_user_role (user_id, role_name) values (?, ?)",
                userId, roleName
        );
    }

    public List<Role> getRolesOfUser(String userId) {
        return jdbc.query(
                "select role_name from xyz_user_role where user_id = '" + userId + "'",
                (ResultSet rs, int rowNum) -> {
                    Role role = new Role();
                    role.setName(rs.getString(1));
                    return role;
                }
        );
    }

    public List<Permission> getPermissionsOfRole(String roleName) {
        return jdbc.query(
                "select auth_name from xyz_role_auth where role_name = '" + roleName + "'",
                (ResultSet rs, int rowNum) -> {
                    Permission permission = new Permission();
                    permission.setName(rs.getString("auth_name"));
                    return permission;
                }
        );
    }
}
