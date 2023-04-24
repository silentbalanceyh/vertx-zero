package io.vertx.tp.plugin.rpc;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.servicediscovery.Record;
import io.vertx.tp.plugin.rpc.client.RpcStub;
import io.vertx.tp.plugin.rpc.client.UnityStub;
import io.vertx.up.atom.agent.IpcData;
import io.vertx.up.commune.Envelop;
import io.horizon.eon.em.container.IpcType;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.ipc.DataEncap;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;

public class RpcClientImpl implements RpcClient {

    private static final Annal LOGGER = Annal.get(RpcClientImpl.class);

    private static final String DS_LOCAL_MAP_NAME = "__vertx.IpcClient.{0}";

    private final Vertx vertx;
    private final JsonObject config;
    private final String name;
    protected RpcClient client;
    protected RpcHolder holder;

    public RpcClientImpl(final Vertx vertx,
                         final JsonObject config,
                         final String name) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(config);
        this.vertx = vertx;
        this.config = config;
        this.name = name;
    }

    @Override
    public RpcClient connect(final JsonObject config,
                             final JsonObject data,
                             final Handler<AsyncResult<JsonObject>> handler) {
        final Record record = RpcHelper.getRecord(config);
        // Service Configuration
        final String name = config.getString(Key.NAME);
        final String address = config.getString(Key.ADDR);

        final JsonObject normalized = RpcHelper.normalize(name, config, record);
        this.holder = this.lookupHolder(this.vertx, name, normalized);
        // Get Channel
        final IpcType type = Ut.toEnum(IpcType.class, config.getString(Key.TYPE));
        final RpcStub stub = this.getStub(type);
        // Future result return to client.
        final IpcData request = new IpcData();
        request.setType(type);
        request.setAddress(address);
        // The same operation for request.
        DataEncap.in(request, record);
        DataEncap.in(request, Envelop.success(data));
        LOGGER.info(Info.CLIENT_TRAFFIC, request.toString());
        final Future<JsonObject> future = stub.traffic(request);
        future.onComplete(res -> handler.handle(Future.succeededFuture(res.result())));
        return this;
    }

    @Override
    public RpcClient connect(final String name,
                             final String address,
                             final JsonObject data,
                             final Handler<AsyncResult<JsonObject>> handler) {
        return this.connect(RpcHelper.on(name, address), data, handler);
    }

    @Override
    public RpcClient close() {
        this.holder.close();
        return this;
    }

    private RpcStub getStub(final IpcType type) {
        final RpcStub stub;
        switch (type) {
            case UNITY:
            default: {
                stub = Ut.instance(UnityStub.class, this.holder.getChannel());
            }
            break;
        }
        return stub;
    }

    private RpcHolder lookupHolder(
        final Vertx vertx,
        final String ipcName,
        final JsonObject config) {
        synchronized (this.vertx) {
            final String name = MessageFormat.format(DS_LOCAL_MAP_NAME, config.getString("type"));
            final LocalMap<String, RpcHolder> map = this.vertx.sharedData().getLocalMap(name);
            RpcHolder holder = map.get(ipcName);
            if (null == holder) {
                holder = new RpcHolder(vertx, config, () -> this.removeFromMap(map, name));
                map.put(name, holder);
            } else {
                holder.incRefCount();
            }
            return holder;
        }
    }

    private void removeFromMap(final LocalMap<String, RpcHolder> map,
                               final String name) {
        synchronized (this.vertx) {
            map.remove(name);
            if (map.isEmpty()) {
                map.close();
            }
        }
    }
}
