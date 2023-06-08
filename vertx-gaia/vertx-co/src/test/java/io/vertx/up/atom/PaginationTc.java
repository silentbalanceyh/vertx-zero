package io.vertx.up.atom;

import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Pagination;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class PaginationTc extends ZeroBase {

    @Test
    public void testPagination(final TestContext context) {
        final Pager pager = Pager.create(1, 300);
        final Pagination pagination = new Pagination(pager).setTotal(1358);
        this.tcAsync(context, pagination.scatterAsync(true), actual -> {
            actual.forEach(System.err::println);

        });
    }
}
