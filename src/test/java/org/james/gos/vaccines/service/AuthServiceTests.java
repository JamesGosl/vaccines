package org.james.gos.vaccines.service;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.auth.service.impl.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * AuthService Tests
 *
 * @author James Gosl
 * @since 2023/08/15 16:34
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class AuthServiceTests {

    @Autowired
    AuthService authService;

    @Test
    public void getAuthAll() {
        long start = System.currentTimeMillis();
        authService.getAuthAll();
        log.debug("{}", System.currentTimeMillis() - start);

        for (int i = 0; i < 10; i++) {
            start = System.currentTimeMillis();
            authService.getAuthAll();
            log.debug("{}", System.currentTimeMillis() - start);
        }
    }

    @Test
    public void getAuth() {
        authService.getAuthAll();

        long start = System.currentTimeMillis();
        authService.getAuth(1L);
        log.debug("{}", System.currentTimeMillis() - start);

        for (int i = 0; i < 10; i++) {
            start = System.currentTimeMillis();
            authService.getAuth(1L);
            log.debug("{}", System.currentTimeMillis() - start);
        }
    }
}
