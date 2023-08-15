package org.james.gos.vaccines.auth.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.auth.doman.entity.Auth;
import org.james.gos.vaccines.auth.mapper.AuthMapper;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限信息缓存
 *
 * @author James Gosl
 * @since 2023/08/15 15:39
 */
@Component
@Slf4j
public class AuthCache {

    @Resource
    private AuthMapper authMapper;

    /**
     * 获取所有权限信息
     *
     * @return 二级缓存，数据库
     */
    @Cacheable(cacheNames = CacheKey.AUTH, key =  "'authList'")
    public List<Auth> getAuthAll() { //不添加二级缓存
        return authMapper.selectAll();
    }

    /**
     * 根据ID 获取权限信息
     */
    @Cacheable(cacheNames = CacheKey.AUTH, key =  "'byId-' + #id")
    public Auth getAuth(Long id) {
        log.debug("未击中缓存-{}", id);

        List<Auth> auths = CacheUtils.get(CacheKey.AUTH, "authList");

        Auth auth = null;
        if (auths != null) {
            auth = auths.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        }
        return auth == null ? authMapper.selectByPrimaryKey(id) : auth;
    }

    /**
     * 根据auth 获取权限信息
     */
    @Cacheable(cacheNames = CacheKey.AUTH, key =  "'byAuth-' + #auth")
    public Auth getAuth(Integer auth) {
        log.debug("未击中缓存-{}", auth);

        List<Auth> auths = CacheUtils.get(CacheKey.AUTH, "authList");

        if (auths != null) {
            return auths.stream().filter(a -> a.getAuth().equals(auth)).findFirst().orElse(null);
        }

        Example example = new Example(Auth.class);
        example.createCriteria().andEqualTo("auth", auth);
        return authMapper.selectOneByExample(example);
    }
}
