package io.vertx.tp.atom.modeling.element;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class DataReference implements Serializable {

    private transient final String source;
    /*
     * 属性名 attr -> 字段类型
     * 引用只有两种类型：JsonArray, JsonObject（目前版本）
     *  */
    private transient final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
    /* 属性名 -> sourceConfig 配置 */
    private transient final ConcurrentMap<String, JsonObject> sourceConfig = new ConcurrentHashMap<>();
    /* 属性名 -> sourceReference 配置 */
    private transient final ConcurrentMap<String, JsonObject> sourceReference = new ConcurrentHashMap<>();

    private DataReference(final String source) {
        this.source = source;
    }

    public static DataReference create(final String tableName) {
        return new DataReference(tableName);
    }

    @Fluent
    public DataReference add(final MAttribute attribute) {
        /*
         * JsonArray / JsonObject
         */
        final Boolean isArray = attribute.getIsArray();
        final String name = attribute.getName();
        if (Objects.nonNull(isArray) && isArray) {
            this.typeMap.put(name, JsonArray.class);
        } else {
            this.typeMap.put(name, JsonObject.class);
        }
        /*
         * sourceConfig
         */
        if (Objects.nonNull(attribute.getSourceConfig())) {
            final JsonObject sourceConfig = Ut.toJObject(attribute.getSourceConfig());
            this.sourceConfig.put(name, sourceConfig);
        }
        /*
         * sourceReference
         */
        if (Objects.nonNull(attribute.getSourceReference())) {
            final JsonObject sourceReference = Ut.toJObject(attribute.getSourceReference());
            this.sourceReference.put(name, sourceReference);
        }
        return this;
    }
}
