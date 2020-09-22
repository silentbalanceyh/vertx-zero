package io.vertx.tp.plugin.etcd.center;

import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.zero.EtcdConfigEmptyException;
import io.vertx.up.exception.zero.EtcdNetworkException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class EtcdData {
    private static final Annal LOGGER = Annal.get(EtcdData.class);
    private static final Node<JsonObject> NODE = Ut.singleton(ZeroUniform.class);
    private static final ConcurrentMap<Class<?>, EtcdData> POOL
            = new ConcurrentHashMap<>();
    /**
     * Config data
     */
    private static final String KEY = "etcd";
    /**
     * It's required for micro service, it means cluster name for current micro services.
     */
    private static final String MICRO = "micro";
    private static final String NODES = "nodes";
    private static final String TIMEOUT = "timeout";
    /**
     * Sub nodes of nodes
     */
    private static final String PORT = "port";
    private static final String HOST = "host";
    /**
     * Etcd Client
     */
    private final transient JsonArray config = new JsonArray();
    private final transient EtcdClient client;
    private final transient Class<?> clazz;
    private transient long timeout = -1;
    private transient String application = Strings.EMPTY;

    private EtcdData(final Class<?> clazz) {
        this.clazz = clazz;
        final Annal logger = Annal.get(clazz);
        // Read configuration
        final JsonObject config = NODE.read();
        if (config.containsKey(KEY)) {
            final JsonObject root = config.getJsonObject(KEY);
            // Verify the data
            Fn.outUp(() -> Fn.onZero(() -> Ruler.verify(KEY, root), root),
                    LOGGER);
            if (root.containsKey(TIMEOUT)) {
                this.timeout = root.getLong(TIMEOUT);
            }
            if (root.containsKey(MICRO)) {
                this.application = root.getString(MICRO);
            }
            // Nodes
            if (root.containsKey(NODES)) {
                this.config.addAll(root.getJsonArray(NODES));
            }
            LOGGER.info(Info.ETCD_TIMEOUT,
                    this.application, this.timeout, this.config.size());
        }
        Fn.outUp(this.config.isEmpty(), logger,
                EtcdConfigEmptyException.class, this.clazz);

        final Set<URI> uris = new HashSet<>();
        final ConcurrentMap<Integer, String> networks
                = new ConcurrentHashMap<>();
        Observable.fromIterable(this.config)
                .filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .filter(item -> item.containsKey(PORT) && item.containsKey(HOST))
                .map(item -> {
                    final Integer port = item.getInteger(PORT);
                    final String host = item.getString(HOST);
                    networks.put(port, host);
                    return "http://" + host + ":" + port;
                })
                .map(URI::create)
                .subscribe(uris::add)
                .dispose();
        // Network checking
        networks.forEach((port, host) ->
                Fn.outUp(!Ut.netOk(host, port), LOGGER,
                        EtcdNetworkException.class, this.getClass(), host, port));
        LOGGER.info(Info.ETCD_NETWORK);
        this.client = new EtcdClient(uris.toArray(new URI[]{}));
    }

    public static EtcdData create(final Class<?> clazz) {
        if (enabled()) {
            LOGGER.info(Info.ETCD_ENABLE);
        }
        return Fn.pool(POOL, clazz, () ->
                Fn.getNull(null, () -> new EtcdData(clazz), clazz));
    }

    /**
     * Whether Etcd Enabled.
     *
     * @return predicated result
     */
    public static boolean enabled() {
        final JsonObject config = NODE.read();
        return null != config && config.containsKey(KEY);
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

    public ConcurrentMap<String, String> readDir(
            final String path,
            final boolean shiftted) {
        /*
         * Ensure Path created when read exception
         */
        return Fn.getJvm(new ConcurrentHashMap<>(), () -> {
            final EtcdKeysResponse.EtcdNode node = this.readNode(path, this.client::getDir);
            return Fn.getJvm(new ConcurrentHashMap<>(), () -> {
                final ConcurrentMap<String, String> result = new ConcurrentHashMap<>();
                /*
                 * Read all nodes information
                 * */
                final List<EtcdKeysResponse.EtcdNode> nodes = node.getNodes();
                for (final EtcdKeysResponse.EtcdNode nodeItem : nodes) {
                    String key = nodeItem.getKey();
                    if (shiftted) {
                        key = key.substring(key.lastIndexOf(Strings.SLASH) + Values.ONE);
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
        return Fn.getJvm(Strings.EMPTY,
                () -> this.readNode(path, this.client::get).getValue(), path);
    }

    private EtcdKeysResponse.EtcdNode readNode(
            final String path,
            final Function<String, EtcdKeyGetRequest> executor) {

        return Fn.getJvm(null, () -> {
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
        return Fn.getJvm(Boolean.FALSE, () -> {
            final EtcdKeyDeleteRequest request = this.client.delete(path);
            final EtcdResponsePromise<EtcdKeysResponse> promise = request.send();
            final EtcdKeysResponse response = promise.get();
            return null != response.getNode();
        }, path);
    }

    public <T> JsonObject write(final String path, final T data, final int ttl) {
        return Fn.getJvm(null, () -> {
            final EtcdKeyPutRequest request = this.client.put(path,
                    Fn.getSemi(data instanceof JsonObject || data instanceof JsonArray,
                            LOGGER,
                            () -> Ut.invoke(data, "encode"),
                            data::toString));
            if (Values.ZERO != ttl) {
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
