package io.vertx.mod.is.uca.updater;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.uca.command.Fs;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StoreRename implements StoreUp {
    @Override
    public Future<IDirectory> migrate(final IDirectory directory, final JsonObject directoryJ) {
        final String storePath = directoryJ.getString(KName.STORE_PATH);
        final Fs fs = Ut.instance(directory.getRunComponent());
        return fs.rename(directory.getStorePath(), storePath)
            .compose(renamed -> Ux.future(directory));
    }
}
