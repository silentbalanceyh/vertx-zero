package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public final class UxJoin {

    private transient final JsonObject configuration = new JsonObject();
    private transient final JqJoinder joinder = new JqJoinder();

    private transient final ConcurrentMap<Class<?>, String> POJO_MAP
            = new ConcurrentHashMap<>();
    private transient Mojo merged = null;
    private transient Set<String> fieldSet = new HashSet<>();

    public UxJoin(final String file) {
        if (Ut.notNil(file)) {
            final JsonObject config = Ut.ioJObject(file);
            if (Ut.notNil(config)) {
                /*
                 * Only one level for mapping configuration
                 * - field -> sourceTable
                 */
                configuration.mergeIn(config);
            }
        }
    }

    public <T> UxJoin add(final Class<?> daoCls) {
        this.joinder.add(daoCls, this.translate(daoCls, "key"));
        return this;
    }

    public <T> UxJoin add(final Class<?> daoCls, final String field) {
        this.joinder.add(daoCls, this.translate(daoCls, field));
        return this;
    }

    public <T> UxJoin pojo(final Class<?> daoCls, final String pojo) {
        final Mojo created = Mirror.create(UxJoin.class).mount(pojo).mojo();
        this.POJO_MAP.put(daoCls, pojo);
        if (Objects.isNull(this.merged)) {
            this.merged = new Mojo();
        }
        this.merged.bind(created).bindColumn(created.getInColumn());
        return this;
    }

    public <T> UxJoin join(final Class<?> daoCls) {
        this.joinder.join(daoCls, this.translate(daoCls, "key"));
        return this;
    }

    public <T> UxJoin join(final Class<?> daoCls, final String field) {
        this.joinder.join(daoCls, this.translate(daoCls, field));
        return this;
    }

    private String translate(final Class<?> daoCls, final String field) {
        final String pojoFile = this.POJO_MAP.get(daoCls);
        if (Ut.isNil(pojoFile)) {
            return field;
        } else {
            final Mojo mojo = Mirror.create(UxJoin.class).mount(pojoFile).mojo();
            if (Objects.isNull(mojo)) {
                return field;
            } else {
                final String translated = mojo.getIn().get(field);
                if (Ut.isNil(translated)) {
                    return field;
                } else {
                    return translated;
                }
            }
        }
    }

    // -------------------- Search Operation -----------
    /* (Async / Sync) Sort, Projection, Criteria, Pager Search Operations */

    public Future<JsonObject> searchAsync(final JsonObject params) {
        return searchAsync(toQr(params));
    }

    private Qr toQr(final JsonObject params) {
        return Objects.isNull(this.merged) ? Qr.create(params) : JqTool.qr(
                params,
                this.merged,
                this.joinder.firstFields()              // The first major jooq should be ignored
        );
    }

    public Future<JsonObject> searchAsync(final Qr qr) {
        this.POJO_MAP.forEach(this.joinder::pojo);
        return this.joinder.searchPaginationAsync(qr, this.merged);
    }

    public Future<Long> countAsync(final JsonObject params) {
        return countAsync(toQr(params));
    }

    public Future<Long> countAsync(final Qr qr) {
        this.POJO_MAP.forEach(this.joinder::pojo);
        return this.joinder.countPaginationAsync(qr);
    }

    public JsonArray fetch(final Qr qr) {
        this.POJO_MAP.forEach(this.joinder::pojo);
        return this.joinder.searchArray(qr, this.merged);
    }

    public JsonArray fetch(final JsonObject params) {
        return this.fetch(toQr(new JsonObject().put(Qr.KEY_CRITERIA, params)));
    }

    public Future<JsonArray> fetchAsync(final Qr qr) {
        return Ux.future(this.fetch(qr));
    }

    public Future<JsonArray> fetchAsync(final JsonObject params) {
        return fetchAsync(toQr(new JsonObject().put(Qr.KEY_CRITERIA, params)));
    }
}
