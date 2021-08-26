package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.config.AoAttribute;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.commune.element.TypeField;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * 模型基本信息
 */
class AoDefine {

    private transient final Model modelRef;
    private transient final String identifier;
    private transient final TypeAtom typeAtom = TypeAtom.create();

    AoDefine(final Model modelRef) {
        /* 模型引用信息 */
        this.modelRef = modelRef;
        /* 直接从模型中读取 identifier */
        this.identifier = modelRef.identifier();
        /* 直接计算 */
        modelRef.dbAttributes().forEach(attr -> {
            /* 提取 name */
            final String name = attr.getName();
            if (Ut.notNil(name)) {
                /* AoAttribute Extract from Model */
                final AoAttribute aoAttr = modelRef.attribute(name);
                final TypeField typeField;
                if (Objects.isNull(aoAttr)) {
                    // Fix Common issue here for type checking
                    typeField = TypeField.create(name, attr.getAlias());
                } else {
                    typeField = aoAttr.type();
                }
                if (Objects.nonNull(typeField)) this.typeAtom.add(typeField);
            }
        });
    }

    /* 读取底层存储的模型信息 */
    Model model() {
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

    AoAttribute attribute(final String attributeName) {
        return this.modelRef.attribute(attributeName);
    }

    /* 读取模型中的属性信息 */
    Set<String> attributeNames() {
        return this.modelRef.dbAttributes().stream()
            .map(MAttribute::getName)
            .filter(Ut::notNil)
            .collect(Collectors.toSet());
    }

    /* 属性 name = alias */
    ConcurrentMap<String, String> alias() {
        return this.typeAtom.alias();
    }

    // ------------------ 计算型处理 -----------------

    /*
     * 返回当前DataAtom中的类型
     * Shape 是复杂的类型数据，和 type 的返回值比较近似，但 Shape 中包含了更加丰富的类型数据相关信息
     * */
    TypeAtom shape() {
        /* 构造 Shape */
        return this.typeAtom;
    }

    ConcurrentMap<String, Class<?>> typeCls() {
        return this.modelRef.typeCls();
    }

    ConcurrentMap<String, TypeField> types() {
        return this.modelRef.types();
    }

    Class<?> type(final String field) {
        return this.modelRef.typeCls().getOrDefault(field, null);
    }

    private <T> T sure(final Function<MModel, T> function) {
        final MModel model = this.model().dbModel();
        if (Objects.isNull(model)) {
            return null;
        } else {
            return function.apply(model);
        }
    }
}
