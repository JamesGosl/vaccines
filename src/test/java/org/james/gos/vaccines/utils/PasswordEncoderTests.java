package org.james.gos.vaccines.utils;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderTests
 *
 * @author James Gosl
 * @since 2023/08/15 21:00
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class PasswordEncoderTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String password = "admin";

    @Test
    public void encode() {
        String encode = passwordEncoder.encode(password);
        log.debug(encode);
    }

    @Test
    public void decode() {
        assert passwordEncoder.matches(password, "$2a$04$zIDPfj4c7V/LgjaifW3Hae5mMEXAS7kUXQ8/flXTstsUdiklNTs0y");
    }
}
