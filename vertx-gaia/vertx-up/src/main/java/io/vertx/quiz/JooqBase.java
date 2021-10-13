package io.vertx.quiz;

import io.vertx.up.atom.query.Criteria;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class JooqBase extends ZeroBase {
    /*
     * Query condition
     */
    protected Criteria ioCriteria(final String filename) {
        return Criteria.create(this.ioJObject(filename));
    }

}
