package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.commune.element.CParam;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * 模型基本信息
 */
class MetaInfo {

    private transient final Model modelRef;
    private transient final String identifier;
    private transient final Shape shape = Shape.create();

    MetaInfo(final Model modelRef) {
        /* 模型引用信息 */
        this.modelRef = modelRef;
        /* 直接从模型中读取 identifier */
        this.identifier = modelRef.identifier();
        /* 直接计算 */
        modelRef.getAttributes().forEach(attr -> {
            /* 提取 name */
            final String name = attr.getName();
            final String alias = attr.getAlias();
            if (Ut.notNil(name)) {
                /* isArray 判断集合 */
                Boolean isArray = attr.getIsArray();
                if (Objects.isNull(isArray)) {
                    isArray = Boolean.FALSE;
                }
                /* type */
                final AttributeType type = Ut.toEnum(attr::getType, AttributeType.class, AttributeType.INTERNAL);
                /*
                 * 添加操作
                 * 由于引入了 up / down 类型，所以这里还需要另外一个条件
                 * 1. type = INTERNAL
                 * 2. isArray = true
                 * */
                if (isArray && AttributeType.INTERNAL == type) {
                    /*
                     * Complex for Array
                     */
                    this.shape.add(name, alias, Bridge.toShape(attr, MAttribute::getSourceConfig));
                } else {
                    /*
                     * Simple for Flatted
                     */
                    this.shape.add(name, alias, this.type(name));
                }
            }
        });
    }

    /* 读取底层存储的模型信息 */
    Model reference() {
        return this.modelRef;
    }

    /* 模型标识符 */
    String identifier() {
        return this.identifier;
    }

    /* 返回统一标识符 */
    String sigma() {
        return this.sure(MModel::getSigma);
    }

    String language() {
        return this.sure(MModel::getLanguage);
    }


    /* 读取模型中的属性信息 */
    Set<String> attributes() {
        return this.modelRef.getAttributes().stream()
                .map(MAttribute::getName)
                .filter(Ut::notNil)
                .collect(Collectors.toSet());
    }

    /* 属性 name = alias */
    ConcurrentMap<String, String> alias() {
        return this.shape.alias();
    }

    // ------------------ 计算型处理 -----------------

    /*
     * 返回当前DataAtom中的类型
     * Shape 是复杂的类型数据，和 type 的返回值比较近似，但 Shape 中包含了更加丰富的类型数据相关信息
     * */
    Shape shape() {
        /* 构造 Shape */
        return this.shape;
    }

    CParam diff() {
        /* 构造 CParam */
        return CParam.create(this.modelRef.types());
    }

    CParam diff(final Set<String> ignoreSet) {
        return this.diff().ignores(ignoreSet);
    }

    ConcurrentMap<String, Class<?>> type() {
        return this.modelRef.types();
    }

    Class<?> type(final String field) {
        return this.modelRef.types().getOrDefault(field, null);
    }

    private <T> T sure(final Function<MModel, T> function) {
        final MModel model = this.reference().getModel();
        if (Objects.isNull(model)) {
            return null;
        } else {
            return function.apply(model);
        }
    }
}
