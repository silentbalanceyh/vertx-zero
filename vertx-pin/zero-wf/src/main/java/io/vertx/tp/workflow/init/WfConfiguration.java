package io.vertx.tp.workflow.init;

import cn.vertxup.workflow.domain.tables.daos.WFlowDao;
import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.atom.WfConfig;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.commune.config.Database;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.jooq.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class WfConfiguration {
    private static final Node<JsonObject> READER = Ut.singleton(ZeroUniform.class);
    private static final ConcurrentMap<String, WFlow> FLOW_POOL = new ConcurrentHashMap<>();
    private static WfConfig CONFIG;
    private static ProcessEngine ENGINE;

    private WfConfiguration() {
    }

    static void init() {
        final JsonObject configJson = READER.read();
        if (configJson.containsKey(WfCv.ROOT_FOLDER)) {
            final JsonObject configuration = configJson.getJsonObject(WfCv.ROOT_FOLDER, new JsonObject());
            Wf.Log.infoInit(WfConfiguration.class, "The workflow engine will be initialized!! `{0}`",
                configuration.encode());
            CONFIG = Ut.deserialize(configuration, WfConfig.class);
        }
    }

    /*
     * Camunda Engine Creating
     */
    static ProcessEngine camunda() {
        Objects.requireNonNull(CONFIG);
        if (Objects.isNull(ENGINE)) {
            final Database database = CONFIG.camundaDatabase();
            Objects.requireNonNull(database);
            ENGINE = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                // Fix Issue:
                // org.camunda.bpm.engine.ProcessEngineException: historyLevel mismatch: configuration says HistoryLevelAudit(name=audit, id=2) and database says HistoryLevelFull(name=full, id=3)
                .setHistory(HistoryLevel.HISTORY_LEVEL_FULL.getName())     // none, audit, full, activity
                .setProcessEngineName(CONFIG.getName())
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcUrl(database.getJdbcUrl())
                .setJdbcDriver(database.getDriverClassName())
                .setJdbcUsername(database.getUsername())
                .setJdbcPassword(database.getPassword())
                .setJobExecutorActivate(true)
                .buildProcessEngine();
        }
        return ENGINE;
    }

    static List<String> camundaResources() {
        final List<String> folders = Ut.ioDirectories(WfCv.ROOT_FOLDER);
        final List<String> results = new ArrayList<>();
        folders.forEach(each -> results.add(WfCv.ROOT_FOLDER + "/" + each));
        return results;
    }

    /*
     * W_FLOW Cached when started ( Query once )
     */
    static Future<Boolean> init(final Vertx vertx) {
        final Configuration configuration = Ke.getConfiguration();
        final WFlowDao flowDao = new WFlowDao(configuration, vertx);
        return flowDao.findAll().compose(flows -> {
            Wf.Log.infoInit(WfConfiguration.class, "Flow definitions: {0}", flows.size());
            FLOW_POOL.putAll(Ut.elementZip(flows, WFlow::getCode, flow -> flow));
            return Ux.futureT();
        });
    }

    static WFlow workflow(final String code) {
        return FLOW_POOL.get(code);
    }
}
