package io.vertx.mod.plugin.etcd.center;

import io.vertx.quiz.EpicBase;

public class EtcdDataTc extends EpicBase {

    public void testData() {
        EtcdData.create(this.getClass());
    }
}
