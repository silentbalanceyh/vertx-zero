package io.vertx.tp.atom.modeling.reference;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DataQuote implements Serializable {

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

    /* 规则集 -> sourceReference 中拆分 */
    private transient final ConcurrentMap<String, DataQRule> sourceRule = new ConcurrentHashMap<>();

    private DataQuote(final String source) {
        this.source = source;
    }

    public static DataQuote create(final String tableName) {
        return new DataQuote(tableName);
    }

    @Fluent
    public DataQuote add(final MAttribute attribute) {
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
            /*
             * JsonObject rule
             */
            final JsonObject ruleData = Ut.sureJObject(sourceReference.getJsonObject(KeField.RULE));
            if (Ut.notNil(ruleData)) {
                final DataQRule rule = Ut.deserialize(ruleData, DataQRule.class);
                this.sourceRule.put(name, rule.type(this.typeMap.get(name)));
            }
        }
        return this;
    }

    public ConcurrentMap<String, DataQRule> rules() {
        return this.sourceRule;
    }

    public Class<?> type(final String field) {
        return this.typeMap.getOrDefault(field, null);
    }
}
