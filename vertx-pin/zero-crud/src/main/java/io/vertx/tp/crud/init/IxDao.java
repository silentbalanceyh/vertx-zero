package io.vertx.tp.crud.init;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.IxFolder;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.specification.*;
import io.vertx.tp.ke.cv.em.DSMode;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.DS;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Debugger;
import io.vertx.up.uca.jooq.UxJoin;
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
    private static final ConcurrentMap<String, KModule> CONFIG_MAP =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> ALIAS_MAP =
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
                    Ix.Log.init(IxDao.class, IxMsg.INIT_INFO, path, config.getName());
                }
                /* 4. Default Values */
                final String identifier = initValues(config, file);
                /* 5. Url & Map */
                IxConfiguration.addUrs(config.getName());
                CONFIG_MAP.put(config.getName(), config);
                ALIAS_MAP.put(identifier, config.getName());
            }, configDao);
        });
        Ix.Log.init(IxDao.class, "IxDao Finished ! Size = {0}, Uris = {0}",
            CONFIG_MAP.size(), IxConfiguration.getUris().size());
    }

    static KModule get(final String actor) {
        final KModule config = CONFIG_MAP.get(actor);
        if (Objects.isNull(config)) {
            final String name = ALIAS_MAP.get(actor);
            if (Ut.notNil(name)) {
                Ix.Log.rest(IxDao.class, "Actor: name = `{0}`, identifier = `{1}`", name, actor);
                return CONFIG_MAP.get(name);
            } else {
                return null;
            }
        } else {
            Ix.Log.rest(IxDao.class, "Actor: name = `{0}`", actor);
            return config;
        }
    }

    static UxJoin get(final KModule module, final KModule connect) {
        return Fn.getNull(null, () -> {
            /* 1. Build UxJoin Object */
            final UxJoin dao = Ux.Join.on();

            /* 2. Module */
            final String pojo = module.getPojo();
            dao.add(module.getDaoCls());
            if (Ut.notNil(pojo)) {
                dao.pojo(module.getDaoCls(), pojo);
            }

            /* 3. Connect */
            final KJoin join = module.getConnect();
            final KPoint point = join.point(connect.getIdentifier());
            assert null != point;
            final Class<?> daoCls = connect.getDaoCls();
            dao.join(daoCls, point.getKeyJoin());

            /* 4. Alias */
            final JsonObject synonym = point.getSynonym();
            if (Ut.notNil(synonym)) {
                Ut.<String>itJObject(synonym, (aliasField, field) -> dao.alias(daoCls, field, aliasField));
            }

            /* 5. Connect Joined pojo */
            final String pojoS = connect.getPojo();
            if (Ut.notNil(pojoS)) {
                dao.pojo(connect.getDaoCls(), pojoS);
            }
            return dao;
        }, module, connect);
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

    private static String initValues(final KModule module, final String file) {
        /* Default Column */
        final String identifier = file.replace(Strings.DOT + FileSuffix.JSON, Strings.EMPTY);
        if (Objects.isNull(module.getColumn())) {
            final KColumn column = new KColumn();
            column.setDynamic(Boolean.FALSE);
            column.setIdentifier(identifier);
            module.setColumn(column);
        }

        /* Header Processing */
        final JsonObject header = Ut.sureJObject(module.getHeader());
        /* sigma -> X-Sigma */
        Fn.safeSemi(!header.containsKey(KName.SIGMA),
            () -> header.put(KName.SIGMA, ID.Header.X_SIGMA));
        /* language -> X-Lang */
        Fn.safeSemi(!header.containsKey(KName.LANGUAGE),
            () -> header.put(KName.LANGUAGE, ID.Header.X_LANG));
        module.setHeader(header);

        /* Auditor Processing */
        final KField field = Objects.isNull(module.getField()) ? new KField() : module.getField();
        // key -> key
        Fn.safeSemi(Objects.isNull(field.getKey()), () -> field.setKey(KName.KEY));
        // created
        final JsonObject created = Ut.sureJObject(field.getCreated());
        Fn.safeSemi(!created.containsKey(KName.AT), () -> created.put(KName.AT, KName.CREATED_AT));
        Fn.safeSemi(!created.containsKey(KName.BY), () -> created.put(KName.BY, KName.CREATED_BY));
        field.setCreated(created);
        // updated
        final JsonObject updated = Ut.sureJObject(field.getUpdated());
        Fn.safeSemi(!updated.containsKey(KName.AT), () -> updated.put(KName.AT, KName.UPDATED_AT));
        Fn.safeSemi(!updated.containsKey(KName.BY), () -> updated.put(KName.BY, KName.UPDATED_BY));
        field.setUpdated(updated);
        // Module field setting workflow for default
        module.setField(field);
        return identifier;
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
