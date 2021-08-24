package io.vertx.tp.crud.init;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.IxFolder;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.view.KColumn;
import io.vertx.tp.ke.cv.em.DSMode;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.DS;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Dao class initialization
 * plugin/crud/module/ folder singleton
 */
class IxDao {
    /*
     * Logger for IxDao
     */
    private static final Annal LOGGER = Annal.get(IxDao.class);

    private static final ConcurrentMap<String, KModule> CONFIG_MAP =
            new ConcurrentHashMap<>();

    static void init() {
        /*
         * Read all definition files, wall files must be following:
         * <name>.json
         * 1）Each file could define only one module, the filename is module name.
         * 2）Each file must be json format with .json extension, others will be ignored.
         * */
        final List<String> files = Ut.ioFiles(IxFolder.MODULE, FileSuffix.JSON);

        files.forEach(file -> {
            /* 1.File absolute path under classpath */
            final String path = IxFolder.MODULE + file;
            final JsonObject configDao = Ut.ioJObject(path);

            Fn.safeNull(() -> {
                /* 2. Deserialize to IxConfig object */
                final KModule config = Ut.deserialize(configDao, KModule.class);
                /* 3. Logger */
                if (Debugger.isEnabled("curd.dao.file")) {
                    Ix.infoInit(LOGGER, IxMsg.INIT_INFO, path, config.getName());
                }
                /* 4. Default Column */
                final String identifier = file.replace(Strings.DOT + FileSuffix.JSON, Strings.EMPTY);
                if (Objects.isNull(config.getColumn())) {
                    final KColumn column = new KColumn();
                    column.setDynamic(Boolean.FALSE);
                    column.setIdentifier(identifier);
                    config.setColumn(column);
                }
                /* 5. Url & Map */
                IxConfiguration.addUrs(config.getName());
                CONFIG_MAP.put(config.getName(), config);
            }, configDao);
        });
        Ix.infoInit(LOGGER, "IxDao Finished ! Size = {0}, Uris = {0}",
                CONFIG_MAP.size(), IxConfiguration.getUris().size());
    }

    static KModule get(final String actor) {
        Ix.debugRest(LOGGER, "Actor = {0}", actor);
        final KModule config = CONFIG_MAP.get(actor);
        return Fn.getNull(null, () -> config, config);
    }

    static UxJooq get(final KModule config, final MultiMap headers) {
        return Fn.getNull(null, () -> {
            final Class<?> daoCls = config.getDaoCls();
            assert null != daoCls : " Should not be null, check configuration";

            /* 1. Build UxJooq Object */
            final UxJooq dao = get(config, daoCls, headers);
            final String pojo = config.getPojo();

            /* 2. Where existing pojo.yml ( Zero support yml file to define mapping ) */
            if (Ut.notNil(pojo)) {
                dao.on(pojo);
            }
            return dao;
        }, config);
    }

    private static UxJooq get(final KModule module, final Class<?> clazz, final MultiMap headers) {
        final UxJooq dao;
        /*
         * 1. Extract Mode from `IxModule` for data source switching
         */
        final DSMode mode = module.getMode();
        if (DSMode.DYNAMIC == mode) {
            dao = Ke.channelSync(DS.class,
                    /*
                     * `provider` configured
                     */
                    () -> Ux.Jooq.on(clazz),
                    /*
                     * Dynamic Data Source here
                     */
                    ds -> Ux.Jooq.on(clazz, ds.switchDs(headers))
            );
        } else {
            if (DSMode.HISTORY == mode) {
                /*
                 * `orbit` configured
                 */
                dao = Ux.Jooq.ons(clazz);
            } else if (DSMode.EXTENSION == mode) {
                final String modeKey = module.getModeKey();
                if (Ut.isNil(modeKey)) {
                    /*
                     * `provider` configured
                     */
                    dao = Ux.Jooq.on(clazz);
                } else {
                    /*
                     * `<key>` configured
                     */
                    dao = Ux.Jooq.on(clazz, modeKey);
                }
            } else {
                /*
                 * `provider` configured
                 */
                dao = Ux.Jooq.on(clazz);
            }
        }
        return dao;
    }
}
