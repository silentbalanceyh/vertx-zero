package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.exception.WebException;

/**
 * Query中的属性类型必须符合条件
 * <pre><code>
 *     {
 *         "criteria": {},
 *         "pager": {},
 *         "projection": [],
 *         "sorter": []
 *     }
 * </code></pre>
 */
public class _400QQueryAttributeException extends WebException {

    public _400QQueryAttributeException(final Class<?> clazz,
                                        final String key,
                                        final Class<?> expectedCls,
                                        final Class<?> currentCls) {
        super(clazz, key, expectedCls, currentCls);
    }

    @Override
    public int getCode() {
        return -60022;
    }

    @Development("IDE视图专用")
    private int __60022() {
        return this.getCode();
    }
}
