package io.vertx.up.uca.jooq.util;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class JqFlow {
    private static final Cc<String, JqFlow> CC_FLOW = Cc.open();
    private transient JqAnalyzer analyzer;
    private transient String pojo;

    private JqFlow(final JqAnalyzer analyzer, final String pojo) {
        this.analyzer = analyzer;
        this.pojo = pojo;
    }

    public static JqFlow create(final JqAnalyzer analyzer) {
        /*
         * Pojo resolutation
         */
        return create(analyzer, analyzer.pojoFile());
    }

    public static JqFlow create(final JqAnalyzer analyzer, final String pojo) {
        /*
         * The ( pojo + key ) is the pool key for JqIn
         * 1) Each key contains only one `JqIn` to do input converting
         * 2) The `JqIn` could process input/output workflow instead of other component
         */
        final Class<?> entityCls = analyzer.type();
        final String normalized = analyzer.pojoFile(pojo);
        return CC_FLOW.pick(() -> new JqFlow(analyzer, normalized), entityCls.getName() + "," + normalized);
        // Fn.po?l(Pool.POOL_FLOW, entityCls.getName() + "," + normalized, () -> new JqFlow(analyzer, normalized));
    }

    // ============ JqFlow re-bind and calculation =========

    public JqFlow on(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.pojo = analyzer.pojoFile();
        return this;
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

    public Ir inputQr(final JsonObject input) {
        return JqTool.qr(input, this.pojo);
    }

    public JsonObject inputQrJ(final JsonObject criteria) {
        return JqTool.criteria(criteria, pojo);
    }

    public Future<Ir> inputQrAsync(final JsonObject input) {
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

    public <T> ConcurrentMap<String, JsonArray> output(final ConcurrentMap<String, List<T>> input) {
        final ConcurrentMap<String, JsonArray> map = new ConcurrentHashMap<>();
        input.forEach((field, list) -> {
            final JsonArray groupData = Ux.toJson(list, this.pojo);
            map.put(field, groupData);
        });
        return map;
    }

    public <T> Future<ConcurrentMap<String, JsonArray>> outputAsync(final ConcurrentMap<String, List<T>> input) {
        return Future.succeededFuture(this.output(input));
    }

    public <T> Future<JsonArray> outputAsync(final List<T> input) {
        return Future.succeededFuture(this.output(input));
    }
}
