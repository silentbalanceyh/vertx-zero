package io.vertx.up.atom.query;

import io.vertx.core.Future;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author lang
 * Batch operation by `pager` instead of fetch all data here.
 */
public class Pagination {
    private final Pager pager;
    private Integer total;

    public Pagination(final Pager pager) {
        this.pager = pager;
    }

    public Pager getPager() {
        return this.pager;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Pagination setTotal(final Integer total) {
        this.total = total;
        return this;
    }

    public Future<Set<Pagination>> scatterAsync() {
        return this.scatterAsync(false);
    }

    public Future<Set<Pagination>> scatterAsync(final boolean current) {
        if (Objects.isNull(this.total)) {
            return Future.succeededFuture(new HashSet<>());
        } else {
            /*
             * Get page size from current pager.
             */
            final Integer pageSize = this.pager.getSize();
            /*
             * Get pages calculated, all Pagination could not share page here
             */
            if (0 < pageSize) {
                final int pages = this.total / pageSize + 1;
                final Set<Pagination> paginationSet = new HashSet<>();
                /*
                 * Whether include current `pagination`
                 */
                if (current) {
                    paginationSet.add(this);
                }
                /*
                 * Build all other `pagination`
                 */
                for (int last = 2; last <= pages; last++) {
                    final Pager pager = Pager.create(last, pageSize);
                    paginationSet.add(new Pagination(pager).setTotal(this.total));
                }
                return Future.succeededFuture(paginationSet);
            } else {
                return Future.succeededFuture(new HashSet<>());
            }
        }
    }

    @Override
    public String toString() {
        return "Pagination{" +
            "pager=" + this.pager +
            ", total=" + this.total +
            '}';
    }
}
