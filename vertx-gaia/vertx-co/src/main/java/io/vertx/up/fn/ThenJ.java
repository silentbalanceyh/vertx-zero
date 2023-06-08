package io.vertx.up.fn;

import io.horizon.fn.HFn;
import io.horizon.uca.log.Log;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 要么 JsonObject / 要么 JsonArray
 *
 * @author lang : 2023/4/27
 */
@SuppressWarnings("all")
final class ThenJ {
    private ThenJ() {
    }

    static <J, A> Future<JsonObject> choiceJ(
        final JsonObject input, final String field,
        final Function<JsonObject, Future<J>> itemFnJ, final Function<JsonArray, Future<A>> itemFnA) {
        final Object value = input.getValue(field);
        if (value instanceof final JsonArray valueA) {
            // 提取参数为 JsonArray
            return itemFnA.apply(valueA).compose(processed -> {
                input.put(field, processed);
                return Future.succeededFuture(input);
            });
        } else if (value instanceof final JsonObject valueJ) {
            // 提取参数为 JsonObject
            return itemFnJ.apply(valueJ).compose(processed -> {
                input.put(field, processed);
                return Future.succeededFuture(input);
            });
        } else {
            // 什么都不做
            Log.warn(ThenJ.class, __Message.ThenJ.TYPE_JA_NOT_MATCH, value);
            return Future.succeededFuture(input);
        }
    }

    static Future<JsonObject> combineJ(final Future<JsonObject>... futures) {
        return CompositeFuture.join(Arrays.asList(futures)).compose(finished -> {
            final JsonObject resultMap = new JsonObject();
            if (null != finished) {
                Ut.itList(finished.list(), (item, index) -> resultMap.put(index.toString(), item));
            }
            return Future.succeededFuture(resultMap);
        }).otherwise(HFn.outAsync(JsonObject::new));
    }

    static Future<JsonObject> combineJ(
        final Future<JsonObject> source, final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun
    ) {
        return source.compose(first -> CompositeFuture.join(generateFun.apply(first)).compose(finished -> {
            if (null != finished) {
                final List<JsonObject> secondary = finished.list();
                // Zipper Operation, the base list is first
                Ut.itList(secondary, (item, index) -> operatorFun[index].accept(first, item));
            }
            return Future.succeededFuture(first);
        })).otherwise(HFn.outAsync(JsonObject::new));
    }
}
