package io.vertx.tp.plugin.rpc;

import io.horizon.eon.em.container.IpcType;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.Ruler;
import io.vertx.up.exception.web._424RpcServiceException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.discovery.IpcOrigin;
import io.vertx.up.uca.micro.discovery.Origin;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

class RpcHelper {

    private static final Annal LOGGER = Annal.get(RpcHelper.class);
    private static final Origin ORIGIN = Ut.singleton(IpcOrigin.class);


    static Record getRecord(final JsonObject config) {
        /* Config Verify **/
        Fn.outUp(() -> Fn.safeZero(() -> Ruler.verify(Key.RULE_KEY, config), config),
            LOGGER);
        // Connect remote etcd to check service
        final ConcurrentMap<String, Record> registryData = ORIGIN.getRegistryData();
        final String name = config.getString(Key.NAME);
        final String address = config.getString(Key.ADDR);
        LOGGER.debug(Info.RPC_SERVICE, name, address);
        // Empty Found
        Fn.outWeb(registryData.values().isEmpty(), LOGGER,
            _424RpcServiceException.class, RpcHelper.class,
            name, address);

        // Service status checking
        final Refer container = new Refer();
        // Lookup Record instance
        Observable.fromIterable(registryData.values())
            .filter(Objects::nonNull)
            .filter(item -> Ut.notNil(item.getName()))
            .filter(item -> name.equals(item.getName()) &&
                address.equals(item.getMetadata().getString(Key.PATH)))
            .subscribe(container::add)
            .dispose();
        // Service Not Found
        Fn.outWeb(!container.successed(), LOGGER,
            _424RpcServiceException.class, RpcHelper.class,
            name, address);
        // Address Not Found
        Fn.outWeb(!container.successed(), LOGGER,
            _424RpcServiceException.class, RpcHelper.class,
            name, address);
        final Record record = container.get();
        LOGGER.debug(Info.RPC_FOUND, record.toJson());
        return container.get();
    }

    /**
     * Normalize rpc client standard configuration
     *
     * @param name   service name
     * @param config current configuration
     * @param record found rpc record
     *
     * @return normalized JsonObject for channel
     */
    static JsonObject normalize(
        final String name,
        final JsonObject config,
        final Record record) {
        // Parse
        final JsonObject ssl = getSslConfig(name, config);
        final JsonObject normalized = new JsonObject();
        normalized.put(Key.HOST, record.getLocation().getString(Key.HOST));
        normalized.put(Key.PORT, record.getLocation().getInteger(Key.PORT));
        normalized.put(Key.SSL, ssl);
        return normalized;
    }

    static JsonObject getSslConfig(final String name,
                                   final JsonObject rpcConfig) {
        return Fn.orNull(new JsonObject(), () -> {
            final JsonObject sslConfig = new JsonObject();
            if (rpcConfig.containsKey(Key.SSL) &&
                Boolean.parseBoolean(rpcConfig.getValue(Key.SSL).toString())) {
                if (rpcConfig.containsKey("extension")) {
                    // Non Uniform, Search by name
                    final JsonObject visited = Ut.visitJObject(rpcConfig, "extension", name);
                    sslConfig.mergeIn(visited);
                }
                if (sslConfig.isEmpty()) {
                    // Uniform mode default.
                    sslConfig.mergeIn(Ut.visitJObject(rpcConfig, "uniform"));
                }
            }
            return sslConfig;
        }, rpcConfig);
    }

    /**
     * Build target config
     *
     * @param name service name
     * @param addr target address
     * @param type target type
     *
     * @return JsonObject of config
     */
    static JsonObject on(final String name,
                         final String addr,
                         final IpcType type) {
        final JsonObject config = new JsonObject();
        config.put(Key.NAME, name);
        config.put(Key.ADDR, addr);
        config.put(Key.TYPE, type);
        return config;
    }

    static JsonObject on(final String name,
                         final String addr) {
        return on(name, addr, IpcType.UNITY);
    }
}
