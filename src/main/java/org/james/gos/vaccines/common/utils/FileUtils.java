package org.james.gos.vaccines.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String fileToString(String path) {
        InputStream resourceAsStream = null;
        if (FileUtils.class.getResource("").getProtocol().equals("jar")) {
            resourceAsStream = FileUtils.class.getResourceAsStream(path);
        }
        resourceAsStream = FileUtils.class.getResourceAsStream(path);

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
