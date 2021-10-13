package io.vertx.quiz.nova;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.ext.unit.TestContext;
import io.vertx.up.exception.zero.TestCaseNameException;
import io.vertx.up.fn.Fn;

import java.util.function.BiConsumer;

/*
 * Qz package for help test tool to test Zero Extension module
 */
public class Qz {
    /*
     * The class who called Qz tool
     */
    private final transient Class<?> target;
    private QzData data;

    private Qz(final Class<?> target) {
        final String name = target.getSimpleName();
        Fn.out(!name.endsWith("Tc"), TestCaseNameException.class, target, name);
        this.target = target;
    }

    public static Qz get(final Class<?> target) {
        return Fn.pool(Pool.QZ_POOL, target, () -> new Qz(target));
    }

    /*
     * 1. Step one
     *    Collect input from ioFile instead of other input source
     */
    @Fluent
    public Qz input(final String filename) {
        this.data = QzData.create(this.target, filename).init();
        return this;
    }

    /*
     * 2. Step execute
     *    Bind Method and execute to next
     */
    @SuppressWarnings("all")
    public <T> void test(final TestContext context, final BiConsumer<T, T> consumer) {
        // Fn.onTest(context, this.data.<Envelop>async(), result -> consumer.accept((T) this.data.input(), (T) result));
    }
}
