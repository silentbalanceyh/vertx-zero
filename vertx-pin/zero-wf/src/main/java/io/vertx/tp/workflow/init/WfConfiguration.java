package io.vertx.tp.workflow.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WfConfig;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.commune.config.Database;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class WfConfiguration {
    private static final String KEY = "workflow";
    private static final Node<JsonObject> READER = Ut.singleton(ZeroUniform.class);
    private static WfConfig CONFIG;
    private static ProcessEngine ENGINE;

    private WfConfiguration() {
    }

    static void init() {
        final JsonObject configJson = READER.read();
        if (configJson.containsKey(KEY)) {
            final JsonObject configuration = configJson.getJsonObject(KEY, new JsonObject());
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
        final List<String> folders = Ut.ioDirectories(KEY);
        final List<String> results = new ArrayList<>();
        folders.forEach(each -> results.add(KEY + "/" + each));
        return results;
    }
}
