package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.refine.Fm;
import io.vertx.tp.ke.atom.KSpec;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BookService implements BookStub {
    @Override
    public Future<List<FBook>> fetchAsync(final JsonObject criteria) {
        return Ux.Jooq.on(FBookDao.class).fetchAsync(criteria);
    }

    @Override
    public Future<List<FBook>> fetchByOrder(final String orderId) {
        Objects.requireNonNull(orderId);
        final JsonObject condition = new JsonObject();
        condition.put("orderId", orderId);
        return this.fetchAsync(condition);
    }

    @Override
    public Future<List<FBook>> createAsync(final List<FBook> books, final KSpec spec) {
        final List<FBook> subBooks = Fm.umBook(spec, books);
        return Ux.Jooq.on(FBookDao.class).insertAsync(subBooks);
    }
}
