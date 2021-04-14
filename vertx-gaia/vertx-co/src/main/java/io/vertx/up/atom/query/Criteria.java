package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.atom.query.engine.QrDo;
import io.vertx.up.exception.web._500QueryMetaNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.io.Serializable;

/**
 * ## 「Pojo」Criteria Object
 *
 * ### 1. Intro
 *
 * Criteria for condition set, the connector is and Advanced criteria will use tree mode, the flat mode is "AND"
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
     * The LINEAR/TREE analyzer.
     */
    private final transient QrDo analyzer;

    /**
     * Create criteria based on json object.
     *
     * @param data {@link io.vertx.core.json.JsonObject}
     */
    private Criteria(final JsonObject data) {
        Fn.outWeb(null == data, LOGGER, _500QueryMetaNullException.class, this.getClass());
        assert data != null : "If null pointer, the exception will be thrown out.";
        if (QrDo.isComplex(data)) {
            this.mode = Qr.Mode.TREE;
        } else {
            this.mode = Qr.Mode.LINEAR;
        }
        this.analyzer = QrDo.create(data);
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
     * Get the json query condition mode.
     *
     * @return {@link Qr.Mode}
     */
    public Qr.Mode mode() {
        return this.mode;
    }

    /**
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link Criteria}
     */
    public Criteria save(final String fieldExpr, final Object value) {
        this.analyzer.save(fieldExpr, value);
        return this;
    }

    /**
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link Criteria}
     */
    public Criteria update(final String fieldExpr, final Object value) {
        this.analyzer.update(fieldExpr, value);
        return this;
    }

    /**
     * @param fieldExpr {@link java.lang.String} Removed fieldExpr
     *
     * @return {@link Criteria}
     */
    public Criteria remove(final String fieldExpr) {
        this.analyzer.remove(fieldExpr, false);
        return this;
    }

    /**
     * @param fieldExpr {@link java.lang.String} Removed fieldExpr
     *
     * @return {@link Criteria}
     */
    public Criteria removeBy(final String fieldExpr) {
        this.analyzer.remove(fieldExpr, true);
        return this;
    }

    /**
     * Return to condition of json object.
     *
     * @return {@link JsonObject}
     */
    public JsonObject toJson() {
        return this.analyzer.toJson();
    }
}
