package io.vertx.mod.plugin.etcd.center;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.exception.booting.EtcdConfigEmptyException;
import io.vertx.up.exception.booting.EtcdNetworkException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.requests.EtcdKeyDeleteRequest;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.requests.EtcdKeyPutRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class EtcdData {
    private static final Annal LOGGER = Annal.get(EtcdData.class);
    private static final Cc<Class<?>, EtcdData> CC_ETCD_DATA = Cc.open();
    /**
     * Etcd Client
     */
    private final transient JsonArray config = new JsonArray();
    private final transient EtcdClient client;
    private final transient Class<?> clazz;
    private transient long timeout = -1;
    private transient String application = VString.EMPTY;

    private EtcdData(final Class<?> clazz) {
        this.clazz = clazz;
        final Annal logger = Annal.get(clazz);
        // Read configuration
        if (ZeroStore.is(YmlCore.etcd.__KEY)) {
            final JsonObject root = ZeroStore.option(YmlCore.etcd.__KEY);
            // Verify the data
            Fn.outBug(() -> Fn.bugAt(() -> Ruler.verify(YmlCore.etcd.__KEY, root), root),
                LOGGER);
            if (root.containsKey(YmlCore.etcd.TIMEOUT)) {
                this.timeout = root.getLong(YmlCore.etcd.TIMEOUT);
            }
            if (root.containsKey(YmlCore.etcd.MICRO)) {
                this.application = root.getString(YmlCore.etcd.MICRO);
            }
            // Nodes
            if (root.containsKey(YmlCore.etcd.NODES)) {
                this.config.addAll(root.getJsonArray(YmlCore.etcd.NODES));
            }
            LOGGER.info(Info.ETCD_TIMEOUT,
                this.application, this.timeout, this.config.size());
        }
        Fn.outBoot(this.config.isEmpty(), logger,
            EtcdConfigEmptyException.class, this.clazz);

        final Set<URI> uris = new HashSet<>();
        final ConcurrentMap<Integer, String> networks
            = new ConcurrentHashMap<>();
        Observable.fromIterable(this.config)
            .map(item -> (JsonObject) item)
            .filter(item -> item.containsKey(YmlCore.etcd.nodes.PORT)
                && item.containsKey(YmlCore.etcd.nodes.HOST))
            .map(item -> {
                final Integer port = item.getInteger(YmlCore.etcd.nodes.PORT);
                final String host = item.getString(YmlCore.etcd.nodes.HOST);
                networks.put(port, host);
                return "http://" + host + ":" + port;
            })
            .map(URI::create)
            .subscribe(uris::add)
            .dispose();
        // Network checking
        networks.forEach((port, host) ->
            Fn.outBoot(!Ut.netOk(host, port), LOGGER,
                EtcdNetworkException.class, this.getClass(), host, port));
        LOGGER.info(Info.ETCD_NETWORK);
        this.client = new EtcdClient(uris.toArray(new URI[]{}));
    }

    public static EtcdData create(final Class<?> clazz) {
        if (enabled()) {
            LOGGER.info(Info.ETCD_ENABLE);
        }
        return CC_ETCD_DATA.pick(() -> Fn.runOr(null, () -> new EtcdData(clazz), clazz), clazz);
        // return Fn.po?l(POOL, clazz, () -> Fn.getNull(null, () -> new EtcdData(clazz), clazz));
    }

    /**
     * Whether Etcd Enabled.
     *
     * @return predicated result
     */
    public static boolean enabled() {
        return ZeroStore.is(YmlCore.etcd.__KEY);
    }

    public EtcdClient getClient() {
        return this.client;
    }

    public JsonArray getConfig() {
        return this.config;
    }

    public String getApplication() {
        return this.application;
    }

    public Class<?> target() {
        return this.clazz;
    }

    public ConcurrentMap<String, String> readDir(
        final String path,
        final boolean shiftted) {
        /*
         * Ensure Path created when read exception
         */
        return Fn.failOr(new ConcurrentHashMap<>(), () -> {
            final EtcdKeysResponse.EtcdNode node = this.readNode(path, this.client::getDir);
            return Fn.failOr(new ConcurrentHashMap<>(), () -> {
                final ConcurrentMap<String, String> result = new ConcurrentHashMap<>();
                /*
                 * Read all nodes information
                 * */
                final List<EtcdKeysResponse.EtcdNode> nodes = node.getNodes();
                for (final EtcdKeysResponse.EtcdNode nodeItem : nodes) {
                    String key = nodeItem.getKey();
                    if (shiftted) {
                        key = key.substring(key.lastIndexOf(VString.SLASH) + VValue.ONE);
                    }
                    result.put(key, nodeItem.getValue());
                }
                return result;
            }, node);
        }, path);
    }

    @SuppressWarnings("unused")
    private void ensurePath(final String path) {
        if (0 <= path.lastIndexOf('/')) {
            final String parent = path.substring(0, path.lastIndexOf('/'));
            try {
                // Trigger Key not found
                final EtcdKeysResponse response =
                    this.client.getDir(parent).send().get();
                if (null != response) {
                    this.client.putDir(path).send();
                    this.ensurePath(parent);
                }
            } catch (final EtcdException | EtcdAuthenticationException
                           | IOException | TimeoutException ex) {
                this.ensurePath(parent);
            }
        }
    }

    public String readData(
        final String path
    ) {
        return Fn.failOr(VString.EMPTY,
            () -> this.readNode(path, this.client::get).getValue(), path);
    }

    private EtcdKeysResponse.EtcdNode readNode(
        final String path,
        final Function<String, EtcdKeyGetRequest> executor) {

        return Fn.failOr(null, () -> {
            final EtcdKeyGetRequest request = executor.apply(path);
            /*
             * Set timeout parameters
             */
            if (-1 != this.timeout) {
                request.timeout(this.timeout, TimeUnit.SECONDS);
            }
            final EtcdResponsePromise<EtcdKeysResponse> promise = request.send();
            final EtcdKeysResponse response = promise.get();
            return response.getNode();
        }, path);
    }

    public String read(final String path) {
        final EtcdKeysResponse.EtcdNode node = this.readNode(path, this.client::get);
        return null == node ? null : node.getValue();
    }

    public boolean delete(final String path) {
        return Fn.failOr(Boolean.FALSE, () -> {
            final EtcdKeyDeleteRequest request = this.client.delete(path);
            final EtcdResponsePromise<EtcdKeysResponse> promise = request.send();
            final EtcdKeysResponse response = promise.get();
            return null != response.getNode();
        }, path);
    }

    public <T> JsonObject write(final String path, final T data, final int ttl) {
        return Fn.failOr(null, () -> {
            final EtcdKeyPutRequest request = this.client.put(path,
                Fn.runOr(data instanceof JsonObject || data instanceof JsonArray,
                    LOGGER,
                    () -> Ut.invoke(data, "encode"),
                    data::toString));
            if (VValue.ZERO != ttl) {
                request.ttl(ttl);
            }
            /*
             * Set timeout parameters
             */
            if (-1 != this.timeout) {
                request.timeout(this.timeout, TimeUnit.SECONDS);
            }
            final EtcdResponsePromise<EtcdKeysResponse> promise = request.send();
            final EtcdKeysResponse response = promise.get();
            return Ut.serializeJson(response.getNode());
        }, path, data);
    }
}
