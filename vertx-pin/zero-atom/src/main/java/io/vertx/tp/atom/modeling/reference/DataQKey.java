package io.vertx.tp.atom.modeling.reference;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.error._404ModelNotFoundException;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * ##「Pojo」DataAtom Key Replaced
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DataQKey {
    /**
     * 「Dynamic」When dynamic source triggered, this reference stored model definition in `M_MODEL / M_ATTRIBUTE`
     */
    private transient DataAtom atom;
    /**
     * 「Static」When static source triggered, this reference stored initialized Dao class.
     *
     * The Dao class came from `serviceReference`
     */
    private transient UxJooq jooq;
    /**
     * Source identifier that has been mapped `source` field of `M_ATTRIBUTE`.
     */
    private final transient String source;

    public DataQKey(final String appName, final String source) {
        this.source = source;
        try {
            this.atom = DataAtom.get(appName, source);
        } catch (final _404ModelNotFoundException ignored) {
        }
    }

    public DataAtom atom() {
        return this.atom;
    }

    public String source() {
        return this.source;
    }

    @Fluent
    public DataQKey connect(final DataQuote quote) {
        if (Objects.nonNull(quote) && Objects.isNull(this.atom)) {
            this.jooq = Ux.Jooq.on(quote.typeDao());
        }
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final DataQKey dataQKey = (DataQKey) o;
        return this.source.equals(dataQKey.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source);
    }
}
