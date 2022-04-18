package io.vertx.up.experiment.reference;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;

/**
 * ##「Pojo」DataAtom Key Replaced
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RDao {
    /**
     * Source identifier that has been mapped `source` field of `M_ATTRIBUTE`.
     */
    private final transient String source;

    private transient Function<JsonObject, JsonArray> actionS;

    private transient Function<JsonObject, Future<JsonArray>> actionA;

    public RDao(final String source) {
        this.source = source;
    }

    public RDao actionS(final Function<JsonObject, JsonArray> executor) {
        this.actionS = executor;
        return this;
    }

    public RDao actionA(final Function<JsonObject, Future<JsonArray>> executor) {
        this.actionA = executor;
        return this;
    }

    public JsonArray fetchBy(final JsonObject condition) {
        return Objects.requireNonNull(this.actionS).apply(condition);
    }

    public Future<JsonArray> fetchByAsync(final JsonObject condition) {
        return Objects.requireNonNull(this.actionA).apply(condition);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RDao rDao = (RDao) o;
        return this.source.equals(rDao.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source);
    }
}
