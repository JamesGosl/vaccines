package org.james.gos.vaccines.utils;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.common.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JwtUtils Tests
 *
 * @author James Gosl
 * @since 2023/08/15 20:05
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class JwtUtilsTests {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void createToken() {
        String token = jwtUtils.createToken(1L);
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsImNyZWF0ZVRpbWUiOjE2OTIxMDE2MDQsImV4cCI6MTY5MjEwMTYwNn0.FXj0BYTTQhKpnb10BqEtsLwqPXVFoPvLbeenDov2LOw
        log.debug(token);
    }

    @Test
    public void getUidOrNull() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsImNyZWF0ZVRpbWUiOjE2OTIxMDE2MDQsImV4cCI6MTY5MjEwMTYwNn0.FXj0BYTTQhKpnb10BqEtsLwqPXVFoPvLbeenDov2LOw";
        Long uidOrNull = jwtUtils.getAidOrNull(token);
        log.debug(uidOrNull.toString());
    }
}
