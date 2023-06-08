package io.mature.extension.error;

import io.horizon.exception.WebException;

/**
 * ## 「Error-400006」环境异常
 *
 * ### 1. 错误信息
 *
 * ```shell
 * 对不起，这是开发专用命令，不能在生产环境执行
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _501EnvironmentException extends WebException {
    public _501EnvironmentException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -81001;
    }
}
