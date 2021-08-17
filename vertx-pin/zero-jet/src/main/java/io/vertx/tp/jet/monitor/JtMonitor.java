package io.vertx.tp.jet.monitor;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.jet.cv.JtMsg;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/*
 * The monitor of workflow here.
 */
public class JtMonitor {

    private transient final Annal logger;
    private transient final String name;
    private transient final JtAtomic atomic = new JtAtomic();

    private JtMonitor(final Class<?> clazz) {
        this.name = clazz.getName();
        this.logger = Annal.get(clazz);
    }

    public static JtMonitor create(final Class<?> clazz) {
        return Fn.pool(Pool.MONITORS, clazz, () -> new JtMonitor(clazz));
    }

    // ---------------- Agent
    public void agentConfig(final JsonObject config) {
        this.atomic.start(this.logger, config);
    }

    // ---------------- Worker
    public void workerStart() {
        this.atomic.worker(this.logger);
    }

    public void workerFailure() {
        this.atomic.workerFailure(this.logger);
    }

    public void workerDeploying(final Integer instances, final String name) {
        this.atomic.workerDeploying(this.logger, instances, name);
    }

    public void workerDeployed(final Integer instances, final String name) {
        this.atomic.workerDeployed(this.logger, instances, name);
    }

    public void receiveData(final String identifier, final JtUri uri) {
        Jt.infoWeb(this.logger, JtMsg.CONSUME_MESSAGE, identifier, uri.method(), uri.path());
        Jt.infoWeb(this.logger, JtMsg.CONSUME_API, ((JsonObject) Ut.serializeJson(uri.api())).encode());
        Jt.infoWeb(this.logger, JtMsg.CONSUME_SERVICE, ((JsonObject) Ut.serializeJson(uri.service())).encode());
        Jt.infoWeb(this.logger, JtMsg.CONSUME_WORKER, ((JsonObject) Ut.serializeJson(uri.worker())).encode());
    }

    // ---------------- Ingest

    // ---------------- Aim
    public void aimEngine(final HttpMethod method, final String path, final JsonObject data) {
        Jt.infoWeb(this.logger, JtMsg.WEB_ENGINE, method, path, data.encode());
    }

    public void aimSend(final JsonObject data, final String address) {
        Jt.infoWeb(this.logger, JtMsg.WEB_SEND, data.encode(), address);
    }

    // ---------------- Channel
    public void channelHit(final Class<?> clazz) {
        Jt.infoWeb(this.logger, JtMsg.CHANNEL_SELECT, null == clazz ? null : clazz.getName());
    }

    public void componentHit(final Class<?> componentClass, final Class<?> recordClass) {
        Jt.infoWeb(this.logger, JtMsg.COMPONENT_SELECT,
                null == componentClass ? null : componentClass.getName(),
                null == recordClass ? null : recordClass.getName());
    }
}
