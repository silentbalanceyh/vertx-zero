package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractMixer implements Mixer {

    protected Vertx vertx;

    AbstractMixer(final Vertx vertx) {
        this.vertx = vertx;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
