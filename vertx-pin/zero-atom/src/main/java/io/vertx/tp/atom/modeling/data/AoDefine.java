package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HTAtom;
import io.vertx.up.experiment.mixture.HTField;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
    private transient final HTAtom htAtom = HTAtom.create();

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
                final HAttribute aoAttr = modelRef.attribute(name);
                final HTField hField;
                if (Objects.isNull(aoAttr)) {
                    // Fix Common issue here for type checking
                    hField = HTField.create(name, attr.getAlias());
                } else {
                    hField = aoAttr.field();
                }
                if (Objects.nonNull(hField)) {
                    this.htAtom.add(hField);
                }
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

    HAttribute attribute(final String attributeName) {
        return this.modelRef.attribute(attributeName);
    }

    /* 读取模型中的属性信息 */
    Set<String> attribute() {
        return this.modelRef.dbAttributes().stream()
            .map(MAttribute::getName)
            .filter(Ut::notNil)
            .collect(Collectors.toSet());
    }

    /* 属性 name = alias */
    ConcurrentMap<String, String> alias() {
        return this.htAtom.alias();
    }

    /*
     * 返回当前DataAtom中的类型
     * Shape 是复杂的类型数据，和 type 的返回值比较近似，但 Shape 中包含了更加丰富的类型数据相关信息
     * */
    HTAtom shape() {
        /* 构造 Shape */
        return this.htAtom;
    }

    // ------------------ 计算型处理 -----------------

    ConcurrentMap<String, HTField> types() {
        final ConcurrentMap<String, HTField> typeMap = new ConcurrentHashMap<>();
        this.modelRef.attribute().forEach((name) -> {
            final HAttribute attribute = this.modelRef.attribute(name);
            typeMap.put(name, attribute.field());
        });
        return typeMap;
    }

    HTField type(final String field) {
        final HAttribute attribute = this.modelRef.attribute(field);
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.field();
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
