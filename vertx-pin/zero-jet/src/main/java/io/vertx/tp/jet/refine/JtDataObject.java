package io.vertx.tp.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.aeon.experiment.rule.RuleUnique;
import io.horizon.spi.environment.Ambient;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.cv.JtConstant;
import io.vertx.tp.jet.cv.em.WorkerType;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.exchange.DSetting;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

class JtDataObject {

    static Integration toIntegration(final IService service) {
        if (Objects.isNull(service)) {
            return new Integration();
        } else {
            final JsonObject data = Ut.toJObject(service.getConfigIntegration());
            final Integration integration = new Integration();
            integration.fromJson(data);
            // Dict
            final DSetting dict = JtBusiness.toDict(service);
            if (Objects.nonNull(dict) && !dict.getEpsilon().isEmpty()) {
                /*
                 * Internal binding
                 */
                integration.setEpsilon(dict.getEpsilon());
            }
            /*
             * SSL Options
             */
            // TODO: SSL Options
            return integration;
        }
    }

    static RuleUnique toRule(final IService service) {
        if (Objects.isNull(service)) {
            return null;
        } else {
            final String rules = service.getRuleUnique();
            if (Ut.isNil(rules)) {
                return null;
            } else {
                return Ut.deserialize(rules, RuleUnique.class);
            }
        }
    }

    static Database toDatabase(final IService service) {
        /*
         * 第一数据源
         */
        final String database = service.getConfigDatabase();
        if (Ut.isNil(database)) {
            final JtApp app = Ambient.getApp(service.getSigma());
            if (Objects.nonNull(app)) {
                /*
                 * name, database
                 * 数据库专用
                 */
                return app.getSource();
            } else {
                return null;
            }
        } else {
            /*
             * 异构数据源专用
             */
            return null;
        }
    }

    static Database toDatabase(final Supplier<String> supplier, final Database defaultDatabase) {

        final JsonObject databaseJson = Ut.toJObject(supplier.get());
        if (Ut.isNil(databaseJson)) {
            /*
             * Current app.
             */
            return defaultDatabase;
        } else {
            /*
             * Api `configDatabase` first
             */
            final Database database = new Database();
            database.fromJson(databaseJson);
            return database;
        }
    }

    @SuppressWarnings("all")
    static JsonObject toOptions(final JtApp app, final IApi api, final IService service) {
        final JsonObject options = toOptions(app, service);
        // TODO: Api configuration
        return options;
    }

    @SuppressWarnings("all")
    static JsonObject toOptions(final JtApp app, final IJob job, final IService service) {
        final JsonObject options = toOptions(app, service);
        // TODO: Job configuration
        return options;
    }

    static JsonObject toOptions(final JtApp app, final IService service) {
        /*
         * SERVICE_CONFIG / serviceComponent options
         * here for configuration instead of others
         * {
         *    "name": appName,
         *    "identifier": <id>,
         *    "sigma": <sigma>
         * }
         */
        final JsonObject options = Ut.toJObject(service.getServiceConfig());
        {
            /* default options, you can add more */
            options.put(KName.NAME, app.getName());
            options.put(KName.SIGMA, app.getSigma());
            options.put(KName.IDENTIFIER, service.getIdentifier());
        }
        return options;
    }

    static void initApi(final IApi api) {
        /*
         * Set default value in I_API related to worker
         * workerType
         * workerAddress
         * workerConsumer
         * workerClass
         * workerJs
         */
        Fn.safeSemi(Ut.isNil(api.getWorkerClass()),
            () -> api.setWorkerClass(JtConstant.COMPONENT_DEFAULT_WORKER.getName()));
        Fn.safeSemi(Ut.isNil(api.getWorkerAddress()),
            () -> api.setWorkerAddress(JtConstant.EVENT_ADDRESS));
        Fn.safeSemi(Ut.isNil(api.getWorkerConsumer()),
            () -> api.setWorkerConsumer(JtConstant.COMPONENT_DEFAULT_CONSUMER.getName()));
        Fn.safeSemi(Ut.isNil(api.getWorkerType()),
            () -> api.setWorkerType(WorkerType.STD.name()));
    }
}
