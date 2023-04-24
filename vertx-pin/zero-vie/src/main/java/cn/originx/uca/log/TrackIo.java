package cn.originx.uca.log;

import cn.originx.refine.Ox;
import cn.originx.uca.code.Numeration;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.error._400TrackingErrorException;
import io.vertx.tp.optic.plugin.AspectPlugin;
import io.vertx.up.exception.WebException;
import io.aeon.experiment.mixture.HDao;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/*
 * 执行 Activity
 */
public class TrackIo {
    private static final Cc<String, TrackIo> CC_TRACK = Cc.open();
    private final transient DataAtom atom;

    @SuppressWarnings("all")    // Temp
    private final transient HDao dao;
    private final transient Boolean isTrack;
    private final transient Set<String> ignoreSet;

    private TrackIo(final DataAtom atom, final HDao dao) {
        // 打开注入
        Ox.numerationStd();
        this.atom = atom;
        this.dao = dao;
        /*
         * 重新绑定
         */
        this.dao.mount(atom);
        this.isTrack = atom.trackable();
        /*
         * ATOM-001
         * 变更历史生成的标准流程，track = false 会直接被忽略掉
         */
        this.ignoreSet = Ox.ignorePure(atom);
    }

    public static TrackIo create(final DataAtom atom, final HDao dao) {
        return CC_TRACK.pick(() -> new TrackIo(atom, dao), atom.identifier());
        // Fn.po?l(TRACK_POOL, atom.identifier(), () -> new TrackIo(atom, dao));
    }

    public Future<JsonArray> procAsync(final JsonArray newArray,
                                       final JsonArray oldArray,
                                       final JsonObject options,
                                       final Integer counter) {
        final JsonObject optJson = Ut.valueJObject(options);
        final AspectPlugin plugin = Ox.pluginActivity(optJson);
        final Set<String> trackFields = this.atom.marker().onTrack();
        if (Objects.isNull(plugin) || !this.isTrack || trackFields.isEmpty()) {
            final JsonArray response = Objects.isNull(newArray) ? oldArray : newArray;
            return Ux.future(response);
        } else {
            if (Objects.isNull(newArray) && Objects.isNull(oldArray)) {
                final WebException error = new _400TrackingErrorException(this.getClass());
                return Future.failedFuture(error);
            } else {
                final Numeration numeration = Numeration.service(this.atom.sigma());
                return numeration.clazz(XActivity.class, counter).compose(serials -> {
                    /*
                     * 初始化 Auditor
                     * 直接生成合法变更历史
                     */
                    final Auditor auditor = Auditor.history(this.atom, options);
                    return auditor.trackAsync(newArray, oldArray, serials, this.ignoreSet);
                }).compose(activities -> {
                    if (Objects.isNull(newArray)) {
                        /* 删除 */
                        return Ux.future(oldArray);
                    } else {
                        /* 添加 / 更新 */
                        return Ux.future(newArray);
                    }
                });
            }
        }
    }

    /*
     * 变更历史生成器，主要用于生成非集成部分变更历史
     */
    public Future<JsonObject> procAsync(final JsonObject newRecord,
                                        final JsonObject oldRecord,
                                        final JsonObject options) {
        final JsonObject optJson = Ut.valueJObject(options);
        final AspectPlugin plugin = Ox.pluginActivity(optJson);
        /*
         * 变更历史记录条件
         * 1）plugin 已配置
         * 2）isTrack = true
         * 3）发生了变更：
         * - ADD - newRecord 有值，oldRecord = null
         * - DELETE - 和 ADD 相反
         * - UPDATE - 计算最终结果
         */
        final Set<String> trackFields = this.atom.marker().onTrack();
        if (Objects.isNull(plugin) || !this.isTrack || trackFields.isEmpty()) {
            final JsonObject response = Objects.isNull(newRecord) ? oldRecord : newRecord;
            return Ux.future(response);
        } else {
            /*
             * 新旧数据专用
             */
            if (Objects.isNull(newRecord) && Objects.isNull(oldRecord)) {
                final WebException error = new _400TrackingErrorException(this.getClass());
                return Future.failedFuture(error);
            } else {
                final Numeration numeration = Numeration.service(this.atom.sigma());
                return numeration.clazz(XActivity.class).compose(serial -> {
                    /*
                     * 初始化 Auditor
                     */
                    final Auditor auditor = Auditor.history(this.atom, options);
                    return auditor.trackAsync(newRecord, oldRecord, serial, this.ignoreSet);
                }).compose(activities -> {
                    if (Objects.isNull(newRecord)) {
                        /* 删除 */
                        return Ux.future(oldRecord);
                    } else {
                        /* 添加 / 更新 */
                        return Ux.future(newRecord);
                    }
                });
            }
        }
    }
}
