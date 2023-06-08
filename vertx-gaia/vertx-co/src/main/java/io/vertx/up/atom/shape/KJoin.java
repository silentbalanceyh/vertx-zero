package io.vertx.up.atom.shape;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._412IndentParsingException;
import io.vertx.up.exception.web._412IndentUnknownException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * ## 「Pojo」Join Configuration
 */
public class KJoin implements Serializable {
    /**
     * Zero standard logger
     */
    private static final Annal LOGGER = Annal.get(KJoin.class);
    /**
     * `identifier` field of join configuration
     */
    private volatile String targetIndent;
    /**
     * `source` field of join configuration
     */
    private KPoint source;
    /**
     * `target` field of join configuration
     */
    private ConcurrentMap<String, KPoint> target = new ConcurrentHashMap<>();

    public String getTargetIndent() {
        return this.targetIndent;
    }

    public void setTargetIndent(final String targetIndent) {
        this.targetIndent = targetIndent;
    }

    public KPoint getSource() {
        return this.source;
    }

    public void setSource(final KPoint source) {
        this.source = source;
    }

    public ConcurrentMap<String, KPoint> getTarget() {
        return this.target;
    }

    public void setTarget(final ConcurrentMap<String, KPoint> target) {
        this.target = target;
    }

    private String identifier(final JsonObject data) {
        /*
         * Joined configuration read
         */
        /*
         * Search by `field`
         */
        if (Ut.isNil(this.targetIndent)) {
            LOGGER.warn("The `targetIndent` field is null");
            return null;
        }
        final String identifier;
        if (data.containsKey(this.targetIndent)) {
            /*
             * Data Processing
             */
            identifier = data.getString(this.targetIndent);
        } else {
            identifier = this.targetIndent;
        }
        return identifier;
    }

    public KPoint point(final JsonObject data) {
        final String identifier = this.identifier(data);
        Fn.out(Ut.isNil(identifier), _412IndentParsingException.class, this.getClass(), this.targetIndent, data);
        final KPoint result = this.point(identifier);
        if (Objects.isNull(result)) {
            LOGGER.info("System could not find configuration for `{0}` with data = {1}",
                identifier, data.encode());
        }
        return result;
    }

    public KPoint point(final String identifier) {
        final KPoint target = this.target.getOrDefault(identifier, null);
        if (Objects.isNull(target)) {
            return null;
        }
        return target.indent(identifier);
    }

    public KPoint point(final JsonArray data) {
        final Set<String> idSet = new HashSet<>();
        Ut.itJArray(data).map(this::identifier).filter(Objects::nonNull).forEach(idSet::add);
        Fn.out(1 != idSet.size(), _412IndentUnknownException.class, this.getClass(), this.targetIndent);
        final String identifier = idSet.iterator().next();
        return this.point(identifier);
    }

    /**
     * key -> keyJoin
     */
    public void dataIn(final JsonObject ds, final KPoint target, final JsonObject data) {
        this.dataRun(target, (source) -> {
            final String joinedValue = ds.getString(source.getKey());
            if (Ut.isNotNil(joinedValue)) {
                data.put(target.getKeyJoin(), joinedValue);
            }
        });
    }

    public void dataCond(final JsonObject ds, final KPoint target, final JsonObject data) {
        this.dataRun(target, (source) -> {
            String joinedValue = ds.getString(source.getKey());
            if (Ut.isNil(joinedValue)) {
                /*
                 * Here has no `key` defined in ds, synced by `keyJoin` directly
                 */
                joinedValue = ds.getString(target.getKeyJoin());
            }
            if (Ut.isNotNil(joinedValue)) {
                data.put(target.getKeyJoin(), joinedValue);
            }
        });
    }

    /**
     * keyJoin -> key
     */
    public void dataOut(final JsonObject ds, final KPoint target, final JsonObject data) {
        this.dataRun(target, (source) -> {
            final String joinedValue = ds.getString(target.getKeyJoin());
            if (Ut.isNotNil(joinedValue)) {
                data.put(source.getKey(), joinedValue);
            }
        });
    }

    private void dataRun(final KPoint target, final Consumer<KPoint> consumer) {
        final KPoint source = this.source;
        if (Objects.nonNull(target) && Objects.nonNull(source)) {
            consumer.accept(source);
        }
    }
}
