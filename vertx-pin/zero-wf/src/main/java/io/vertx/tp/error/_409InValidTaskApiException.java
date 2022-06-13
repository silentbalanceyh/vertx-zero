package io.vertx.tp.error;

import cn.zeroup.macrocosm.cv.em.NodeType;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * Error = 80604
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409InValidTaskApiException extends WebException {
    public _409InValidTaskApiException(final Class<?> clazz, final NodeType input,
                                       final NodeType expected) {
        super(clazz, input, expected);
    }

    @Override
    public int getCode() {
        return -80610;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
