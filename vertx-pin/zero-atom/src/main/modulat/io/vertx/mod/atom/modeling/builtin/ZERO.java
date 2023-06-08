package io.vertx.mod.atom.modeling.builtin;


import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.eon.VValue;
import io.modello.eon.em.EmKey;
import io.modello.eon.em.EmModel;
import io.vertx.mod.atom.error._417RelationCounterException;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.element.DataKey;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.jooq.tools.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 连接专用
 * Model 和 Schema 执行连接操作
 */
class Bridge {

    static void connect(final Model model,
                        final Set<Schema> schemaSet) {
        final Set<MJoin> joins = model.dbJoins();
        if (!joins.isEmpty()) {
            // 查找和 joins 中定义匹配的 Schema
            final Set<Schema> found = schemaSet.stream()
                // 过滤 null
                .filter(Objects::nonNull)
                .filter(schema -> Objects.nonNull(schema.getEntity()))
                // 匹配 model 和 schema的名空间
                .filter(schema -> StringUtils.equals(schema.namespace(), model.namespace()))
                // 当前 join 中包含了该 schema
                .filter(schema -> joins.stream()
                    .filter(Objects::nonNull)
                    // 计算 join 中的 entity 是否和 schema 的 identifier 相等
                    .map(MJoin::getEntity)
                    .filter(Objects::nonNull)
                    .anyMatch(entity -> entity.equals(schema.identifier())))
                .collect(Collectors.toSet());
            // 连接过后执行
            if (!found.isEmpty()) {
                // 执行消费处理
                connectInternal(model, found);
            }
        }
    }

    private static void connectInternal(final Model model,
                                        final Set<Schema> found) {
        // 核心方法，用于链接
        final Set<Schema> schemata = model.schema();
        // 不可能null
        assert null != schemata : "[OxA] 当前Schema集合不可能为空！";
        schemata.clear();
        schemata.addAll(found);

        // 针对模型的属性的处理
        final Set<MAttribute> attributes = model.dbAttributes();
        /*
         * 1.只有特殊情况会在这个流程中填充source，一个是连接过后值为1
         * 2.另外一个是模型类型为DIRECT
         */
        final EmModel.Type type = Ut.toEnum(model.dbModel().getType(), EmModel.Type.class);
        if (VValue.ONE == schemata.size() && EmModel.Type.DIRECT == type) {
            // 判断模型的类型
            final Schema schema = schemata.iterator().next();
            // 设置对应的 source
            attributes.stream()
                .filter(attribute -> Ut.isNil(attribute.getSource()))
                .forEach(attribute -> attribute.setSource(schema.identifier()));
        }
    }

    /**
     * 计算组件和模型的模式
     */
    static void connect(final Model model,
                        final String unique) {
        // 1. 读取 DataKey
        final DataKey key = DataKey.create(unique);
        // 2. 读取 Schema
        final Set<MJoin> joins = model.dbJoins();
        final Set<Schema> schema = model.schema();
        /*
         * 正常模式下，joins > 1 和 schema > 1 发生时，需要进行第一次分流，而且必须满足基本条件
         * joins.size() >= schema.size() >= 1
         * 否则直接抛出异常信息
         */
        Fn.outWeb(joins.size() < 1 || schema.size() < 1 || schema.size() < joins.size(),
            _417RelationCounterException.class, Bridge.class,
            /* ARG1：当前模型的标识，同一个应用中的模型标识唯一 */ model.dbModel().getIdentifier(),
            /* ARG2：当前模型 Model 中对应的实体数量 */ schema.size(),
            /* ARG3：当前模型 Model 中定义的 MJoin 的数量 */ joins.size());
        /*
         * 先根据 Schema Size 计算当前的模型的类型：
         * JOINED：1 model - n entity
         * DIRECT：1 model - 1 entity
         */
        if (1 < schema.size()) {
            model.dbModel().setType(EmModel.Type.JOINED.name());
            /*
             * 多表流程，这个时候 schema 的长度 > 1.
             * 读取 Schema 的信息来计算
             */
            if (schema.size() == joins.size()) {
                final Set<String> targets = joins.stream()
                    .map(MJoin::getEntityKey)
                    .filter(Objects::nonNull).collect(Collectors.toSet());
                if (1 == targets.size()) {
                    /*
                     * 同键单键连接，每个实体只有一个键，而键名相同
                     *
                     */
                    key.setMode(EmKey.Mode.JOIN_KEY);
                } else {
                    /*
                     * 同键多键连接，每个实体有一个自己的主键，键名不同
                     */
                    key.setMode(EmKey.Mode.JOIN_MULTI);
                }
            } else {
                /*
                 * 同键集合键连接
                 */
                key.setMode(EmKey.Mode.JOIN_COLLECTION);
            }
        } else {
            model.dbModel().setType(EmModel.Type.DIRECT.name());
            /*
             * 单表流程，这个时候 schema 和 join 的size都应该是 1.
             * 读取 Schema 的信息
             */
            final Schema schemaObj = schema.iterator().next();
            final List<MField> fields = schemaObj.getPrimaryKeys();
            if (1 == fields.size()) {
                /*
                 * Schema 中的主键长度为 1，表示
                 * 这个实体有 1 个主键，那么就属于单主键
                 */
                key.setMode(EmKey.Mode.DIRECT);
            } else {
                /*
                 * Schema 中包含了多个主键，主键数量 > 1
                 */
                key.setMode(EmKey.Mode.COLLECTION);
            }
        }
        model.key(key);
    }
}
