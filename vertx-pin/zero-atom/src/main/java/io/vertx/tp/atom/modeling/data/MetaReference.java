package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.reference.DataQRule;
import io.vertx.tp.atom.modeling.reference.DataQuote;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MetaReference {
    /*
     * 引用信息
     */
    private final transient ConcurrentMap<String, DataQuote> references
            = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, DataQRule> rules
            = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, Set<String>> ruleDiff
            = new ConcurrentHashMap<>();

    MetaReference(final Model modelRef) {
        /*
         * Reference
         */
        final Set<MAttribute> attributes = modelRef.getAttributes();
        attributes.stream().filter(Objects::nonNull)
                // 包含了 source 引用才合法
                .filter(attr -> Objects.nonNull(attr.getSource()))
                // 类型定义必须是 REFERENCE
                .filter(attr -> AttributeType.REFERENCE.name().equals(attr.getType()))
                .forEach(attribute -> {
                    // 读取 Table Name
                    final String source = attribute.getSource();
                    final DataQuote reference = Fn.pool(this.references, source, () -> DataQuote.create(source));
                    // 执行 Source 的初始化
                    reference.add(attribute);
                });
        /*
         * DataQuote之后处理
         */
        this.references.values().forEach(source -> this.rules.putAll(source.rules()));
        this.rules.forEach((field, rule) -> {
            final Set<String> diffFields = Ut.toSet(rule.getDiff());
            if (!diffFields.isEmpty()) {
                this.ruleDiff.put(field, diffFields);
            }
        });
    }


    /*
     * 模板中增加引用
     * source -> DataQuote
     */
    ConcurrentMap<String, DataQuote> references() {
        return this.references;
    }

    ConcurrentMap<String, DataQRule> rules() {
        return this.rules;
    }

    ConcurrentMap<String, Set<String>> ruleDiff() {
        return this.ruleDiff;
    }
}
