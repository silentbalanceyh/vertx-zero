package io.vertx.tp.plugin.mongo;

import io.vertx.ext.mongo.FindOptions;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.Sorter;
import io.vertx.up.fn.Fn;

/**
 * Build FindOptions for mongo database.
 */
public class MongoReadOpts {
    /**
     * JqTool top n records by field DESC
     *
     * @param sortField
     * @param limit
     *
     * @return
     */
    public static FindOptions toDescLimit(
        final String sortField,
        final int limit
    ) {
        return toFull(Pager.create(1, limit), Sorter.create(sortField, false));
    }

    /**
     * JqTool top n records by field ASC
     *
     * @param sortField
     * @param limit
     *
     * @return
     */
    public static FindOptions toAscLimit(
        final String sortField,
        final int limit
    ) {
        return toFull(Pager.create(1, limit), Sorter.create(sortField, true));
    }

    /**
     * JqTool all records by field Desc
     *
     * @param sortField
     *
     * @return
     */
    public static FindOptions toDesc(
        final String sortField
    ) {
        return toFull(null, Sorter.create(sortField, false));
    }

    /**
     * JqTool all records by field Asc
     *
     * @param sortField
     *
     * @return
     */
    public static FindOptions toAsc(
        final String sortField
    ) {
        return toFull(null, Sorter.create(sortField, true));
    }

    /**
     * JqTool all records by pager/sorter both
     *
     * @param pager
     * @param sorter
     *
     * @return
     */
    public static FindOptions toFull(
        final Pager pager,
        final Sorter sorter
    ) {
        final FindOptions options = new FindOptions();
        Fn.safeNull(() -> {
            options.setLimit(pager.getSize());
            options.setSkip(pager.getStart());
        }, pager);
        Fn.safeNull(() -> options.setSort(sorter.toJson((mode) -> mode ? 1 : -1)), sorter);
        return options;
    }
}
