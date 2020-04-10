package io.vertx.tp.plugin.etcd.unit;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.etcd.Enrol;
import io.vertx.tp.plugin.etcd.center.EtcdData;
import io.vertx.up.util.Ut;
import io.vertx.quiz.EpicBase;

public class EnrolJsonTc extends EpicBase {

    public void testWrite() {
        final Enrol<JsonObject> enrol = Ut.singleton(JObjectEnrol.class);
        enrol.write("/zero/ipc/192.168.0.100/8080/", this.ioJObject("Store.json"));
    }

    public void testRead() {
        final Enrol<JsonObject> enrol = Ut.singleton(JObjectEnrol.class);
        final JsonObject data = enrol.read("/zero/ipc/192.168.0.100/8080/");
        System.out.println(data);
    }

    public void testDelete() {
        final EtcdData etcdData = EtcdData.create(this.getClass());
        etcdData.delete("zero/ipc/services/ipc-cronus/0.0.0.0/6884");
    }

    public void testReadDir() {
        final EtcdData etcdData = EtcdData.create(this.getClass());
        etcdData.read("zero/endpoint/services");
    }
}
