package io.mature.extension.uca.log;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuditorHistory extends AbstractAuditor {
    static final Cc<String, Auditor> CC_AUDITOR = Cc.open();

    public AuditorHistory(final JsonObject options) {
        super(options);
    }

    @Override
    public Future<JsonObject> trackAsync(final JsonObject recordN, final JsonObject recordO,
                                         final String serial, final Set<String> ignoreSet) {
        // 创建主方法
        final JsonObject record = Ux.ruleNil(recordN, recordO);
        final JsonObject data = this.initialize(record);
        // 类型计算
        final ChangeFlag flag = Ut.aiFlag(recordN, recordO);
        data.put(KName.TYPE, flag.name());
        data.put(KName.ACTIVE, this.isActive());
        // 新旧值计算
        if (Objects.nonNull(recordO)) {
            data.put(KName.RECORD_OLD, recordO.encode());
        }
        if (Objects.nonNull(recordN)) {
            data.put(KName.RECORD_NEW, recordN.encode());
        }
        data.put(KName.SERIAL, serial);

        // RUN：带插件执行流程
        return Ox.runAop(
            data,                                   // 数据
            record,                                 // 配置
            () -> Ox.pluginActivity(this.options),  // 插件
            this.atom,
            (nil) -> AtRunner.create(this.atom).executeAsync(recordO, recordN, data, ignoreSet)
        );
    }

    @Override
    public Future<JsonArray> trackAsync(final JsonArray recordN, final JsonArray recordO,
                                        final Queue<String> serial, final Set<String> ignoreSet) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        if (Ut.isNil(recordN)) {
            if (Ut.isNotNil(recordO)) {
                // DELETE
                Ut.itJArray(recordO).forEach(item ->
                    futures.add(this.trackAsync(null, item, serial.poll(), ignoreSet)));
            }
        } else {
            // UPDATE / ADD，使用 code 查找
            Ut.itJArray(recordN).forEach(item -> {
                final String code = item.getString(KName.CODE);
                final JsonObject found = Ut.elementFind(recordO, KName.CODE, code);
                if (Objects.isNull(found)) {
                    /*
                     * ADD
                     * 1. code 为空，并且旧的Array也为空
                     * 2. 无法在旧数据中找到新输入的数组，所以为添加
                     */
                    futures.add(this.trackAsync(item, null, serial.poll(), ignoreSet));
                } else {
                    // UPDATE
                    futures.add(this.trackAsync(item, found, serial.poll(), ignoreSet));
                }
            });
        }
        return Fn.combineA(futures);
    }

    /**
     * 创建Active的记录
     *
     * 1. 默认：Active记录，active = true
     * 2. 待确认：Inactive记录，active = false
     *
     * @return {@link Boolean} 记录是否激活
     */
    protected boolean isActive() {
        return Boolean.TRUE;
    }
}
