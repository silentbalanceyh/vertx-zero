package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * 解析过程中遇到了 Null 类型的元数据
 */
public class _500QQueryMetaNullException extends WebException {

    public _500QQueryMetaNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60024;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }


    @Development("IDE视图专用")
    private int __60024() {
        return this.getCode();
    }
}
