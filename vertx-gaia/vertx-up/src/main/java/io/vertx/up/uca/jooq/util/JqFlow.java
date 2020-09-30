package io.vertx.up.uca.jooq.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public class JqFlow {
    private transient final JqAnalyzer analyzer;
    private transient final String pojo;

    private JqFlow(final JqAnalyzer analyzer, final String pojo) {
        this.analyzer = analyzer;
        this.pojo = pojo;
    }

    public static JqFlow create(final JqAnalyzer analyzer) {
        return create(analyzer, null);
    }

    public static JqFlow create(final JqAnalyzer analyzer, final String pojo) {
        /*
         * The ( pojo + key ) is the pool key for JqIn
         * 1) Each key contains only one `JqIn` to do input converting
         * 2) The `JqIn` could process input/output workflow instead of other component
         */
        final Class<?> entityCls = analyzer.type();
        final String normalized = analyzer.pojoFile(pojo);
        return Fn.pool(Pool.POOL_FLOW, entityCls.getName() + "," + normalized, () -> new JqFlow(analyzer, normalized));
    }

    // ============ Input Conversation =============

    public <T> T input(final JsonObject data) {
        final Class<?> entityCls = this.analyzer.type();
        return (T) Ux.fromJson(data, entityCls, this.pojo);
    }

    public <T> Future<T> inputAsync(final JsonObject data) {
        return Future.succeededFuture(this.input(data));
    }

    public <T> List<T> input(final JsonArray data) {
        final Class<?> entityCls = this.analyzer.type();
        return (List<T>) Ux.fromJson(data, entityCls, this.pojo);
    }

    public <T> Future<List<T>> inputAsync(final JsonArray data) {
        return Future.succeededFuture(this.input(data));
    }

    public Inquiry inputQr(final JsonObject input) {
        return JqTool.getInquiry(input, this.pojo);
    }

    public JsonObject inputQrJ(final JsonObject criteria) {
        final Inquiry inquiry = JqTool.getInquiry(new JsonObject().put(Inquiry.KEY_CRITERIA, criteria), this.pojo);
        return Objects.isNull(inquiry.getCriteria()) ? new JsonObject() : inquiry.getCriteria().toJson();
    }

    public Future<Inquiry> inputQrAsync(final JsonObject input) {
        return Future.succeededFuture(this.inputQr(input));
    }

    public Future<JsonObject> inputQrJAsync(final JsonObject criteria) {
        return Future.succeededFuture(this.inputQrJ(criteria));
    }

    // ============ Output Conversation =============
    public <T> JsonObject output(final T input) {
        return Ux.toJson(input, this.pojo);
    }

    public <T> Future<JsonObject> outputAsync(final T input) {
        return Future.succeededFuture(this.output(input));
    }

    public <T> JsonArray output(final List<T> input) {
        return Ux.toJson(input, this.pojo);
    }

    public <T> Future<JsonArray> outputAsync(final List<T> input) {
        return Future.succeededFuture(this.output(input));
    }
}
