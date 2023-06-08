package io.mature.extension.error;

import io.horizon.exception.WebException;

/**
 * ##「Error-400005」不支持异常
 *
 * ### 1. 错误信息
 *
 * ```shell
 * 对不起，当前环境参数（{0}）不被支持，请输入其他环境参数
 * ```
 *
 * ### 2. 此处参数
 *
 * - prod：生产环境专用命令。
 * - dev：开发环境专用命令。
 * - home：本地环境专用命令。
 * - zw：招为云环境。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _400EnvUnsupportException extends WebException {
    public _400EnvUnsupportException(final Class<?> clazz, final String env) {
        super(clazz, env);
    }

    @Override
    public int getCode() {
        return -81000;
    }
}
