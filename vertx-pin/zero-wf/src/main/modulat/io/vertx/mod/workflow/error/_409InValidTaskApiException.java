package io.vertx.mod.workflow.error;

import cn.vertxup.workflow.cv.em.PassWay;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * Error = 80604
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409InValidTaskApiException extends WebException {
    public _409InValidTaskApiException(final Class<?> clazz, final PassWay input,
                                       final PassWay expected) {
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
