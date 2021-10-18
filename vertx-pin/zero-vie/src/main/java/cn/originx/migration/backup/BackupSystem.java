package cn.originx.migration.backup;

import cn.originx.migration.tookit.AbstractStatic;
import cn.vertxup.ambient.domain.tables.daos.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.unity.Ux;

public class BackupSystem extends AbstractStatic {

    public BackupSystem(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003.13. 备份基础信息");
        final String folder = "system";
        return Ux.future(config)
            /* XApp */
            .compose(this.backupT(XAppDao.class, folder)::procAsync)
            /* XCategory */
            .compose(this.backupT(XCategoryDao.class, folder)::procAsync)
            /* XNumber */
            .compose(this.backupT(XNumberDao.class, folder)::procAsync)
            /* XMenu */
            .compose(this.backupT(XMenuDao.class, folder)::procAsync)
            /* XTabular */
            .compose(this.backupT(XTabularDao.class, folder)::procAsync)
            /* XModule */
            .compose(this.backupT(XModuleDao.class, folder)::procAsync)
            /* XSource */
            .compose(this.backupT(XSourceDao.class, folder)::procAsync);
    }
}
