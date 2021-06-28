package io.vertx.tp.optic.modeling;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.config.AoSource;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AnyType {
    /**
     * 重写类型分析器
     *
     * @param attribute {@link MAttribute} 传入的属性对象
     * @param schemaFn  {@link Function} 提取原始Schema专用
     *
     * @return {@link Class}
     */
    static Class<?> analyze(final MAttribute attribute, final Function<String, Schema> schemaFn) {
        /*
         * 属性构造，根据属性类型构造
         */
        final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);
        if (AttributeType.INTERNAL == type) {
            /*
             * type = INTERNAL 属性
             */
            if (KeField.Modeling.VALUE_SET.contains(attribute.getSourceField())) {
                final AoSource service = new AoSource(attribute);
                return service.type();
            } else {
                final Schema schema = schemaFn.apply(attribute.getSource());
                final MField field = schema.getField(attribute.getSourceField());
                if (Objects.nonNull(field)) {
                    /*
                     * 属性 Map 处理
                     */
                    return Ut.clazz(field.getType());
                } else return null;
            }
        } else if (AttributeType.REFERENCE == type) {
            /*
             * REFERENCE 新增类型，用于引用不同类型，主要应用于
             * 1）读取
             * 2）查询
             *
             * type = REFERENCE 属性
             * 只支持两种数据类型
             * JsonObject / JsonArray
             *
             * 这种类型禁止使用在写操作中：添加、删除、修改
             */
            final AoSource service = new AoSource(attribute);
            return service.type();
        } else return null;
    }
}
