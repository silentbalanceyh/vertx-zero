package io.vertx.up.uca.micro.ipc.client;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.atom.rpc.IpcData;
import io.vertx.up.commune.Envelop;

/**
 * Different implementation by type.
 */
public interface Spear {
    /**
     * Rpc Logical
     */
    Future<Envelop> send(
        final Vertx vertx,
        IpcData data);
}
