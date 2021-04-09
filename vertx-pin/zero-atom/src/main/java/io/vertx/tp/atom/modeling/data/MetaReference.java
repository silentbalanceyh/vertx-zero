package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.reference.DataQKey;
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
         * Reference Calculation based on Model
         */
        final Set<MAttribute> attributes = modelRef.getAttributes();
        attributes.stream().filter(Objects::nonNull)
                // 包含了 source 引用才合法
                .filter(attr -> Objects.nonNull(attr.getSource()))
                // The attribute type must be `REFERENCE`
                .filter(attr -> AttributeType.REFERENCE.name().equals(attr.getType()))
                .forEach(attribute -> {
                    /*
                     *  Here are two category.
                     *
                     *  1 - source stored `M_MODEL` identifiers.
                     *  2 - source is refer to static DAO class.
                     */
                    final String source = attribute.getSource();
                    final DataQuote reference = Fn.pool(this.references, source, () -> DataQuote.create(source));
                    // 执行 Source 的初始化
                    reference.add(attribute);
                });
        /*
         * DataQuote之后处理
         */
        this.references.values().forEach(source -> {
            /* Rules added */
            this.rules.putAll(source.rules());
            /* Source mount */
        });

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

    ConcurrentMap<String, DataQRule> rules() {
        return this.rules;
    }

    ConcurrentMap<String, Set<String>> ruleDiff() {
        return this.ruleDiff;
    }

    ConcurrentMap<DataQKey, DataQuote> references(final String appName) {
        final ConcurrentMap<DataQKey, DataQuote> switched = new ConcurrentHashMap<>();
        this.references.forEach((source, quote) -> {
            /* DataAtom 交换 */
            final DataQKey qKey = new DataQKey(appName, source);
            qKey.connect(quote);
            switched.put(qKey, quote);
        });
        return switched;
    }
}
