package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.atom.IsConfig;
import io.vertx.tp.is.cv.em.TypeDirectory;
import io.vertx.tp.is.init.IsPin;
import io.vertx.tp.is.refine.Is;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
    public Future<JsonArray> mkdir(final JsonArray data) {
        this.runRoot(root -> {
            final Set<String> dirSet = new HashSet<>();
            Ut.itJArray(data).forEach(json -> {
                final String path = json.getString(KName.STORE_PATH);
                dirSet.add(Ut.ioPath(root, path));
            });
            dirSet.forEach(Ut::ioMkdir);
        });
        return Ux.future(data);
    }

    private void runRoot(final Consumer<String> fsRoot) {
        final IsConfig config = IsPin.getConfig();
        final String rootPath = config.getStoreRoot();
        if (Ut.isNil(rootPath)) {
            Is.Log.warnPath(this.getClass(), "The `storeRoot` of integration service is null");
        }
        fsRoot.accept(rootPath);
    }

    @Override
    public Future<JsonObject> mkdir(final JsonObject data) {
        this.runRoot(root -> {
            final String path = data.getString(KName.STORE_PATH);
            Ut.ioMkdir(Ut.ioPath(root, path));
        });
        return Ux.future(data);
    }

    @Override
    public Future<JsonArray> synchronize(final JsonArray data, final JsonObject config) {
        // Data Part for I_DIRECTORY Initializing
        return this.syncDirectory(data, config).compose(inserted -> this.mkdir(data));
    }
}
