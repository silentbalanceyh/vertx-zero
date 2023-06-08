package io.vertx.mod.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.uca.log.Annal;
import io.vertx.mod.atom.error._417RelatedFieldMissingException;
import io.vertx.mod.atom.error._417RelatedSchemaMissingException;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.element.DataKey;
import io.vertx.mod.atom.modeling.element.DataRow;
import io.vertx.mod.atom.modeling.element.DataTpl;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 连接专用
 * Model 和 Schema 执行连接操作
 */
class Bridge {

    static void connect(final Model model,
                        final Function<Schema, BiConsumer<MField, MAttribute>> consumer) {
        /*
         * 1. 遍历当前模型中的 Schema
         * 2. 二层遍历当前模型中的 Attribute
         */
        Ut.itCollection(model.schema(), nil -> model.dbAttributes(),
            /*
             * 核心逻辑，用于填充 arguments
             */
            (schema, attribute) -> Arrays.asList(schema.getFields()).forEach(field -> {
                /*
                 * 直接构造二维函数，在内部需要的时候调用和执行
                 */
                if (Objects.isNull(attribute.getSourceField())) {
                    /* 条件1：如果没有 sourceField，则考虑 name 相匹配 */
                    if (field.getName().equals(attribute.getName())) {
                        consumer.apply(schema).accept(field, attribute);
                    }
                } else {
                    /* 条件2：如果包含 sourceField，则考虑 sourceField 匹配 */
                    if (field.getName().equals(attribute.getSourceField())) {
                        consumer.apply(schema).accept(field, attribute);
                    }
                }
            }),
            /*
             * 过滤器，用于过滤不满足条件的 ( schema, attribute ) 键值对
             */
            (schema, attribute) -> attribute.getSource().equals(schema.identifier()));
        /*
         * FIX：解决Schema中的主键问题
         * 1. 对于 JOIN_MULTI 这种情况，在这里初始化 Matrix 的时候，有可能出现主键没有被记录到 Matrix的情况
         * 2. 如果属性中的值不包括主键，也会出现主键不在的情况，所以需要将各自的主键补充到对应的 Matrix 中
         * */
        model.schema().forEach(schema -> schema.getPrimaryKeys()
            .forEach(field -> consumer.apply(schema)
                .accept(field, toAttribute(schema, field))));

        /*
         * FIX：关于 Join 部分的填充
         * 1. 对于 JOIN_MULTI 的情况，还有一个特殊点就是 Join 部分的内容同样需要添加到 Matrix 中
         * 2. 这种只适应于 Join 部分中的内容不属于主键的情况
         */
        model.dbJoins().forEach(join -> {
            /* 读取模型 */
            final String identifier = join.getEntity();
            final Schema schema = model.schema(identifier);
            if (null != schema) {
                /* 模型字段 */
                final String fieldKey = join.getEntityKey();
                final MField field = schema.getField(fieldKey);
                if (null != field && !field.getIsPrimary()) {
                    consumer.apply(schema).accept(field, toAttribute(schema, field));
                }
            }
        });
    }

    private static MAttribute toAttribute(final Schema schema, final MField field, final String name) {
        final MAttribute attribute = new MAttribute();
        attribute.setName(name);
        attribute.setSource(schema.identifier());
        attribute.setSourceField(field.getName());
        return attribute;
    }

    private static MAttribute toAttribute(final Schema schema, final MField field) {
        return toAttribute(schema, field, field.getName());
    }

    @SuppressWarnings("all")
    static void join(final Model model,
                     final Function<Schema, BiConsumer<MField, MAttribute>> consumer) {
        final Set<MJoin> joins = model.dbJoins();
        final DataKey key = model.key();
        joins.forEach(join -> {
            final Schema schema = model.schema(join.getEntity());

            /* 找不到实体报错 */
            final String unique = model.namespace() + "-" + model.identifier();
            Fn.outWeb(null == schema, _417RelatedSchemaMissingException.class, Bridge.class,
                /* ARG1：当前关联关系对应的实体名 */ join.getEntity(),
                /* ARG2：当前主键的唯一标识 */ key.getUnique());

            final MField field = schema.getField(join.getEntityKey());
            /* 找不到字段定义 */
            Fn.outWeb(null == field, _417RelatedFieldMissingException.class, Bridge.class,
                /* ARG1：当前关联关系对应的实体字段名 */ join.getEntityKey(),
                /* ARG2：当前关联关系对应的实体名 */ join.getEntity(),
                /* ARG3：当前主键的唯一标识 */ key.getUnique());

            /* 读取主键对象 */
            final MAttribute virtual = toAttribute(schema, field, "pk." + schema.identifier() + "." + field.getName());
            /* 双函数Consumer */
            consumer.apply(schema).accept(field, virtual);
        });
    }
}

class Debug {

    private static final Annal LOGGER = Annal.get(Debug.class);

    public static void tpl(final DataTpl tpl) {
        /* DataAtom 处理 */
        final DataAtom atom = tpl.atom();
        final Model model = atom.model();
        /* 打印主体 */
        final StringBuilder source = new StringBuilder();
        source.append("\n<TPL> -- 主键模式：")
            .append(model.key().getMode())
            .append("\n");
        source.append("模型类型：")
            .append(model.dbModel().getType())
            .append("\n");
        source.append("映射关系 -> \n\n");
        source.append(String.format("%-20s", "属性名"))
            .append(String.format("%-20s", "表名"))
            .append("\n");
        tpl.appendConsole(source);
        LOGGER.debug(source.toString());
    }

    public static void io(final List<DataRow> rows) {
        final StringBuilder source = new StringBuilder();
        source.append("\n<Data> -- 数据信息：").append("\n");
        rows.forEach(row -> row.appendConsole(source));
        LOGGER.debug(source.toString());
        /* Json 格式 */
        final StringBuilder json = new StringBuilder();
        json.append("\n");
        rows.stream().filter(item -> Objects.nonNull(item.getRecord()))
            .forEach(item -> json.append(item.getRecord().toJson()).append("\n"));
        LOGGER.debug(json.toString());
    }
}