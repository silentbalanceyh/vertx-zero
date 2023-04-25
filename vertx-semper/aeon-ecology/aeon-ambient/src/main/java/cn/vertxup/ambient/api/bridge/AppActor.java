package cn.vertxup.ambient.api.bridge;

import io.aeon.annotations.QaS;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.unity.Ux;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@QaS
public class AppActor {

    @Address(Addr.App.BY_NAME)
    public Future<JsonObject> byName(final String name) {
        final Supplier<String> supplier = () -> name;
        final Future<String> future = Future.succeededFuture();
        final Function<String, Future<String>> supplier1 = (nil) -> future;
        final List<Future<T>> list = new ArrayList<>();

        final String input = "Hello";

        final Function<String, String> function = (nil) -> name;
        return Ux.futureJ();
    }
}
