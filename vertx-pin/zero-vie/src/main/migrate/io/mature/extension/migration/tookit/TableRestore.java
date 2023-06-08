package io.mature.extension.migration.tookit;

import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

import static io.mature.extension.refine.Ox.LOG;

public class TableRestore extends AbstractStatic {
    public TableRestore(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        return this.jooq.fetchAllAsync().compose(Ux::futureA).compose(original -> {
            /*
             * 清除原始数据
             */
            this.tableEmpty(this.jooq.table());
            /*
             * 目录创建
             */
            final String targetFolder = this.ioBackup(config, "data");
            final List<String> files = Ut.ioFiles(targetFolder, ".zero");
            /*
             * 读取文件内容
             */
            final List<Future<JsonArray>> inserted = new ArrayList<>();
            files.stream().map(file -> targetFolder + "/" + file)
                .map(this::ioAsync).forEach(inserted::add);
            return Fn.compressA(inserted).compose(nil -> {
                LOG.Shell.info(this.getClass(), "表名：{0} 数据还原完成！记录数：{1}",
                    this.jooq.table(), String.valueOf(nil.size()));
                /*
                 * 再执行 upsert 将 original 中的数据回写到系统中
                 */
                final List<Future<JsonObject>> upgrade = new ArrayList<>();
                Ut.itJArray(original).map(this::upsertAsync).forEach(upgrade::add);
                return Fn.combineA(upgrade).compose(up -> {
                    LOG.Shell.info(this.getClass(), "表名：{0} 数据还原完成！升级记录：{1}",
                        this.jooq.table(), String.valueOf(up.size()));
                    return Ux.future(config);
                });
            });
        });
    }

    private Future<JsonObject> upsertAsync(final JsonObject data) {
        final String key = data.getString(KName.KEY);
        return Ux.Jooq.on(this.dao()).upsertAsync(key, Ux.fromJson(data, this.ioType()))
            .compose(Ux::futureJ);
    }
}
