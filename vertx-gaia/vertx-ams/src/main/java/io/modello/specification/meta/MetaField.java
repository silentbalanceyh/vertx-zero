package io.modello.specification.meta;

import io.horizon.util.HUt;
import io.modello.atom.normalize.KKey;
import io.modello.eon.em.KeyType;
import io.modello.specification.element.HKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * {@link HMetaField} 的默认实现，您可以直接从此类继承定义自己的 {@link HMetaField} 相关信息
 * 有此默认实现，可省略部分自定义代码，该实现保证在任何框架的JVM场景中使用。
 * 该类型内部采用双引用模式，空间换时间，采用双引用模式的主要原因在于子类型在某些场景中关注顺序问题，但某些场景中不关注顺序问题，
 * 而日常提取单个属性信息时，使用映射的结构更容易拿到当前属性的元数据定义信息，所以某些时候会采用 Map 结构，某些时候采用 List
 * 结构，就近原则，减少语言级的迭代。
 * <pre><code>
 *     1. vector：field = HMetaField，处理内部属性类型向量，方便检索
 *     2. children：原始数据结构，包含了拥有数据的属性定义
 *     3. 动态建模过程中可一次性将 children 和 vector 填满，如果反向分析则 metadata 部分不会一次性
 *        填满，而是在分析过程中动态填充。
 *     4. 后期追加 EMF 中的元模型规范来完善此处的定义
 * </code></pre>
 *
 * 元模型解析部分数据格式：
 * JsonObject 类型
 * // <pre><code class="json">
 * {
 *     "field 1": "HMetaField 1",
 *     "field 2": "HMetaField 2",
 *     "childMap": {
 *         "field 3-1": "HMetaField 3-1",
 *         "...": "..."
 *     }
 * }
 * // </code></pre>
 * JsonArray 类型
 * // <pre><code class="json">
 * {
 *     "field 1": "HMetaField 1",
 *     "field 2": "HMetaField 2",
 *     "children": [
 *         "HMetaField 3-1",
 *         "HMetaField 3-2",
 *         "..."
 *     ]
 * }
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MetaField implements HMetaField {
    // -------------------- Private Field -----------------------
    /**
     * 属性名到子类型的映射关系：name = HMetaField
     */
    private final ConcurrentMap<String, HMetaField> vector = new ConcurrentHashMap<>();
    /**
     * 子类型属性的原始数据
     */
    private final List<HMetaField> children = new ArrayList<>();
    /**
     * 当前字段名称
     */
    private final String name;
    /**
     * 当前字段别名
     */
    private final String alias;
    /**
     * 复杂类型的子键信息，目前版本只支持 UNIQUE 类型的键，不支持其他类型
     */
    private final HKey key = KKey.of(KeyType.UNIQUE);
    /**
     * 当前字段类型，如果是复杂类型则
     * 直接设置子类型 children 将当前类型转换成复杂类型
     */
    private final Class<?> type;

    // -------------------- Constructor -----------------------

    MetaField(final String name, final String alias, final Class<?> type) {
        this.name = name;
        this.alias = alias;
        /*
         * 类型默认值，什么都不传入时采用 java.lang.String（高频使用）
         */
        this.type = Objects.isNull(type) ? String.class : type;
    }

    // -------------------- 接口：键设置 -----------------------
    @Override
    public HKey key() {
        return this.key;
    }

    @Override
    public HMetaField key(final HKey key) {
        this.key.key(Optional.ofNullable(key).map(HKey::keys).orElse(new HashSet<>()));
        return this;
    }

    @Override
    public HMetaField key(final Set<String> keys) {
        // 内部空检查，直接传
        this.key.key(keys);
        return this;
    }

    // -------------------- 接口：基本函数 -----------------------
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String name(final String field) {
        return this.children(field, HMetaField::name);
    }

    @Override
    public String alias() {
        return this.alias;
    }

    @Override
    public String alias(final String field) {
        return this.children(field, HMetaField::alias);
    }

    @Override
    public Class<?> type() {
        return this.type;
    }

    @Override
    public Class<?> type(final String field) {
        return this.children(field, HMetaField::type);
    }

    private <T> T children(final String field, final Function<HMetaField, T> fetchFn) {
        if (HUt.isNil(field)) {
            return null;
        } else {
            final HMetaField item = this.vector.getOrDefault(field, null);
            if (Objects.isNull(item)) {
                return null;
            } else {
                return fetchFn.apply(item);
            }
        }
    }

    // -------------------- 接口：子函数 -----------------------
    @Override
    public HMetaField add(final Collection<HMetaField> children) {
        if (Objects.nonNull(children) && !children.isEmpty()) {
            /* 同时追加到 vector 和 children 中 */
            children.forEach(item -> this.add(item.name(), item));
        }
        return this;
    }

    @Override
    public HMetaField add(final String name, final HMetaField child) {
        if (Objects.nonNull(child)) {
            this.children.add(child);
            this.vector.put(child.name(), child);
        }
        return this;
    }

    @Override
    public HMetaField set(final String name, final HMetaField child) {
        if (Objects.nonNull(child)) {
            // 替换 children 和 vector
            this.children.replaceAll(item -> item.name().equals(name) ? child : item);
            this.vector.put(child.name(), child);
        }
        return this;
    }

    @Override
    public boolean isComplex() {
        // 双检查
        return !this.children.isEmpty() && !this.vector.isEmpty();
    }


    @Override
    public List<HMetaField> children() {
        return this.children;
    }

    // -------------------- Object 继承 -----------------------
    @Override
    public String toString() {
        return "HTField{" +
            "name='" + this.name + '\'' +
            ", alias='" + this.alias + '\'' +
            ", type=" + this.type +
            '}';
    }
}
