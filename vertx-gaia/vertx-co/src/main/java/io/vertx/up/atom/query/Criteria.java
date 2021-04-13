package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.atom.query.engine.QrDo;
import io.vertx.up.atom.query.engine.QrLinear;
import io.vertx.up.atom.query.engine.QrTree;
import io.vertx.up.exception.web._500QueryMetaNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * ## 「Pojo」Criteria Object
 *
 * ### 1. Intro
 *
 * Criteria for condition set, the connector is and Advanced criteria will use tree mode, the flat mode is "AND"
 *
 * #### 1.1. Features
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Criteria implements Serializable {
    /**
     * Zero standard logger in current
     */
    private static final Annal LOGGER = Annal.get(Criteria.class);
    /**
     * The mode of current criteria condition.
     *
     * Here are two modes:
     *
     * 1. LINEAR - The linear condition.
     * 2. TREE - The tree condition ( Complex query tree data structure ).
     */
    private final Qr.Mode mode;
    /**
     * The LINEAR analyzer.
     */
    private transient QrDo linear;
    /**
     * The TREE analyzer
     */
    private transient QrDo tree;

    /**
     * Create criteria based on json object.
     *
     * @param data {@link io.vertx.core.json.JsonObject}
     */
    private Criteria(final JsonObject data) {
        Fn.outWeb(null == data, LOGGER, _500QueryMetaNullException.class, this.getClass());
        assert data != null : "If null pointer, the exception will be thrown out.";
        this.mode = this.parseMode(data);
        if (Qr.Mode.LINEAR == this.mode) {
            this.linear = QrLinear.create(data);
        } else {
            this.tree = QrTree.create(data);
        }
    }

    /**
     * Create new instance of criteria.
     *
     * @param data {@link io.vertx.core.json.JsonObject}
     *
     * @return {@link Criteria}
     */
    public static Criteria create(final JsonObject data) {
        return new Criteria(data);
    }

    /**
     * Parsing the data to detect the query mode.
     *
     * @param data {@link io.vertx.core.json.JsonObject}
     *
     * @return {@link Qr.Mode}
     */
    private Qr.Mode parseMode(final JsonObject data) {
        Qr.Mode mode = Qr.Mode.LINEAR;
        for (final String field : data.fieldNames()) {
            if (Ut.isJObject(data.getValue(field))) {
                mode = Qr.Mode.TREE;
                break;
            }
        }
        return mode;
    }

    /**
     * Whether the condition is valid based on input json.
     *
     * @return {@link java.lang.Boolean}
     */
    public boolean valid() {
        if (Qr.Mode.LINEAR == this.mode) {
            return this.linear.valid();
        } else {
            return this.tree.valid();
        }
    }

    /**
     * Get the json query condition mode.
     *
     * @return {@link Qr.Mode}
     */
    public Qr.Mode getMode() {
        return this.mode;
    }

    /**
     * @param field {@link java.lang.String}
     * @param value {@link java.lang.Object}
     *
     * @return {@link Criteria}
     */
    public Criteria add(final String field, final Object value) {
        if (Qr.Mode.LINEAR == this.mode) {
            this.linear.add(field, value);
        } else {
            this.tree.add(field, value);
        }
        return this;
    }

    /**
     * Return to condition of json object.
     *
     * @return {@link JsonObject}
     */
    public JsonObject toJson() {
        if (Qr.Mode.LINEAR == this.mode) {
            return this.linear.toJson();
        } else {
            return this.tree.toJson();
        }
    }
}
