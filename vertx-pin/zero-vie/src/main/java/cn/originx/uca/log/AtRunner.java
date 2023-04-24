package cn.originx.uca.log;

import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.horizon.eon.em.ChangeFlag;
import io.horizon.specification.modeler.HAttribute;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.ActivityStatus;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtRunner {

    private final transient DataAtom atom;

    private AtRunner(final DataAtom atom) {
        this.atom = atom;
    }

    static AtRunner create(final DataAtom atom) {
        return new AtRunner(atom);
    }

    Future<JsonObject> executeAsync(final JsonObject oldRecord, final JsonObject newRecord,
                                    final JsonObject activityJson, final Set<String> ignoreSet) {
        /*
         * 主单据变更类型，
         * 先计算 modelKey
         * 几个核心字段：
         *
         * - modelId：模型对应的 identifier
         * - modelKey：配置项记录的主键值 key
         */
        final XActivity activity = Ut.deserialize(activityJson, XActivity.class);
        if (Objects.isNull(activity.getModelKey())) {
            String foundKey = null;
            if (Objects.nonNull(oldRecord)) {
                foundKey = oldRecord.getString(KName.KEY);
            }
            if (Objects.isNull(foundKey)) {
                if (Objects.nonNull(newRecord)) {
                    foundKey = newRecord.getString(KName.KEY);
                }
            }
            activity.setModelKey(foundKey);
        }
        /*
         * Flag 处理
         * 并且计算改变过后的字段列表信息
         *
         * 1）如果计算的字段列表信息为 Empty，则变更历史跳过
         * 2）如果不为 Empty 才执行下一步操作
         */
        final ChangeFlag flag = Ut.toEnum(ChangeFlag.class, activity.getType());
        final List<XActivityChange> changeList = this.toChanges(
            oldRecord, newRecord,
            flag, this.atom, ignoreSet);
        if (changeList.isEmpty()) {
            /*
             * Bug Fix:
             * 系统中发现了没有任何 changeList 的 Activity 变更历史记录，
             * 此种情况下，对 activityJson 不执行任何操作，证明并没有内容发生变更
             */
            return Ux.future(activityJson);
        } else {
            //变更字段数量
            final long counter;
            //是否生成待确认变更
            final boolean isValid = activityJson.getBoolean(KName.ACTIVE, Boolean.TRUE);
            if (isValid) {
                counter = 0L;
            } else {
                /*
                 * ATOM-04: isConfirm = true 最少有内容才可以执行变更历史
                 */
                final Set<String> confirmFields = this.atom.marker().onConfirm();
                counter = changeList.stream()
                    .filter(change -> confirmFields.contains(change.getFieldName()))
                    .count();
                if (0 < counter) {
                    // 生成待确认，那么 activity 的 active = false
                    activity.setActive(Boolean.FALSE);
                } else {
                    //无变更字段
                    activity.setActive(Boolean.TRUE);
                }
            }
            if (ChangeFlag.UPDATE == flag) {
                /*
                 * 移除BUG修改
                 */
                if (Objects.nonNull(newRecord)) {
                    activity.setRecordNew(newRecord.encode());
                }
            }
            /*
             * updatedAt 时间戳设置
             */
            if (Objects.isNull(activity.getUpdatedAt())) {
                activity.setUpdatedAt(LocalDateTime.now());
            }
            return Ux.Jooq.on(XActivityDao.class).insertAsync(activity)
                .compose(inserted -> {
                    /*
                     * 关联设置和初始化状态信息
                     */
                    changeList.forEach(change -> {
                        change.setActivityId(inserted.getKey());
                        /*
                         * 待确认字段为 PENDING / 未设置字段为 CONFIRMED
                         * ATOM-02:
                         * track = true 的字段为 changeList 中的 PENDING
                         * track = false 的字段为 changeList 中的 SYSTEM
                         */
                        final Set<String> track = this.atom.marker().onTrack();
                        if (track.contains(change.getFieldName())) {
                            change.setStatus(ActivityStatus.PENDING.name());
                        } else {
                            change.setStatus(ActivityStatus.SYSTEM.name());
                        }
                        /*
                         * Bug Fix:
                         * 绑定 createdAt / createdBy
                         * 解决变更历史中没有变更人的核心问题
                         */
                        if (Objects.isNull(change.getCreatedBy())) {
                            change.setCreatedBy(activity.getUpdatedBy());
                        }
                    });
                    /*
                     * 新功能：
                     * 计算当前 activity 是否生成待确认，放到最终的 JsonObject 中，用于判断待确认流程
                     * 默认 isConfirm 为 TRUE
                     */
                    return Ux.Jooq.on(XActivityChangeDao.class)
                        .insertAsync(changeList)
                        .compose(nil -> Ux.future(inserted))
                        .compose(Ux::futureJ)
                        .compose(Fn.ifJObject(KName.METADATA, KName.RECORD_NEW, KName.RECORD_OLD))
                        .compose(activityResult -> {
                            /*
                             * 特殊字段判断是否继续生成待确认
                             * 1）0 < counter：变更字段中包含了生成待确认的字段
                             * 2）0 == counter：变更字段中不包含生成待确认字段
                             */
                            activityResult.put(KName.NEXT, 0 < counter);
                            return Ux.future(activityResult);
                        });
                });
        }
    }

    // ----------------------- Activity Change -----------------------

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
    private List<XActivityChange> toChanges(final JsonObject oldRecord, final JsonObject newRecord,
                                            final ChangeFlag flag, final DataAtom atom, final Set<String> ignores) {
        final List<XActivityChange> changes = new ArrayList<>();
        if (ChangeFlag.NONE != flag) {
            // Vs 引用获取
            if (ChangeFlag.ADD == flag) {

                /* 主记录添加 */
                final Set<String> added = this.onSet(newRecord.fieldNames(), atom, ignores, newRecord::getValue);
                this.onAttribute(added, atom, (attrName, change) -> {
                    change.setType(flag.name());
                    final Object value = newRecord.getValue(attrName);
                    change.setValueNew(value.toString());
                    changes.add(change);
                });
            } else if (ChangeFlag.DELETE == flag) {

                /* 主记录删除 */
                final Set<String> deleted = this.onSet(oldRecord.fieldNames(), atom, ignores, oldRecord::getValue);
                this.onAttribute(deleted, atom, (attrName, change) -> {
                    change.setType(flag.name());
                    final Object value = oldRecord.getValue(attrName);
                    change.setValueOld(value.toString());
                    changes.add(change);
                });
            } else if (ChangeFlag.UPDATE == flag) {

                /* 主记录更新，属性：ADD */
                final Set<String> addedSet = Ut.diff(newRecord.fieldNames(), oldRecord.fieldNames());
                final Set<String> added = this.onSet(addedSet, atom, ignores, newRecord::getValue);
                this.onAttribute(added, atom, (attrName, change) -> {
                    change.setType(ChangeFlag.ADD.name());
                    final Object value = newRecord.getValue(attrName);
                    change.setValueNew(value.toString());
                    changes.add(change);
                });

                /* 主记录更新，属性：DELETE */
                final Set<String> deletedSet = Ut.diff(oldRecord.fieldNames(), newRecord.fieldNames());
                final Set<String> deleted = this.onSet(deletedSet, atom, ignores, oldRecord::getValue);
                this.onAttribute(deleted, atom, (attrName, change) -> {
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
                final Set<String> updated = this.onSet(updatedSet, atom, ignores, oldRecord::getValue, (valueOld, attributeName) -> {
                    final Object valueNew = newRecord.getValue(attributeName);
                    return vs.isChange(valueOld, valueNew, attributeName);
                });
                this.onAttribute(updated, atom, (attrName, change) -> {
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

    // ----------------------- Diff for Activity Changes --------------------

    private Set<String> onSet(final Set<String> fieldSet, final DataAtom atom,
                              final Set<String> ignoreSet,
                              final Function<String, Object> valueFn) {
        final Vs vs = atom.vs();
        return this.onSet(fieldSet, atom, ignoreSet, valueFn, vs::isValue);
    }

    private Set<String> onSet(final Set<String> fieldSet, final DataAtom atom,
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

    private void onAttribute(final Set<String> fieldNames, final DataAtom atom, final BiConsumer<String, XActivityChange> consumer) {
        final Model model = atom.model();
        fieldNames.forEach(attributeName -> {
            /*
             * 字段信息填充
             */
            final MAttribute attribute = model.dbAttribute(attributeName);
            final MField field = this.onField(attribute, model);
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

    private MField onField(final MAttribute attribute, final Model model) {
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
