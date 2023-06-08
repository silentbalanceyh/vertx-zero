package io.modello.specification.meta;

import io.horizon.eon.VValue;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link HMetaAtom} 的默认实现，元模型类型定义专用数据结构，您可以从此类中直接继承打造自己的 {@link HMetaAtom}
 * 有此默认实现，可省略部分自定义代码，该实现保证在任何框架的JVM场景中可使用。
 * <pre><code>
 *     1. 基础属性的类型定义和类型分析
 *     2. 支持属性为复杂结构如 JsonObject / JsonArray 的类型分析
 *     3. 如果属性中包含了一个 complex = true 的情况则定义当前模型为复杂模型
 *     4. 支持逆向分析功能：从数据反向分析构造元模型
 *     5. 后期追加 EMF 中的元模型规范来完善此处定义
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MetaAtom implements HMetaAtom {
    /**
     * 逆向功能专用，从数据中分析出来的类型定义
     */
    private final MetaAtomMatrix matrix = new MetaAtomMatrix();
    /*
     * 返回当前模型定义中的原属性定义
     */
    private final ConcurrentMap<String, HMetaField> shape = new ConcurrentHashMap<>();

    /*
     * 判断当前模型是否一个复杂类型
     */
    private boolean complex = Boolean.FALSE;

    MetaAtom() {
    }

    // Elementary
    @Override
    public HMetaAtom add(final String name, final String alias, final Class<?> type) {
        if (HUt.isNotNil(name)) {
            /* JsonArray */
            final HMetaField typeItem = HMetaField.of(name, alias, type);
            this.shape.put(name, typeItem);
        }
        return this;
    }

    @Override
    public HMetaAtom add(final String name, final String alias, final Collection<HMetaField> children) {
        if (HUt.isNotNil(name)) {
            this.complex = true;
            // 分析当前类型，并设置当前字段元数据
            final Class<?> fieldType;
            if (children instanceof List) {
                fieldType = JsonArray.class;
            } else {
                fieldType = JsonObject.class;
            }
            this.add(name, alias, fieldType);
            // 设置子字段元数据
            final HMetaField child = this.shape.getOrDefault(name, null);
            child.add(children);
        }
        return this;
    }

    @Override
    public HMetaAtom add(final HMetaField field) {
        final String name = field.name();
        this.shape.put(name, field);
        if (field.isComplex()) {
            this.complex = true;
        }
        return this;
    }

    @Override
    public HMetaAtom clear() {
        this.shape.clear();
        return this;
    }

    @Override
    public boolean isComplex() {
        return this.complex;
    }

    @Override
    public boolean isComplex(final String name) {
        final HMetaField item = this.shape.getOrDefault(name, null);
        if (Objects.isNull(item)) {
            return false;
        } else {
            return item.isComplex();
        }
    }

    @Override
    public int size(final String name) {
        final HMetaField typeItem = this.shape.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            return typeItem.children().size();
        } else {
            return VValue.ZERO;
        }
    }

    @Override
    public int size() {
        return this.shape.size();
    }

    @Override
    public HMetaField field(final String name) {
        return this.shape.getOrDefault(name, null);
    }

    @Override
    public ConcurrentMap<String, String> alias() {
        final ConcurrentMap<String, String> alias = new ConcurrentHashMap<>();
        this.shape.forEach((field, item) -> alias.put(field, item.alias()));
        return alias;
    }

    @Override
    public Class<?> type(final String field) {
        final HMetaField item = this.shape.getOrDefault(field, null);
        return Objects.isNull(item) ? null : item.type();
    }

    @Override
    public Class<?> type(final String field, final String childField) {
        final HMetaField item = this.shape.getOrDefault(field, null);
        if (Objects.isNull(item)) {
            return null;
        } else {
            if (JsonArray.class == item.type()) {
                return item.type(childField);
            } else {
                return null;
            }
        }
    }

    // -------------------- 逆向工程专用接口 -----------------------

    @Override
    public List<Class<?>> type() {
        return this.matrix.type(); // this.typeList;
    }

    @Override
    public Class<?> type(final Integer index) {
        return this.matrix.type(index);
    }

    @Override
    public HMetaAtom compile(final JsonArray data) {
        if (this.complex) {
            this.matrix.compileComplex(data, this);
        } else {
            this.matrix.compileSimple(data, this);
        }
        return this;
    }
}
