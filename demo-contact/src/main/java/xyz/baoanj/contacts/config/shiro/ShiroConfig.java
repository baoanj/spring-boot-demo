package xyz.baoanj.contacts.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Resource
    private RedisSessionDAO redisSessionDAO;

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 自定义拦截器
        Map<String, Filter> myFilters = new LinkedHashMap();
        myFilters.put("contact", new ContactAuthorizationFilter());
        myFilters.put("login", new LoginAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(myFilters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // filterChainDefinitions过滤器中对于路径的配置是有顺序的，
        // 当找到匹配的条目之后容器不会再继续寻找，因此带有通配符的路径要放在后面。
        filterChainDefinitionMap.put("/contact/login", "anon");
        filterChainDefinitionMap.put("/contact/regist", "anon");
        filterChainDefinitionMap.put("/contact/createWithFile", "login,contact[contact:create]");
        filterChainDefinitionMap.put("/contact/list", "login,contact[contact:query]");
        filterChainDefinitionMap.put("/contact/**", "login");
        filterChainDefinitionMap.put("/ws/**", "login");
        filterChainDefinitionMap.put("/**", "login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    // 配置核心安全事务管理器
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(contactShiroRealm());
        // 自定义缓存实现, 使用redis
        securityManager.setCacheManager(redisCacheManager());
        // 自定义session管理, 使用redis
        securityManager.setSessionManager(sessionManager());
        // TODO 注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    // 配置自定义的权限登录器
    @Bean
    public ContactShiroRealm contactShiroRealm() {
        ContactShiroRealm realm = new ContactShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setCacheManager(redisCacheManager());
        sessionManager.setGlobalSessionTimeout(-1);
        return sessionManager;
    }

    // 密码匹配凭证管理器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 采用MD5方式加密
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 设置加密次数
        hashedCredentialsMatcher.setHashIterations(10);
        return hashedCredentialsMatcher;
    }

    /**
     * cookie对象
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天, 单位秒 -->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象; 记住我功能
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
}
