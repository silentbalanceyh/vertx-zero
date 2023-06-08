package io.horizon.exception;

import io.horizon.eon.VName;
import io.horizon.eon.VString;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.eon.error.ErrorMessage;
import io.horizon.fn.HFn;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.UUID;

/**
 *
 */
public abstract class WebException extends AbstractException {
    private final String message;
    private final UUID id;
    private final Class<?> caller;
    protected HttpStatusCode status;
    private transient Object[] params;
    private String readable;

    public WebException(final String message) {
        super(message);
        this.message = message;
        this.status = HttpStatusCode.BAD_REQUEST;
        this.caller = null;      // Target;
        // readable 构造时设置
        this.readable = HUt.fromReadable(this.getCode());
        this.id = UUID.randomUUID();
    }

    public WebException(final Class<?> clazz, final Object... args) {
        super(VString.EMPTY);
        this.message = HUt.fromError(ErrorMessage.EXCEPTION_WEB, clazz, this.getCode(), args);
        this.params = args;
        this.status = HttpStatusCode.BAD_REQUEST;
        this.caller = clazz;     // Target;
        // readable 构造时设置
        this.readable = HUt.fromReadable(this.getCode(), args);
        this.id = UUID.randomUUID();
    }

    @Override
    public abstract int getCode();

    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatusCode getStatus() {
        // Default exception for 400
        return this.status;
    }

    @Override
    public Class<?> caller() {
        return this.caller;
    }

    /**
     * 设置状态和可续信息的主要目的是在于触发动态验证，如果系统有动态验证功能，那么
     * 需要重新设置状态以及可读信息，最终生成的数据格式才能通用
     */
    public void status(final HttpStatusCode status) {
        this.status = status;
    }

    public String readable() {
        return this.readable;
    }

    public void readable(final String readable) {
        HFn.runAt(() -> {
            if (Objects.isNull(this.params)) {
                this.readable = readable;
            } else {
                this.readable = HUt.fromMessage(readable, this.params);
            }
        }, readable);
    }

    /**
     * 最终格式化异常信息
     * <pre><code>
     *     {
     *         "code": "内部异常定义",
     *         "message": "系统错误信息",
     *         "info": "人工可读异常"
     *     }
     * </code></pre>
     *
     * @return {@link JsonObject}
     */
    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        // 追加UUID做追踪链
        data.put(VName.ID, this.getId().toString());
        data.put(VName.CODE, this.getCode());
        data.put(VName.MESSAGE, this.getMessage());
        if (HUt.isNotNil(this.readable)) {
            data.put(VName.INFO, this.readable);
        }
        return data;
    }

    /**
     * 针对第三方的API处理的部分 UUID 格式表
     *
     * @return 当前消息标识
     */
    public UUID getId() {
        return this.id;
    }
}
