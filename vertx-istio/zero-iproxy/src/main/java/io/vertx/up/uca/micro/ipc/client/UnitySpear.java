package io.vertx.up.uca.micro.ipc.client;

import io.grpc.ManagedChannel;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.tp.ipc.eon.IpcRequest;
import io.vertx.tp.ipc.service.UnityServiceGrpc;
import io.vertx.tp.plugin.rpc.RpcRepdor;
import io.vertx.tp.plugin.rpc.RpcSslTool;
import io.vertx.up.atom.rpc.IpcData;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.micro.ipc.DataEncap;

public class UnitySpear implements Spear {

    @Override
    public Future<Envelop> send(
            final Vertx vertx,
            final IpcData data) {
        // Channel
        final ManagedChannel channel = RpcSslTool.getChannel(vertx, data);
        final UnityServiceGrpc.UnityServiceVertxStub stub
                = UnityServiceGrpc.newVertxStub(channel);
        // Request
        final IpcRequest request = DataEncap.in(data);
        // Call and return to future
        final Promise<Envelop> handler = Promise.promise();
        stub.unityCall(request, response ->
                // Reply
                RpcRepdor.create(this.getClass()).reply(handler, response));
        return handler.future();
    }
}
