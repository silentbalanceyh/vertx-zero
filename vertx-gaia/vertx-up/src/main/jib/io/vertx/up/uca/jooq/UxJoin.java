package io.vertx.up.uca.jooq;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.pojo.Mirror;
import io.vertx.up.commune.pojo.Mojo;
import io.vertx.up.eon.KName;
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
    private transient final JoinEngine joinder = new JoinEngine();

    private transient final ConcurrentMap<Class<?>, String> pojoMap
        = new ConcurrentHashMap<>();
    private transient Mojo merged = null;
    private transient Set<String> fieldSet = new HashSet<>();

    public UxJoin(final String file) {
        if (Ut.isNotNil(file)) {
            final JsonObject config = Ut.ioJObject(file);
            if (Ut.isNotNil(config)) {
                /*
                 * Only one level for mapping configuration
                 * - field -> sourceTable
                 */
                configuration.mergeIn(config);
            }
        }
    }

    /*
     * T1:
     * Major entity of current dao
     * 1. Dao Cls, default joined field primary key is `key`
     * 2. Dao Cls, you can set the primary key is `field`
     */
    public <T> UxJoin add(final Class<?> daoCls) {
        this.joinder.add(daoCls, this.translate(daoCls, "key"));
        return this;
    }

    public <T> UxJoin add(final Class<?> daoCls, final String field) {
        this.joinder.add(daoCls, this.translate(daoCls, field));
        return this;
    }

    /*
     * When two tables contains two duplicated field such as name
     * This method could rename field as alias
     * T1
     * - name
     * T2
     * - name
     *
     * When you call alias(T2Dao.class, "name", "nameT2")
     * The result should be
     * {
     *      "name": "Value1",
     *      "nameT2": "Value2"
     * }
     */
    public <T> UxJoin alias(final Class<?> daoCls, final String field, final String alias) {
        this.joinder.alias(daoCls, field, alias);
        return this;
    }

    public <T> UxJoin alias(final Class<?> daoCls, final JsonObject fieldMap) {
        Ut.<String>itJObject(fieldMap,
            (alias, field) -> this.alias(daoCls, field, alias));
        return this;
    }

    /*
     * The pojo mapping configuration for Dao class, the pojo configuration came from
     * pojo/pojo.yml
     * 1) pojo/ is the default configuration folder
     * 2) pojo.yml is the parameter of current method `pojo`
     */
    public <T> UxJoin pojo(final Class<?> daoCls, final String pojo) {
        final Mojo created = Mirror.create(UxJoin.class).mount(pojo).mojo();
        this.pojoMap.put(daoCls, pojo);
        if (Objects.isNull(this.merged)) {
            this.merged = new Mojo();
        }
        this.merged.bind(created).bindColumn(created.getInColumn());
        this.pojoMap.forEach(this.joinder::pojo);
        return this;
    }

    /*
     * T2:
     * Joined dao class with field
     * 1. Primary Key Join
     *    T1 ( primary key ) Join T2 on ( ... T2.key )
     * 2. Common Join
     *    T1 ( primary key ) Join T2 on ( ... T2.field )
     */
    public <T> UxJoin join(final Class<?> daoCls) {
        this.joinder.join(daoCls, this.translate(daoCls, KName.KEY));
        return this;
    }

    public <T> UxJoin join(final Class<?> daoCls, final String field) {
        this.joinder.join(daoCls, this.translate(daoCls, field));
        return this;
    }

    // -------------------- Search Operation -----------
    /*
     * searchAsync(JsonObject)
     * searchAsync(Qr)
     */
    public Future<JsonObject> searchAsync(final JsonObject params) {
        return searchAsync(toQr(params));
    }

    public Future<JsonObject> searchAsync(final Ir qr) {
        return this.joinder.searchAsync(qr, this.merged);
    }

    /*
     * countAsync(JsonObject)
     * countAsync(Qr)
     */
    public Future<Long> countAsync(final JsonObject params) {
        return countAsync(toQr(params));
    }

    public Future<Long> countAsync(final Ir qr) {
        return this.joinder.countAsync(qr);
    }

    /*
     * 「Sync」Operation
     * fetch(Qr)
     * fetch(JsonObject)
     *
     * 「Async」Standard Api
     * fetchAsync(Qr)
     * fetchAsync(JsonObject)
     */
    public JsonArray fetch(final Ir qr) {
        return this.joinder.searchArray(qr, this.merged);
    }

    public JsonArray fetch(final JsonObject params) {
        return this.fetch(toQr(new JsonObject().put(Ir.KEY_CRITERIA, params)));
    }

    public Future<JsonArray> fetchAsync(final Ir qr) {
        return Ux.future(this.fetch(qr));
    }

    public Future<JsonArray> fetchAsync(final JsonObject params) {
        return fetchAsync(toQr(new JsonObject().put(Ir.KEY_CRITERIA, params)));
    }

    // -------------------- Crud Operation -----------
    /*
     * Delete Operation Cascade
     * 1) 1 x T1, n x T2
     * 2) 1 x T1, 1 x T2
     * Read Operation Cascade
     * 1) 1 x T1, n x T2
     * 2) 1 x T1, 1 x T2
     * Create/Update Operation
     * 1) 1 x T1 ( Create ), 1 x T2 ( Save )
     * 2) 1 x T1 ( Create ), n x T2 ( Save )
     * 3) 1 x T1 ( Update ), 1 x T2 ( Save )
     * 4) 1 x T1 ( Update ), 1 x T2 ( Save )
     */
    public Future<JsonObject> fetchByIdJAsync(final String key, final String field) {
        return this.joinder.fetchById(key, false, field);
    }

    public Future<JsonObject> fetchByIdAAsync(final String key, final String field) {
        return this.joinder.fetchById(key, true, field);
    }

    public Future<Boolean> removeByIdAsync(final String key) {
        return this.joinder.deleteById(key);
    }

    public Future<JsonObject> insertAsync(final JsonObject data, final String field) {
        return this.joinder.insert(data, field);
    }

    public Future<JsonObject> updateAsync(final String key, final JsonObject data, final String field) {
        return this.joinder.update(key, data, field);
    }

    // -------------------- Private Translate -----------

    private Ir toQr(final JsonObject params) {
        return Objects.isNull(this.merged) ? Ir.create(params) : JqTool.qr(
            params,
            this.merged,
            this.joinder.fieldFirst()              // The first major jooq should be ignored
        );
    }

    private String translate(final Class<?> daoCls, final String field) {
        final String pojoFile = this.pojoMap.get(daoCls);
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
}
