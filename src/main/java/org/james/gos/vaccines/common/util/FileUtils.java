package org.james.gos.vaccines.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileUtils {

    public static String fileToString(String path) {
        InputStream resourceAsStream = null;
        if (Objects.equals("jar", FileUtils.class.getResource("").getProtocol())) {
            resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
        } else {
            resourceAsStream = FileUtils.class.getResourceAsStream(path);
        }

        assert Objects.nonNull(resourceAsStream);
        try {
            byte[] bs = new byte[resourceAsStream.available()];
            resourceAsStream.read(bs);
            return new String(bs, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
