package org.james.gos.vaccines.enums;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * RedisChannelEnumTests
 *
 * @author James Gosl
 * @since 2023/08/18 13:51
 */
@Slf4j
public class RedisChannelEnumTests {

    @Test
    public void test() {
        RedisChannelEnum of = RedisChannelEnum.of("123".getBytes(StandardCharsets.UTF_8));
        System.out.println(of);
    }
}
