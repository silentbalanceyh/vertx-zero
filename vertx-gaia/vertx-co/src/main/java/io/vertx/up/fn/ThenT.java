package io.vertx.up.fn;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
@SuppressWarnings("all")
final class ThenT {

    private ThenT() {
    }

    static <T> Future<List<T>> combineT(final CompositeFuture res) {
        if (res.succeeded()) {
            final List<T> result = res.list();
            return Future.succeededFuture(result);
        } else {
            return Other.otherwiseWeb(ThenT.class, res.cause());
        }
    }

    static <T> Future<List<T>> combineT(final List<Future<T>> futures) {
        final List<Future> futureList = new ArrayList<>(futures);
        return CompositeFuture.join(futureList).compose(finished -> {
            final List<T> result = new ArrayList<>();
            Ut.itList(finished.list(),
                (item, index) -> result.add((T) item));
            return Future.succeededFuture(result);
        }).otherwise(Other.otherwiseOr(ArrayList::new));
    }

    static <T> Future<Set<T>> combineT(final Set<Future<T>> futures) {
        final List<Future> futureList = new ArrayList<>(futures);
        return CompositeFuture.join(futureList).compose(finished -> {
            final Set<T> result = new HashSet<>();
            Ut.itList(finished.list(),
                (item, index) -> result.add((T) item));
            return Future.succeededFuture(result);
        }).otherwise(Other.otherwiseOr(HashSet::new));
    }


    static <F, S, T> Future<T> combineT(final Supplier<Future<F>> futureF, final Supplier<Future<S>> futureS,
                                        final BiFunction<F, S, Future<T>> consumer) {
        return futureF.get().compose(f -> futureS.get().compose(s -> consumer.apply(f, s)));
    }

    static <F, S, T> Future<T> combineT(final Supplier<Future<F>> futureF, final Function<F, Future<S>> futureS,
                                        final BiFunction<F, S, Future<T>> consumer) {
        return futureF.get().compose(f -> futureS.apply(f).compose(s -> consumer.apply(f, s)));
    }
}
