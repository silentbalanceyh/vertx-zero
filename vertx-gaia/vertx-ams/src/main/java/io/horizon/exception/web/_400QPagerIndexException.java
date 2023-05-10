package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.exception.WebException;

/**
 * pager 参数中的 index 非法，实际是
 * <pre><code>
 *     {
 *         "page": xx,
 *         "size": xx
 *     }
 * </code></pre>
 * 格式中的 page 参数必须 > 1
 */
public class _400QPagerIndexException extends WebException {

    public _400QPagerIndexException(final Class<?> clazz,
                                    final Integer page) {
        super(clazz, page);
    }

    @Override
    public int getCode() {
        return -60025;
    }

    @Development("IDE视图专用")
    public int __60025() {
        return this.getCode();
    }
}
