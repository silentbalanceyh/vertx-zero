package io.modello.atom.normalize;

import io.horizon.eon.VString;
import io.horizon.util.HUt;
import io.modello.eon.em.AttributeMarker;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 旧系统在使用的属性标记，标记当前属性在业务场景下的特殊行为，辅助属性完成完整的属性解析
 * 每种标记都是可配置的模式：
 * <pre><code>
 *     1. 基于数据库配置
 *     2. 基于文件配置
 *     3. 动态配置
 * </code></pre>
 * 属性标记可直接序列化成带逗号的字符串，使用字符串的目的是减少配置数量，提升配置效率
 * 根据构造函数支持的格式：
 * <pre><code>
 *     1. 逗号分隔的字符串（注字面量中索引位置会转换成Boolean）
 *        0,1,2,3,4,5,6,7
 *     2. JsonObject，直接使用属性处理，其他的使用默认值
 *        {
 *            "active": true,
 *            "track": true
 *        }
 *     3. JsonArray，转换成字符串中的集合处理
 * </code></pre>
 * 升级到新版之后，采用属性标记的通用方法，从外部传入标记名，于是 {@link KMarkAttribute} 就自然转变成了标记容器。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KMarkAttribute {
    private final ConcurrentMap<String, Boolean> marks = new ConcurrentHashMap<>();

    private KMarkAttribute(final List<String> literal) {
        // X,X,X,X,X,X,X,X
        this.marks.putAll(MarkUtil.parse(literal));
    }

    private KMarkAttribute(final JsonObject inputJ) {
        this.marks.putAll(MarkUtil.parse(inputJ));
    }

    // -------------------- 静态构造方法 ---------------------
    public static KMarkAttribute of() {
        return new KMarkAttribute((List<String>) null);
    }

    public static KMarkAttribute of(final JsonObject inputJ) {
        return new KMarkAttribute(inputJ);
    }

    public static KMarkAttribute of(final String literal) {
        final String[] parsed = literal.split(VString.COMMA);
        return new KMarkAttribute(List.of(parsed));
    }

    public static KMarkAttribute of(final JsonArray arrayA) {
        final List<String> literal = HUt.toList(arrayA);
        return new KMarkAttribute(literal);
    }

    // -------------------- 属性提取 ---------------------
    public boolean value(final AttributeMarker marker) {
        Objects.requireNonNull(marker);
        return MarkUtil.value(marker.name(), this.marks);
    }

    @Override
    public String toString() {
        return MarkUtil.toString(this.getClass().getName(), this.marks);
    }
}
