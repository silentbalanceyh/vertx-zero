package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.cv.em.TypeDirectory;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsDefault extends AbstractFs {
    @Override
    protected IDirectory syncDirectory(final IDirectory directory) {
        /*
         * Store
         */
        directory.setCode(Ut.encryptMD5(directory.getStorePath()));
        return directory.setType(TypeDirectory.STORE.name());
    }

    @Override
    public Future<JsonArray> mkdir(final JsonArray data, final JsonObject config) {
        // Data Part for I_DIRECTORY Initializing
        return this.syncDirectory(data, config).compose(inserted -> {
            final Set<String> dirSet = new HashSet<>();
            final JsonObject store = config.getJsonObject(KName.STORE);
            Ut.itJArray(inserted).forEach(json -> {
                final String root = store.getString(KName.STORE_ROOT);
                final String path = json.getString(KName.STORE_PATH);
                dirSet.add(Ut.ioPath(root, path));
            });
            dirSet.forEach(Ut::ioMkdir);
            return Ux.future(inserted);
        });
    }
}
