package org.james.gos.vaccines.encoder;

import org.james.gos.vaccines.VaccinesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder Tests
 *
 * @author James Gosl
 * @since 2023/08/14 20:16
 */
@SpringBootTest(classes = VaccinesApplication.class)
public class PasswordEncoderTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testBCryptPasswordEncoder() {
        String password = "admin";
        String encode = passwordEncoder.encode(password);
        assert passwordEncoder.matches(password, encode);
    }
}
