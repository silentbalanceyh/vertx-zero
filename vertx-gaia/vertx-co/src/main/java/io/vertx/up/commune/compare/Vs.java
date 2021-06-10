package io.vertx.up.commune.compare;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Diff Container ( New Version )
 *
 * ### 1. Intro
 *
 * Store definition of each model for diff ( comparing workflow )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Vs implements Serializable {

    private static final ConcurrentMap<String, Vs> POOL_VS = new ConcurrentHashMap<>();
    /**
     * Attribute Stored
     * name = type
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. type is the java type that could be reflect.
     */
    private transient final ConcurrentMap<String, Class<?>> mapType = new ConcurrentHashMap<>();

    /**
     * Subset Attribute Stored
     * name = Set
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. The data type is fixed: JsonObject / JsonArray.
     * 3. The `Set` means the attribute names of JsonObject or JsonArray here.
     */
    private transient final ConcurrentMap<String, Set<String>> mapSubtype = new ConcurrentHashMap<>();
    /**
     * Ignored field that could be set from object.
     *
     * 1. Default ignore set
     * 2. System ignore set that could not be compared.
     * 3. The ignore fields that could be set by developer.
     */
    private transient final Set<String> ignores = new HashSet<>();

    @Fluent
    public Vs ignores(final Set<String> ignoreSet) {
        if (Objects.nonNull(ignoreSet) && !ignoreSet.isEmpty()) {
            this.ignores.clear();
            this.ignores.addAll(ignoreSet);
        }
        return this;
    }

    public Set<String> ignores() {
        return this.ignores;
    }

    private Vs(final ConcurrentMap<String, Class<?>> mapType, final ConcurrentMap<String, Set<String>> mapSubtype) {
        if (Objects.nonNull(mapType) && !mapType.isEmpty()) {
            this.mapType.clear();
            this.mapType.putAll(mapType);
        }
        if (Objects.nonNull(mapSubtype) && !mapSubtype.isEmpty()) {
            this.mapSubtype.clear();
            this.mapSubtype.putAll(mapSubtype);
        }
    }

    public static Vs create(final String identifier, final ConcurrentMap<String, Class<?>> mapType, final ConcurrentMap<String, Set<String>> mapSubtype) {
        return Fn.pool(POOL_VS, identifier, () -> new Vs(mapType, mapSubtype));
    }

    // ============================ Comparing Change ===============================

    public boolean isChange(final JsonObject previous, final JsonObject current) {

        return true;
    }

    public boolean isChange(final JsonObject twins) {
        final JsonObject secure = Ut.sureJObject(twins);
        return this.isChange(secure.getJsonObject(Values.VS.OLD), secure.getJsonObject(Values.VS.NEW));
    }

    public boolean isChange(final Object valueOld, final Object valueNew, final String attribute) {
        final Class<?> type = this.mapType.get(attribute);
        final Set<String> subset = this.mapSubtype.getOrDefault(attribute, new HashSet<>());
        return Ut.isSame(valueOld, valueNew, type, subset);
    }
}
