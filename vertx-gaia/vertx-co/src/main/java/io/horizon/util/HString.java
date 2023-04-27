package io.horizon.util;

import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
class HString {
    static boolean isEmpty(final String str) {
        return Objects.isNull(str) || str.trim().isEmpty();
    }
}
