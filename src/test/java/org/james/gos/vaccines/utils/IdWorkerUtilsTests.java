package org.james.gos.vaccines.utils;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.common.util.IdWorkerUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分布式ID 解决方案
 *
 * @author James Gosl
 * @since 2023/08/16 10:55
 */
@Slf4j
@SpringBootTest(classes = VaccinesApplication.class)
public class IdWorkerUtilsTests {

    @Test
    public void genId() {
        for (int i = 0; i < 10; i++) {
            log.debug("{}", IdWorkerUtils.getId());
        }
    }
}
