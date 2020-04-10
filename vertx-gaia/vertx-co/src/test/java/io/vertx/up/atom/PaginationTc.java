package io.vertx.up.atom;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.AsyncBase;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.Pagination;
import org.junit.Test;

public class PaginationTc extends AsyncBase {

    @Test
    public void testPagination(final TestContext context) {
        final Pager pager = Pager.create(1, 300);
        final Pagination pagination = new Pagination(pager).setTotal(1358);
        this.async(context, pagination.scatterAsync(true), actual -> {
            actual.forEach(System.err::println);

        });
    }
}
