package org.james.gos.vaccines.mapper;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.auth.doman.entity.Auth;
import org.james.gos.vaccines.auth.mapper.AuthMapper;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthMapper Tests
 *
 * @author James Gosl
 * @since 2023/08/14 23:36
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class AuthMapperTests {

    @Autowired
    private AuthMapper authMapper;

    @Test
    public void insert() {
        List<Auth> authList = new ArrayList<>();
        authList.add(new Auth().setId(1L).setAuth(AuthEnum.ADMIN.getAuth()).setDescription(AuthEnum.ADMIN.getDesc()));
        authList.add(new Auth().setId(2L).setAuth(AuthEnum.DOCTOR.getAuth()).setDescription(AuthEnum.DOCTOR.getDesc()));
        authList.add(new Auth().setId(3L).setAuth(AuthEnum.USER.getAuth()).setDescription(AuthEnum.USER.getDesc()));

        // 扩展的MyBatis 不支持TxMapper 的insertSelective 方法，或者说我懒得扩展了
        authList.forEach(authMapper::insert);
    }

    @Test
    public void insertSelective() {
        Auth auth = new Auth().setId(1L).setAuth(AuthEnum.ADMIN.getAuth());
        assert 1 == authMapper.insertSelective(auth);
    }

    @Test
    public void updateByPrimaryKeySelective() {
        Auth auth = new Auth().setId(1L).setAuth(AuthEnum.ADMIN.getAuth()).setDescription(AuthEnum.ADMIN.getDesc());
        assert 1 == authMapper.updateByPrimaryKeySelective(auth);
    }

    @Test
    public void selectAll() {
        // ZWSP 零宽字符，在写数据库字段的时候尽量手写，复制可能就会出现这些问题
        // SELECT ​create_time FROM auth
        // SELECT create_time FROM auth

        List<Auth> auths = authMapper.selectAll();
        auths.forEach(auth -> log.debug(auth.toString()));
    }

    @Test
    public void selectByKey() {
        Auth auth = authMapper.selectByPrimaryKey(1L);
    }

    @Test
    public void select() {
        Auth auth = new Auth().setId(1L);
        authMapper.select(auth);
    }

    @Test
    public void selectEx() {
        Example example = new Example(Auth.class);
        example.createCriteria().orEqualTo("id", 1L);
        List<Auth> auths = authMapper.selectByExample(example);
    }

    @Test
    public void update() {
        Auth auth = new Auth().setId(1L).setAuth(AuthEnum.ADMIN.getAuth()).setDescription(AuthEnum.ADMIN.getDesc());
        authMapper.updateByPrimaryKeySelective(auth);
    }

    @Test
    public void delete() {
        Auth auth = new Auth().setId(1L).setAuth(AuthEnum.ADMIN.getAuth());
        authMapper.delete(auth);
    }

    @Test
    public void deleteByPrimaryKey() {
        authMapper.deleteByPrimaryKey(1L);
    }

    @Test
    public void logger() {
        log.debug("Hello World");
    }
}
