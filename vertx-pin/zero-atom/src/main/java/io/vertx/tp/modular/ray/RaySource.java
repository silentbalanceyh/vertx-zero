package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.reference.DataQuote;
import io.vertx.up.commune.Record;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class RaySource {
    private transient final DataQuote quote;
    private transient final DataAtom atom;

    private RaySource(final DataQuote quote, final DataAtom atom) {
        this.quote = quote;
        this.atom = atom;
    }

    static RaySource create(final DataQuote quote, final DataAtom atom) {
        return new RaySource(quote, atom);
    }

    /*
     * RaySource
     * field1 -> DataQRule
     * field2 -> DataQRule
     */
    public ConcurrentMap<String, RayRecord> fetchData(final Record record) {
        final ConcurrentMap<String, RayRecord> data = new ConcurrentHashMap<>();
        this.quote.rules().forEach((field, rule) -> {
            /*
             * 条件处理
             */
            final Class<?> clazz = this.quote.type(field);
            final JsonObject condition = rule.condition(record);

        });
        return null;
    }

    public ConcurrentMap<String, ConcurrentMap<String, RayRecord>> fetchData(final Record[] records) {

        return null;
    }

    private RayRecord fetchData(final JsonObject condition, final Class<?> returnType) {

        return null;
    }
}
