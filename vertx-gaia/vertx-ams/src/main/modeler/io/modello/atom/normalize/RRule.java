package io.modello.atom.normalize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.VString;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 引用规则定义，该规则主要作用于引用查询条件的详细描述，在 Zero 动态建模中，直接对应`X_ATTRIBUTE` 表中的
 * `sourceReference` 属性，其中直接解析 `rule` 节点实现引用的提取行为，配置节点格式如：
 * <pre><code>
 *     {
 *         "rule": {
 *             "conditions": {},
 *             "condition": {},
 *             "required": [],
 *             "diff": [],
 *             "unique": []
 *         }
 *     }
 * </code></pre>
 * 上述五种规则的解释如下
 * <pre><code>
 *     1. condition / conditions：这两个Json格式提供了查询条件模板，用来描述当前引用的查询条件，查询条件描述作用于如下场景：
 *        Atom A -> field1 ( reference 引用 ) -> Atom B
 *        A模型中的 field1 用于直接引用（关联）模型B，如果读取模型 A的记录，则需要提供查询条件，此处的 condition / conditions
 *        提供了查询参数模板（支持JEXL），系统会解析参数模板并解析参数，解析之后根据实际查询条件命中模型存储中的相关数据，最终会
 *        在当前记录中直接设置查询到的模型 B 的数据信息。
 *        condition: 单记录读取，Atom A 和 Atom B 之间的对应关系是 1: 1（如账号/员工），最终返回的记录为 {@link JsonObject}
 *        conditions：多记录读取，Atom A 和 Atom B 之间的对应关系是 1: N（如角色/角色），最终返回的记录为 {@link JsonArray}
 *
 *     2. required：必须属性规则，引用成功的条件在于引用数据中 required 包含的属性集都是有数据的，否则表示引用记录非法。
 *
 *     3. diff：差异属性规则，当两个 Atom A的实例执行比较时，diff 规则用于描述引用的 Atom B中比较哪些属性，如果不设置则表示
 *        所有属性都参与比较，如果设置则表示只有 diff 中的属性才参与比较。
 *
 *     4. unique：标识规则，如果要针对 Atom A中引用的一堆 Atom B 实例执行引用时，unique 描述了 Atom B 执行合并的原则，标识
 *        规则中的属性完全一致可代表：同一记录。
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RRule implements Serializable {
    private static final Annal LOGGER = Annal.get(RRule.class);
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject condition;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject conditions;
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray required;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray unique;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray diff;

    /**
     * 属性类型描述，当一个属性定义为引用类型时，该属性的最终数据一定是
     * {@link io.vertx.core.json.JsonArray} 或 {@link io.vertx.core.json.JsonObject}
     * 这两种类型才支持引用格式，否则引用格式会出现不完整或不规范的情况。
     */
    @JsonIgnore
    private Class<?> type;

    // -------------------------- 获取函数 ------------------------
    public Set<String> getUnique() {
        if (HUt.isNotNil(this.unique)) {
            return HUt.toSet(this.unique);
        } else {
            return new HashSet<>();
        }
    }

    public void setUnique(final JsonArray unique) {
        this.unique = unique;
    }

    public JsonObject getCondition() {
        return this.condition;
    }

    public void setCondition(final JsonObject condition) {
        this.condition = condition;
    }

    public JsonObject getConditions() {
        return this.conditions;
    }

    public void setConditions(final JsonObject conditions) {
        this.conditions = conditions;
    }

    public Set<String> getRequired() {
        if (HUt.isNotNil(this.required)) {
            return HUt.toSet(this.required);
        } else {
            return new HashSet<>();
        }
    }

    public void setRequired(final JsonArray required) {
        this.required = required;
    }

    public JsonArray getDiff() {
        return this.diff;
    }

    public void setDiff(final JsonArray diff) {
        this.diff = diff;
    }

    public Class<?> type() {
        return this.type;
    }

    // -------------------------- 设置函数 ------------------------
    public RRule type(final Class<?> type) {
        this.type = type;
        return this;
    }

    public String keyDao() {
        final StringBuilder key = new StringBuilder();
        if (HUt.isNotNil(this.condition)) {
            key.append("condition:").append(this.condition.hashCode());
        }
        if (HUt.isNotNil(this.conditions)) {
            key.append("conditions:").append(this.conditions.hashCode());
        }
        return key.toString();
    }

    // -------------------------- 条件编译 ------------------------

    /**
     * 单记录条件条件编译，编译结果为 Qr 语法
     * condition + record = 查询引擎结果（不同记录编译结果不同）
     *
     * @param record 单数据记录
     *
     * @return 根据数据记录编译好的 Qr 条件
     */
    public JsonObject compile(final HRecord record) {
        final JsonObject tpl = new JsonObject();
        if (Objects.isNull(record)) {
            return tpl;
        }
        this.compile(this.condition).forEach(field -> {
            // Target field
            final String target = this.condition.getString(field);
            // Null Pointer for record
            final Object value = record.get(target);
            if (Objects.nonNull(value) && HUt.isNotNil(value.toString())) {
                tpl.put(field, value);
            }
        });
        if (HUt.isNotNil(tpl)) {
            LOGGER.info("[ EMF ] Reference condition building: {0}", tpl.encode());
        }
        // If null of "", the AND operator will be set.
        tpl.put(VString.EMPTY, this.condition.getBoolean(VString.EMPTY, Boolean.TRUE));
        return tpl;
    }

    /**
     * 多记录条件编译，编译结果为 Qr 语法
     * conditions + record = 查询引擎结果（不同记录编译结果不同）
     *
     * @param records 多数据记录
     *
     * @return 根据数据记录编译好的 Qr 条件
     */
    public JsonObject compile(final HRecord[] records) {
        final JsonObject tpl = new JsonObject();
        this.compile(this.conditions).forEach(field -> {
            final String target = this.conditions.getString(field);
            final Set<Object> values = Arrays.stream(records)
                .map(record -> record.get(target))
                .filter(Objects::nonNull).collect(Collectors.toSet());
            final JsonArray valueArray = HUt.toJArray(values);
            if (HUt.isNotNil(valueArray)) {
                tpl.put(field, HUt.toJArray(values));
            }
        });
        // If null of "", the AND operator will be set.
        tpl.put(VString.EMPTY, this.conditions.getBoolean(VString.EMPTY, Boolean.TRUE));
        return tpl;
    }

    private Stream<String> compile(final JsonObject condition) {
        return condition.fieldNames().stream()
            // 过滤条件
            .filter(HUt::isNotNil)
            // 过滤空
            .filter(field -> Objects.nonNull(condition.getValue(field)))
            // 过滤非String类型
            .filter(field -> condition.getValue(field) instanceof String);
    }

    @Override
    public String toString() {
        return "RRule{" +
            "condition=" + this.condition +
            ", conditions=" + this.conditions +
            ", required=" + this.required +
            ", unique=" + this.unique +
            ", diff=" + this.diff +
            ", type=" + this.type +
            '}';
    }
}
