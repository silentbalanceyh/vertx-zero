package io.vertx.tp.ke.atom.metadata;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404IndentParsingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

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

    public KPoint procTarget(final JsonObject data) {
        /*
         * Joined configuration read
         */
        final String identifier = this.procIdentifier(data);
        if (Ut.isNil(identifier)) {
            return null;
        }
        final KPoint target = this.target.getOrDefault(identifier, null);
        if (Objects.isNull(target)) {
            LOGGER.warn("System could not find configuration for `{0}`, data = {1}", identifier, data.encode());
            return null;
        }
        return target.indent(identifier);
    }

    private String procIdentifier(final JsonObject data) {
        /*
         * Search by `field`
         */
        final String identifier;
        if (Ut.isNil(this.targetIndent)) {
            LOGGER.warn("The `targetIndent` field is null");
            return null;
        }
        if (data.containsKey(this.targetIndent)) {
            /*
             * Data Processing
             */
            identifier = data.getString(this.targetIndent);
        } else {
            identifier = this.targetIndent;
        }
        Fn.out(Ut.isNil(identifier), _404IndentParsingException.class, this.getClass(), this.targetIndent, data);
        return identifier;
    }

    public <T> T procTarget(final JsonObject data, final Function<KPoint, T> fnProcess) {
        final KPoint point = this.procTarget(data);
        return fnProcess.apply(point);
    }

    /**
     * Source Join Target
     */
    public void procFilters(final JsonObject data, final KPoint target, final JsonObject filterRef) {
        final KPoint source = this.source;
        if (Objects.nonNull(target) && Objects.nonNull(source)) {
            /*
             * joinedValue from source -> key
             */
            final String joinedValue = data.getString(source.getKey());
            if (Ut.notNil(joinedValue)) {
                filterRef.put(target.getKeyJoin(), joinedValue);
            }
        }
    }
}
