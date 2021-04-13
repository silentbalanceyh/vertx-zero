package io.vertx.tp.modular.jooq;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JQPre {
    /**
     * Process for `REFERENCE` condition based on DataAtom
     *
     * @param atom     {@link DataAtom} The model definition.
     * @param criteria {@link Criteria} the criteria object to stored query condition.
     *
     * @return The new modified criteria
     */
    static Criteria prepare(final DataAtom atom, final Criteria criteria) {
        /*
         * Reference collection
         * 1. Attribute must be Elementary
         * 2. Execute query processing
         */
        final JsonObject input = Ut.sureJObject(criteria.toJson());

        return criteria;
    }
}
