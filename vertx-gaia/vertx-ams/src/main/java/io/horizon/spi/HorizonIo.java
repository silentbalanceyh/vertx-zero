package io.horizon.spi;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;

/**
 * 资源文件加载专用SPI模式
 * - 日志器：Annal 加载，HLogger 是高阶实现，Annal 为默认实现
 * - 资源加载器：
 * --- spring 中加载 application-error.yml
 * --- vertx zero 中加载 vertx-error.yml
 * - 最终实现完整加载流程
 * 该组件SPI为底层资源加载组件，用于如下作用
 * 1. 对接不同的 Annal 扩展组件，实现日志器的替换扩展流程。
 * 2. 对接错误信息的资源提取流程，提取错误信息专用，构造成一个JsonObject包含所有资源类错误信息定义。
 *
 * @author lang : 2023/4/28
 */
public interface HorizonIo {
    /**
     * 资源加载，加载对应的异常资源文件，内部实现可自定义
     *
     * @return {@link JsonObject}
     */
    JsonObject ofError();

    /**
     * 日志获取器，可读取扩展日志类型，实例时基于 Class<?>
     *
     * @return {@link Annal}
     */
    default Class<?> ofAnnal() {
        return null;
    }
}
