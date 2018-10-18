package xyz.baoanj.contacts.config.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session
 */
@Component
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

    // session 在redis过期时间是30分钟 30*60
    private static int expireTime = 1800;

    private static String prefix = "contact:shiro:session:";

    @Resource(name = "redisTemplateShiro")
    private RedisTemplate<String, Object> redisTemplate;

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        try {
            redisTemplate.opsForValue().set(prefix + sessionId, session,
                    expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sessionId;
    }

    /**
     * 重写CachingSessionDAO中readSession方法，
     * 如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = getCachedSession(sessionId);
        if (session == null ||
                session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
            session = this.doReadSession(sessionId);
            if (session == null) {
                throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
            } else {
                // 缓存
                cache(session, session.getId());
            }
        }
        return session;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = null;
        try {
            session = (Session) redisTemplate.opsForValue().get(prefix + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        try {
            String key = prefix + session.getId();
            redisTemplate.opsForValue().set(key, session, expireTime, TimeUnit.SECONDS);
//            if (!redisTemplate.hasKey(key)) {
//                redisTemplate.opsForValue().set(key, session);
//            }
//            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        redisTemplate.delete(prefix + session.getId());
    }
}
