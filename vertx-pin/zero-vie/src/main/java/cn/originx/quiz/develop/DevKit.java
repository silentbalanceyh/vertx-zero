package cn.originx.quiz.develop;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DevKit {
    /*
     * Native Vertx
     */
    static {
        JooqInfix.init(Ux.nativeVertx());
    }

    /*
     * Menu fetching from database to Json String
     */

    public static Future<JsonArray> menuFetch() {
        return menuFetch(true);
    }

    public static Future<JsonArray> menuFetch(final boolean readable) {
        return DevMenu.menuFetch(null, readable);
    }

    public static Future<ConcurrentMap<String, JsonArray>> menuInitialize() {
        final List<String> files = Ut.ioFiles(DevMenu.MENU_INIT);
        final Set<String> roleSet = new HashSet<>();
        files.forEach(file -> {
            final String role = file.replace(".json", Strings.EMPTY);
            roleSet.add(role);
        });
        return DevMenu.menuInitialize(roleSet);
    }

    public static Future<Boolean> menuOutput(final ConcurrentMap<String, JsonArray> menuMap,
                                             final String root) {
        menuMap.forEach((role, data) -> {
            final String outFile = root + DevMenu.MENU_OUT + role + ".json";
            Ox.Log.infoShell(DevKit.class, "[ Dev ] File output: {0}", outFile);
            final JsonObject dataRole = new JsonObject();
            dataRole.put(KName.NAME, data);
            Ut.ioOut(outFile, dataRole);
        });
        return Ux.future(Boolean.TRUE);
    }
}
