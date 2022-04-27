package cn.originx.uca.log;

import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 核心变更方法主逻辑
 */
class AtDiffer {
    /**
     * 变更表生成的主逻辑
     *
     * @param oldRecord {@link JsonObject} 旧记录
     * @param newRecord {@link JsonObject} 新记录
     * @param flag      {@link ChangeFlag} 当前变更的操作,ADD,DELETE,UPDATE
     * @param atom      {@link DataAtom} 模型定义对象
     * @param ignores   {@link Set} 被忽略的属性集
     *
     * @return {@link List} 返回变更列表
     */
    static List<XActivityChange> toChanges(final JsonObject oldRecord, final JsonObject newRecord,
                                           final ChangeFlag flag, final DataAtom atom, final Set<String> ignores) {
        final List<XActivityChange> changes = new ArrayList<>();
        if (ChangeFlag.NONE != flag) {
            // Vs 引用获取
            if (ChangeFlag.ADD == flag) {

                /* 主记录添加 */
                final Set<String> added = onSet(newRecord.fieldNames(), atom, ignores, newRecord::getValue);
                onAttribute(added, atom, (attrName, change) -> {
                    change.setType(flag.name());
                    final Object value = newRecord.getValue(attrName);
                    change.setValueNew(value.toString());
                    changes.add(change);
                });
            } else if (ChangeFlag.DELETE == flag) {

                /* 主记录删除 */
                final Set<String> deleted = onSet(oldRecord.fieldNames(), atom, ignores, oldRecord::getValue);
                onAttribute(deleted, atom, (attrName, change) -> {
                    change.setType(flag.name());
                    final Object value = oldRecord.getValue(attrName);
                    change.setValueOld(value.toString());
                    changes.add(change);
                });
            } else if (ChangeFlag.UPDATE == flag) {

                /* 主记录更新，属性：ADD */
                final Set<String> addedSet = Ut.diff(newRecord.fieldNames(), oldRecord.fieldNames());
                final Set<String> added = onSet(addedSet, atom, ignores, newRecord::getValue);
                onAttribute(added, atom, (attrName, change) -> {
                    change.setType(ChangeFlag.ADD.name());
                    final Object value = newRecord.getValue(attrName);
                    change.setValueNew(value.toString());
                    changes.add(change);
                });

                /* 主记录更新，属性：DELETE */
                final Set<String> deletedSet = Ut.diff(oldRecord.fieldNames(), newRecord.fieldNames());
                final Set<String> deleted = onSet(deletedSet, atom, ignores, oldRecord::getValue);
                onAttribute(deleted, atom, (attrName, change) -> {
                    change.setType(ChangeFlag.DELETE.name());
                    final Object value = oldRecord.getValue(attrName);
                    change.setValueOld(value.toString());
                    if (!newRecord.containsKey(attrName)) {
                        /* 移除属性时，用新的 null 覆盖，新记录比对才需要，支持移除功能 */
                        newRecord.putNull(attrName);
                    }
                    changes.add(change);
                });

                final Vs vs = atom.vs();
                final Set<String> updatedSet = Ut.intersect(newRecord.fieldNames(), oldRecord.fieldNames());
                final Set<String> updated = onSet(updatedSet, atom, ignores, oldRecord::getValue, (valueOld, attributeName) -> {
                    final Object valueNew = newRecord.getValue(attributeName);
                    return vs.isChange(valueOld, valueNew, attributeName);
                });
                onAttribute(updated, atom, (attrName, change) -> {
                    change.setType(ChangeFlag.UPDATE.name());
                    // Old
                    final Object oldValue = oldRecord.getValue(attrName);
                    final Object newValue = newRecord.getValue(attrName);
                    if (vs.isValue(oldValue, attrName) || vs.isValue(newValue, attrName)) {
                        if (vs.isValue(oldValue, attrName)) {
                            change.setValueOld(oldValue.toString());
                        }
                        if (vs.isValue(newValue, attrName)) {
                            change.setValueNew(newValue.toString());
                        }
                        changes.add(change);
                    }
                });
            }
        }
        return changes;
    }


    private static Set<String> onSet(final Set<String> fieldSet, final DataAtom atom,
                                     final Set<String> ignoreSet,
                                     final Function<String, Object> valueFn) {
        final Vs vs = atom.vs();
        return onSet(fieldSet, atom, ignoreSet, valueFn, vs::isValue);
    }

    private static Set<String> onSet(final Set<String> fieldSet, final DataAtom atom,
                                     final Set<String> ignoreSet,
                                     final Function<String, Object> valueFn,
                                     final BiPredicate<Object, String> valuePre) {
        // 2. 移除 ignored 字段
        final Set<String> setCopy = new HashSet<>(fieldSet);
        setCopy.removeAll(ignoreSet);

        // 循环外拿到辅助引用
        final Model model = atom.model();
        return setCopy.stream().filter(attributeName -> {
            final MAttribute attribute = model.dbAttribute(attributeName);
            if (Objects.isNull(attribute)) {
                // 3. 非合法属性筛选
                return Boolean.FALSE;
            }
            final Object value = valueFn.apply(attributeName);
            // 4. 必须是 Vs 中验证合法数据
            return valuePre.test(value, attributeName);
        }).collect(Collectors.toSet());
    }

    private static void onAttribute(final Set<String> fieldNames, final DataAtom atom, final BiConsumer<String, XActivityChange> consumer) {
        final Model model = atom.model();
        fieldNames.forEach(attributeName -> {
            /*
             * 字段信息填充
             */
            final MAttribute attribute = model.dbAttribute(attributeName);
            final MField field = onField(attribute, model);
            final XActivityChange change = new XActivityChange();
            change.setKey(UUID.randomUUID().toString());
            change.setActive(Boolean.TRUE);
            change.setSigma(model.dbModel().getSigma());
            change.setLanguage(model.dbModel().getLanguage());
            change.setCreatedAt(LocalDateTime.now());
            if (Objects.nonNull(field)) {
                /*
                 * 字段名 / 字段别称 / 字段类型
                 * 和数据库相关的
                 */
                change.setFieldName(attribute.getName());
                change.setFieldAlias(attribute.getAlias());
                change.setFieldType(field.getType());
                consumer.accept(attributeName, change);
            } else {
                /*
                 * 属性名 / 属性别称 / 属性类型（JsonObject / JsonArray）
                 * 和数据库不相关的
                 */
                final HAttribute aoAttr = atom.attribute(attribute.getName());
                final Class<?> type = Objects.isNull(aoAttr) ? null : aoAttr.field().type();
                change.setFieldName(attribute.getName());
                change.setFieldAlias(attribute.getAlias());
                change.setFieldType(Objects.isNull(type) ? null : type.getName());
                consumer.accept(attributeName, change);
            }
        });
    }

    private static MField onField(final MAttribute attribute, final Model model) {
        final String source = attribute.getSource();
        final String sourceField = attribute.getSourceField();
        /*
         * 读取对应的字段
         */
        final Schema schema = model.schema(source);
        if (Objects.nonNull(schema)) {
            return schema.getField(sourceField);
        } else {
            return null;
        }
    }
}
