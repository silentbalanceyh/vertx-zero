package io.vertx.quiz;

import io.vertx.core.Future;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.up.atom.Kv;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * io.vertx.quiz.EpicBase ( Pure ) :
 * Support IO method for JsonObject / JsonArray
 *
 * |- io.vertx.quiz.ZeroBase ( Vert.x ) :
 *    Provide vert.x instance with options
 *
 *    |- io.vertx.quiz.AsyncBase ( Async ) :
 *       Support `Database` & `Integration`, also include `async` method.
 *
 *       |- io.vertx.quiz.JooqBase ( Jooq ) :
 *          Support `Jooq` database testing unit
 *
 */
@RunWith(VertxUnitRunner.class)
public abstract class JooqBase extends AsyncBase {

    @Rule
    public final RunTestOnContext rule = new RunTestOnContext(OPTIONS);

    public UxJooq getDao() {
        return null;
    }

    public <T> void asyncJooq(final TestContext context,
                              final Supplier<Future<T>> supplier,
                              final Consumer<T> function) {
        this.asyncJooq(context, supplier, function, this::getDao);
    }

    private <T> void asyncJooq(final TestContext context,
                               final Supplier<Future<T>> supplier,
                               final Consumer<T> consumer,
                               final Supplier<UxJooq> daoSupplier) {
        final UxJooq jooq = daoSupplier.get();
        if (null != jooq) {
            final Future<T> future = supplier.get();
            Fn.onTest(context, future, consumer);
        }
    }

    public <T> void notNull(final T entity, final TestContext context) {
        context.assertNotNull(entity);
        Annal.get(this.getClass()).debug("[ Sim ] {0}", entity.getClass());
    }

    protected void fetchOneAsync(
            final TestContext context,
            final Class<?> clazzDao,
            final String pojo,
            final Object... args) {
        final List<Kv<String, Object>> kvs = new ArrayList<>();
        final int length = args.length / 2;
        for (int idx = 0; idx < length; idx++) {
            final int index = idx * 2;
            final String key = args[index].toString();
            final Object value = args[index + 1];
            kvs.add(Kv.create(key, value));
        }
        kvs.forEach(kv -> {
            UxJooq jooq = Ux.Jooq.on(clazzDao);
            if (null != pojo) {
                jooq = jooq.on(pojo);
            }
            this.async(context,
                    jooq.fetchOneAsync(kv.getKey(), kv.getValue()),
                    context::assertNotNull);
        });
    }
/*
    protected void fetchOneAndAsync(
            final TestContext context,
            final Class<?> clazz,
            final String pojo,
            final String... files) {
        final List<JsonObject> filters = new ArrayList<>();
        Arrays.stream(files).forEach(file -> filters.add(this.ioJObject(file)));
        filters.forEach(filter -> {
            UxJooq jooq = Ux.Jooq.on(clazz);
            if (null != pojo) {
                jooq = jooq.on(pojo);
            }
            this.async(context,
                    jooq.fetchOneAsync(filter),
                    context::assertNotNull);
        });
    }*/
}
