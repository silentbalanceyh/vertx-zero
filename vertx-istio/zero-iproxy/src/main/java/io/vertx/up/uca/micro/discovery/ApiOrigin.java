package io.vertx.up.uca.micro.discovery;

import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.EtcdPath;
import io.vertx.up.uca.micro.center.ZeroRegistry;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ApiOrigin implements Origin {

    private final transient ZeroRegistry registry
        = ZeroRegistry.create(this.getClass());

    @Override
    public ConcurrentMap<String, Record> getRegistryData() {
        return this.readData(EtcdPath.ENDPOINT);
    }

    @Override
    public boolean erasing(final Record record) {
        final String host = record.getLocation().getString(HOST);
        final Integer port = record.getLocation().getInteger(PORT);
        final String name = record.getName();
        if (!Ut.netOk(host, port)) {
            this.registry.erasingStatus(name, host, port, this.getPath());
        }
        return true;
    }

    protected EtcdPath getPath() {
        return EtcdPath.ENDPOINT;
    }

    ConcurrentMap<String, Record> readData(final EtcdPath path) {
        // Get End Points.
        final Set<String> results = this.registry.getServices(path);
        // Get records by results.
        final Set<JsonObject> routes = new HashSet<>();
        Observable.fromIterable(results)
            .map(key -> this.registry.getData(path, key, this::getItem))
            .subscribe(routes::addAll).dispose();
        // Build discovery record with metadata to identifier the key.
        final ConcurrentMap<String, Record> map = new ConcurrentHashMap<>();
        // Convert to map
        Observable.fromIterable(routes)
            .subscribe(item -> {
                final String key = item.getJsonObject(META)
                    .getString(ID);
                final Record record = this.createRecord(item);
                map.put(key, record);
            }).dispose();
        return map;
    }

    private Record createRecord(final JsonObject item) {
        final String name = item.getString(NAME);
        final String host = item.getString(HOST);
        final Integer port = item.getInteger(PORT);
        final JsonObject meta = item.getJsonObject(META);
        return HttpEndpoint.createRecord(
            name, host, port, "/*", meta
        );
    }

    private Set<JsonObject> getItem(final String key, final JsonArray value) {
        // Build JsonObject with value array
        final Set<JsonObject> sets = new HashSet<>();
        final String[] meta = key.split(Strings.COLON);
        if (3 == meta.length) {
            Observable.fromIterable(value)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(item -> {
                    final String name = meta[Values.ZERO];
                    final String host = meta[Values.ONE];
                    final String port = meta[Values.TWO];
                    final String id = Ut.encryptSHA256(key + item);
                    return new JsonObject()
                        .put(NAME, name)
                        .put(HOST, host)
                        .put(PORT, Integer.parseInt(port))
                        .put(META, new JsonObject()
                            .put(ID, id)
                            .put(PATH, item));
                })
                .subscribe(sets::add).dispose();
        }
        return sets;
    }
}
