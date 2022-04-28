package cn.originx.uca.log;

import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.ActivityStatus;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        final List<XActivityChange> changeList = AtDiffer.toChanges(
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
                        .compose(Ut.ifJObject(KName.METADATA, KName.RECORD_NEW, KName.RECORD_OLD))
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
}
