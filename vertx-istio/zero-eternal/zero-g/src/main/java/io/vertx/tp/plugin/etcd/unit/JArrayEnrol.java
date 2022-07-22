package io.vertx.tp.plugin.etcd.unit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.etcd.Enrol;
import io.vertx.tp.plugin.etcd.center.EtcdData;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

public class JArrayEnrol implements Enrol<JsonArray> {

    private static final Annal LOGGER = Annal.get(JArrayEnrol.class);

    private transient final EtcdData etcd = EtcdData.create(this.getClass());

    @Override
    public JsonObject write(final String path,
                            final JsonArray entity) {
        final JsonObject data = this.etcd.write(path, entity, 0);
        return Fn.getNull(() -> {
            LOGGER.info(Info.ETCD_WRITE, data, path);
            return data;
        }, data);
    }

    @Override
    public JsonArray read(final String path) {
        return Fn.getNull(() -> {
            final String content = this.etcd.read(path);
            final JsonArray data = new JsonArray(content);
            LOGGER.info(Info.ETCD_READ, data, path);
            return data;
        }, path);
    }
}
