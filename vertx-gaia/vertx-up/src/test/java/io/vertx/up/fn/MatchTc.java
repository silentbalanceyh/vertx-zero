package io.vertx.up.fn;

import io.vertx.core.Future;
import org.junit.Test;

public class MatchTc {

    @Test
    public void testMatch() {
        final String condition = null;
        Fn.match(() -> Fn.fork(() -> Future.succeededFuture()),
                Fn.branch(null == condition,
                        () -> {
                            return Future.succeededFuture("Hello");
                        }))
                .compose(result -> {
                    System.out.println(result);
                    return Future.succeededFuture();
                });
    }
}
