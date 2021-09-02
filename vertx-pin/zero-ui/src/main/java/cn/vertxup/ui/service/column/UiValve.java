package cn.vertxup.ui.service.column;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.fn.Fn;

/*
 * Apeak choice for different usage,
 * valve means small door to do selection on mode
 */
public interface UiValve {

    static UiValve dynamic() {
        return Fn.pool(Pool.VALVE_MAP, StoreValve.class.getName(), StoreValve::new);
    }

    static UiValve fixed() {
        return Fn.pool(Pool.VALVE_MAP, FileValve.class.getName(), FileValve::new);
    }

    Future<JsonArray> fetchColumn(Vis view, String identifier, String sigma);
}
