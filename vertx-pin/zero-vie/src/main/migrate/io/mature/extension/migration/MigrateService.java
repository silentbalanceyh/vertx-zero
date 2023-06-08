package io.mature.extension.migration;

import io.horizon.eon.em.Environment;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class MigrateService implements Migrate {
    private transient Environment environment;
    private transient HArk ark;

    @Override
    public Migrate bind(final Environment environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public Migrate bind(final HArk ark) {
        this.ark = ark;
        return this;
    }

    private Future<JsonObject> timerAsync(final JsonObject config, final long start) {
        final long end = System.nanoTime();
        final long ms = (end - start) / 1000 / 1000;
        Ox.LOG.Shell.info(this.getClass(), "合计消耗时间：{0} ms", String.valueOf(ms));
        return Ux.future(config);
    }

    @Override
    public Future<JsonObject> restoreAsync(final JsonObject config) {
        /*
         * 元数据库专用升级
         * 1. 删除原始库
         * 2. 矫正Number数据
         * 3. 处理配置项数据（删除异常进入历史库）
         * 4. 修正状态
         * 5. 删除所有待确认
         */
        final long start = System.nanoTime();
        return Ux.future(config)
            /* 001 - 容器环境初始化 */
            .compose(Actor.environment(this.environment).bind(this.ark)::procAsync)
            /* 002 - 数据初始化 */
            .compose(Actor.prepare(this.environment).bind(this.ark)::procAsync)
            /* 003 - 数据还原 */
            .compose(Actor.restore(this.environment).bind(this.ark)::procAsync)
            .compose(nil -> this.timerAsync(config, start));
    }

    @Override
    public Future<JsonObject> backupAsync(final JsonObject config) {
        final long start = System.nanoTime();
        return Ux.future(config)
            /* 001 - 容器环境初始化 */
            .compose(Actor.environment(this.environment).bind(this.ark)::procAsync)
            /* 002.1 - 报表生成 */
            .compose(Actor.report(this.environment).bind(this.ark)::procAsync)
            /* 002.2 - DDL的语句执行 */
            .compose(Actor.ddl(this.environment).bind(this.ark)::procAsync)
            /* 003.1 - 备份当前系统 */
            .compose(Actor.backup(this.environment).bind(this.ark)::procAsync)
            .compose(nil -> this.timerAsync(config, start));
    }
}
