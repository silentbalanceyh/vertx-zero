package io.vertx.tp.is.uca.command;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.atom.IsConfig;
import io.vertx.tp.is.cv.IsFolder;
import io.vertx.tp.is.cv.em.TypeDirectory;
import io.vertx.tp.is.init.IsPin;
import io.vertx.tp.is.refine.Is;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsDefault extends AbstractFs {

    @Override
    public IDirectory initTree(final JsonObject directoryJ) {
        /*
         * Store
         */
        final IDirectory directory = Ux.fromJson(directoryJ, IDirectory.class);
        directory.setCode(Ut.encryptMD5(directory.getStorePath()));
        return directory.setType(TypeDirectory.STORE.name());
    }

    @Override
    public void initTrash() {
        this.runRoot(root -> {
            final String rootTrash = Ut.ioPath(root, IsFolder.TRASH_FOLDER);
            Ut.cmdMkdir(rootTrash);
        });
    }

    @Override
    public Future<JsonArray> mkdir(final JsonArray data) {
        this.runRoot(data, dirSet -> dirSet.forEach(Ut::cmdMkdir));
        return Ux.future(data);
    }

    @Override
    public Future<JsonObject> mkdir(final JsonObject data) {
        this.runRoot(root -> {
            final String path = data.getString(KName.STORE_PATH);
            Ut.cmdMkdir(Ut.ioPath(root, path));
        });
        return Ux.future(data);
    }


    @Override
    public Future<JsonArray> synchronize(final JsonArray data, final JsonObject config) {
        // Data Part for I_DIRECTORY Initializing
        return this.initialize(data, config).compose(this::mkdir);
    }

    @Override
    public Future<JsonArray> rm(final JsonArray data) {
        this.runRoot(data, dirSet -> dirSet.forEach(Ut::cmdRm));
        return Ux.future(data);
    }

    @Override
    public Future<Boolean> rm(final Collection<String> storeSet) {
        this.runRoot(root -> storeSet.forEach(path -> Ut.cmdRm(Ut.ioPath(root, path))));
        return Ux.futureT();
    }

    @Override
    public Future<JsonObject> rm(final JsonObject data) {
        this.runRoot(root -> {
            final String path = data.getString(KName.STORE_PATH);
            Ut.cmdRm(Ut.ioPath(root, path));
        });
        return Ux.future(data);
    }

    @Override
    public Future<Boolean> rename(final ConcurrentMap<String, String> transfer) {
        if (!transfer.isEmpty()) {
            this.runRoot(root -> transfer.forEach((from, to) -> this.runRename(root, from, to)));
        }
        return Ux.futureT();
    }

    @Override
    public Future<Boolean> rename(final String from, final String to) {
        this.runRoot(root -> this.runRename(root, from, to));
        return Ux.futureT();
    }

    @Override
    public Future<Boolean> rename(final Kv<String, String> kv) {
        return this.rename(kv.getKey(), kv.getValue());
    }

    // ----------------- Run Private Method for Folder Calculation -----------------

    private void runRename(final String root, final String from, final String to) {
        final String fromPath = Ut.ioPath(root, from);
        final String toPath = Ut.ioPath(root, to);
        Ut.cmdRename(fromPath, toPath);
    }

    private void runRoot(final Consumer<String> fsRoot) {
        final IsConfig config = IsPin.getConfig();
        final String rootPath = config.getStoreRoot();
        if (Ut.isNil(rootPath)) {
            Is.Log.warnPath(this.getClass(), "The `storeRoot` of integration service is null");
        }
        fsRoot.accept(rootPath);
    }

    private void runRoot(final JsonArray data, final Consumer<Set<String>> fsRoot) {
        this.runRoot(root -> {
            final Set<String> dirSet = new HashSet<>();
            Ut.itJArray(data).forEach(json -> {
                final String path = json.getString(KName.STORE_PATH);
                dirSet.add(Ut.ioPath(root, path));
            });
            fsRoot.accept(dirSet);
        });
    }
}
