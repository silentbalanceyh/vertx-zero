package io.vertx.mod.crud.init;

import io.aeon.experiment.specification.KColumn;
import io.aeon.experiment.specification.KModule;
import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.IxFolder;
import io.vertx.mod.crud.cv.IxMsg;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.booting.HExtension;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.crud.refine.Ix.LOG;

;

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
        final List<String> files = Ut.ioFiles(IxFolder.MODULE, VPath.SUFFIX.JSON);

        files.forEach(file -> {
            /* 1.File absolute path under classpath */
            final String path = IxFolder.MODULE + file;
            final JsonObject configDao = Ut.ioJObject(path);
            final String identifierDefault = file.replace(VString.DOT + VPath.SUFFIX.JSON, VString.EMPTY);

            Fn.runAt(() -> addModule(configDao, identifierDefault), configDao);
        });
        /*
         * Boot: Secondary founding to pick up default configuration
         */
        final Set<HExtension> boots = HExtension.initialize();
        boots.forEach(boot -> {
            /*
             *  Crud Module
             */
            final ConcurrentMap<String, JsonObject> modules = boot.module();
            modules.forEach((moduleKey, json) -> {
                if (!CONFIG_MAP.containsKey(moduleKey) && !ALIAS_MAP.containsKey(moduleKey)) {
                    Fn.runAt(() -> addModule(json, moduleKey), json);
                }
            });
        });
        LOG.Init.info(IxDao.class, "IxDao Finished ! Size = {0}, Uris = {0}",
            CONFIG_MAP.size(), IxConfiguration.getUris().size());
    }

    private static void addModule(final JsonObject data, final String identifierDefault) {
        /* 2. Deserialize to IxConfig object */
        final KModule config = Ut.deserialize(data, KModule.class);
        /* 3. Default Values */
        final String identifier = initValues(config, identifierDefault);
        /* 4. Url & Map */
        IxConfiguration.addUrs(config.getName());
        CONFIG_MAP.put(config.getName(), config);
        ALIAS_MAP.put(identifier, config.getName());
        /* 5. Logger */
        if (DevEnv.devDaoBind()) {
            LOG.Init.info(IxDao.class, IxMsg.INIT_INFO, identifier, config.getName());
        }
    }

    static KModule get(final String actor) {
        final KModule config = CONFIG_MAP.get(actor);
        if (Objects.isNull(config)) {
            final String name = ALIAS_MAP.get(actor);
            if (Ut.isNotNil(name)) {
                LOG.Rest.info(IxDao.class, "Actor: name = `{0}`, identifier = `{1}`", name, actor);
                return CONFIG_MAP.get(name);
            } else {
                return null;
            }
        } else {
            LOG.Rest.info(IxDao.class, "Actor: name = `{0}`", actor);
            return config;
        }
    }

    private static String initValues(final KModule module, final String identifier) {
        /* Default Column */
        if (Objects.isNull(module.getColumn())) {
            final KColumn column = new KColumn();
            column.setDynamic(Boolean.FALSE);
            column.setIdentifier(identifier);
            module.setColumn(column);
        }

        /* Header Processing */
        final JsonObject header = Ut.valueJObject(module.getHeader());
        /* sigma -> X-Sigma */
        Fn.runAt(!header.containsKey(KName.SIGMA),
            () -> header.put(KName.SIGMA, KWeb.HEADER.X_SIGMA));
        /* language -> X-Lang */
        Fn.runAt(!header.containsKey(KName.LANGUAGE),
            () -> header.put(KName.LANGUAGE, KWeb.HEADER.X_LANG));
        module.setHeader(header);

        /* Auditor Processing */
        final KField field = Objects.isNull(module.getField()) ? new KField() : module.getField();
        // key -> key
        Fn.runAt(Objects.isNull(field.getKey()), () -> field.setKey(KName.KEY));
        // created
        final JsonObject created = Ut.valueJObject(field.getCreated());
        Fn.runAt(!created.containsKey(KName.AT), () -> created.put(KName.AT, KName.CREATED_AT));
        Fn.runAt(!created.containsKey(KName.BY), () -> created.put(KName.BY, KName.CREATED_BY));
        field.setCreated(created);
        // updated
        final JsonObject updated = Ut.valueJObject(field.getUpdated());
        Fn.runAt(!updated.containsKey(KName.AT), () -> updated.put(KName.AT, KName.UPDATED_AT));
        Fn.runAt(!updated.containsKey(KName.BY), () -> updated.put(KName.BY, KName.UPDATED_BY));
        field.setUpdated(updated);
        // Module field setting workflow for default
        module.setField(field);
        return identifier;
    }
}
