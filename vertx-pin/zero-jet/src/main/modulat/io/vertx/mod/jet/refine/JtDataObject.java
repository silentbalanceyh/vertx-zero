package io.vertx.mod.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.app.KDS;
import io.modello.specification.atom.HRule;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.cv.JtConstant;
import io.vertx.mod.jet.cv.em.WorkerType;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.atom.exchange.DSetting;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Optional;

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

    static HRule toRule(final IService service) {
        if (Objects.isNull(service)) {
            return null;
        } else {
            final String rules = service.getRuleUnique();
            if (Ut.isNil(rules)) {
                return null;
            } else {
                return HRule.of(rules);
            }
        }
    }

    static Database toDatabase(final IService service) {
        final HArk ark = Ke.ark(service.getSigma());
        final KDS<Database> ds = ark.database();
        final JsonObject databaseJ = Ut.toJObject(service.getConfigDatabase());
        // 通道中未配置数据库
        if (Ut.isNil(databaseJ)) {
            return ds.dynamic();
        }
        // 构造通道中数据库
        final Database database = new Database();
        database.fromJson(databaseJ);
        return database;
    }

    @SuppressWarnings("all")
    static JsonObject toOptions(final IService service, final IApi api) {
        final JsonObject options = toOptions(service);
        // TODO: Api configuration
        return options;
    }

    @SuppressWarnings("all")
    static JsonObject toOptions(final IService service, final IJob job) {
        final JsonObject options = toOptions(service);
        // TODO: Job configuration
        return options;
    }

    static JsonObject toOptions(final IService service) {
        final HArk ark = Ke.ark(service.getSigma());
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
            final HApp app = ark.app();
            /* default options, you can add more */
            options.put(KName.NAME, app.name());
            final String sigma = app.option(KName.SIGMA);
            Optional.ofNullable(sigma).ifPresent(value -> options.put(KName.SIGMA, value));
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
        Fn.runAt(Ut.isNil(api.getWorkerClass()),
            () -> api.setWorkerClass(JtConstant.COMPONENT_DEFAULT_WORKER.getName()));
        Fn.runAt(Ut.isNil(api.getWorkerAddress()),
            () -> api.setWorkerAddress(JtConstant.EVENT_ADDRESS));
        Fn.runAt(Ut.isNil(api.getWorkerConsumer()),
            () -> api.setWorkerConsumer(JtConstant.COMPONENT_DEFAULT_CONSUMER.getName()));
        Fn.runAt(Ut.isNil(api.getWorkerType()),
            () -> api.setWorkerType(WorkerType.STD.name()));
    }
}
