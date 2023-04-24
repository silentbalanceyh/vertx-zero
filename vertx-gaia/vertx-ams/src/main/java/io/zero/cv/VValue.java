package io.zero.cv;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lang : 2023/4/24
 */
public interface VValue {
    interface Encoding {
        String UTF_8 = "UTF-8";
        String ISO_8859_1 = "ISO-8859-1";
    }

    interface DFT {
        /**
         * 默认 byte[] 数组的构造尺寸 8192 字节
         */
        int SIZE_BYTE_ARRAY = 8 * 1024;

        Charset CHARSET = StandardCharsets.UTF_8;
    }
}
