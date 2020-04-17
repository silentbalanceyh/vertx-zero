package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class BranchTc {
    @Test
    public void testBranch() {
        final Future<String> result = Fn.match(
                () -> Fn.fork(
                        () -> Ux.log(this.getClass()).on("[ Log ] Fork -> {0}").info("Lang"),
                        () -> Future.succeededFuture("Fork")),
                Fn.branch(true,
                        () -> Ux.log(this.getClass()).on("[ Log ] Branch -> {0}").info("Lang"),
                        () -> Future.succeededFuture("Branch"))
        );
        result.onComplete(System.out::println);
    }
}

class TestFilter {
    private String name;
    private String email;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}