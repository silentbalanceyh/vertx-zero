package io.aeon.experiment.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
 * 其中一条表示规则的相关信息
 * 支持两种格式
 * 1）单一格式：字符串
 * 2）复杂格式：JsonArray
 */
@JsonSerialize(using = RuleTermSerializer.class)
@JsonDeserialize(using = RuleTermDeserializer.class)
public class RuleTerm implements Serializable {

    private final Set<String> fields = new HashSet<>();

    public RuleTerm(final String rule) {
        this.fields.add(rule);
    }

    public RuleTerm(final JsonArray rules) {
        rules.stream()
            /* 过滤空 */
            .filter(Objects::nonNull)
            /* 只要String */
            .filter(item -> item instanceof String)
            .map(item -> (String) item)
            .forEach(this.fields::add);
    }

    public Set<String> getFields() {
        return this.fields;
    }

    public JsonObject dataRule(final JsonObject input) {
        final JsonObject cond = new JsonObject();
        this.fields.stream().filter(input::containsKey)
            .forEach(field -> cond.put(field, input.getValue(field)));
        return cond;
    }

    /*
     * 内置逻辑
     * 使用当前的 RuleTerm 检查输入数据是否符合当前标识规则
     */
    public JsonObject dataMatch(final JsonObject input) {
        if (Objects.isNull(input)) {
            return null;
        } else {
            final JsonObject compress = new JsonObject();
            input.fieldNames().stream()
                .filter(field -> Objects.nonNull(input.getValue(field)))
                .forEach(field -> compress.put(field, input.getValue(field)));
            /* 传入数据本身的 fields */
            final Set<String> dataFields = compress.fieldNames();
            final long counter = this.fields.stream()
                .filter(dataFields::contains)
                .count();
            /* 相等证明 fields 中所有的字段都包含在了 dataFields 中 */
            if (counter == this.fields.size()) {
                return compress.copy();
            } else {
                return null;
            }
        }
    }

    /*
     * 每个 Rule Term 按照字段集合进行相等性匹配
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuleTerm)) {
            return false;
        }
        final RuleTerm ruleTerm = (RuleTerm) o;
        return this.fields.equals(ruleTerm.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fields);
    }

    @Override
    public String toString() {
        return "RuleTerm{" +
            "fields=" + this.fields +
            '}';
    }
}
