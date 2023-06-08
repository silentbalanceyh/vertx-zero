package io.horizon.specification.typed;

import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

/**
 * 「Json序列化规范」
 * <hr/>
 * 针对 JsonObject 专用的接口，用于实现 JsonObject 和 T 类型的互相转换，转换分两个级别
 * <pre><code>
 *     1. 内存级：toJson / fromJson，直接将内存中的字符串字面量和当前对象进行类型转换
 *     2. 文件级：文件级只支持从文件系统读取和加载数据文件，直接将路径中的 json 文件读取
 *        到内存中，并且实现到对象的转换。
 * </code></pre>
 * 默认场景下，该接口中从文件加载数据会直接调用 {@link HUt#ioJObject} 静态方法，该静态
 * 方法会检索文件路径，然后读取相关内容，最终加载到当前对象中。
 *
 * @author lang
 */
public interface TJson {
    /**
     * 将当前对象、组件或实现转换成 {@link JsonObject}
     *
     * @return {@link JsonObject}
     */
    JsonObject toJson();

    /**
     * 将传入的 {@link JsonObject} 对象中的数据加载到环境中填充当前对象
     *
     * @param json 输入的 {@link JsonObject} 类型对象
     */
    void fromJson(JsonObject json);

    /**
     * 「重载方法」直接从文件系统加载数据文件，然后填充到当前对象。
     * 默认实现调用了 {@link HUt#ioJObject} 静态方法，该静态方法会检索文件路径，然后读取相关内容，
     * 转换流程如：
     * <pre><code>
     *     File -> JsonObject -> T
     * </code></pre>
     *
     * @param jsonFile 输入的文件路径
     */
    default void fromFile(final String jsonFile) {
        final JsonObject data = HUt.ioJObject(jsonFile);
        this.fromJson(data);
    }
}
