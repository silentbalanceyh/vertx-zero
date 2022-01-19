package io.vertx.tp.ke.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Async Utility X for Function Only
 *
 * This tool is critical for function management and scheduled in different code flow here.
 * It's a little duplicated to `Ux.then` methods, but no impact here for future usage, in future versions
 * I'll remove all the function part from `Ux`, instead, the Ax is more usage.
 *
 * Here the name are as following:
 *
 * A: JsonArray
 * J: JsonObject
 * T: Generic T
 * S: Set
 * L: List
 *
 * All the returned value must be Function and the returned value should be Future type.
 *
 * i -> Future(o)
 *
 * i - input
 * o - output
 *
 * The name specification is as following:
 *
 * o - One
 * m - Many ( A, S, L )
 *
 * Recommend you to use non-Ax API ( Instead you should use Ux/Ut/Fn )
 *
 * Ax tool is used in .compose only to remove `->`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Ax {
    /*
     * Set<T> -> T -> Future<JsonArray>
     */
    public static <T> Function<Set<T>, Future<JsonArray>> setTA(final Function<T, Future<JsonArray>> consumer) {
        return set -> {
            final List<Future<JsonArray>> futures = new ArrayList<>();
            set.forEach(item -> futures.add(consumer.apply(item)));
            return Ux.thenCombineArray(futures);
        };
    }
}
