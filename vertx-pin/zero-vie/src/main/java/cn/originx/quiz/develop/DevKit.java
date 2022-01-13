package cn.originx.quiz.develop;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DevKit {
    static {
        JooqInfix.init(Ux.nativeVertx());
    }

    // ----------------------- DevMenu -------------------------
    public static Future<JsonArray> menuFetch() {
        return menuFetch(true);
    }

    public static Future<JsonArray> menuFetch(final boolean readable) {
        return DevMenu.menuFetch(null, readable);
    }

    public static Future<ConcurrentMap<String, JsonArray>> menuBoot() {
        return DevMenu.menuInitialize(DevDefault.roles());
    }

    public static Future<Boolean> menuOutput(final ConcurrentMap<String, JsonArray> menuMap, final String root) {
        return DevMenu.menuOutput(menuMap, root);
    }

    // ----------------------- Dev Data Loading -------------------------

    public static void oobCmdb() {
        doLoading(DevDefault.pathCmdb(), null);
    }

    public static void oobUi() {
        doLoading(DevDefault.pathUi(), null);
    }

    public static void oobFull() {
        doLoading(DevDefault.pathOob(), null);
    }

    // ----------------------- DevModeller Object -------------------------

    public static DevModeller modeller() {
        return modeller(DevDefault.pathIn(), DevDefault.pathOut());
    }

    public static DevModeller modeller(final String input, final String output) {
        Objects.requireNonNull(input, output);
        final String hashKey = Ut.encryptMD5(input + Strings.COLON + output);
        return Fn.pool(DevDefault.MODELLER, hashKey, () -> new DevModeller(input, output));
    }

    // ----------------------- Private Method -------------------------
    @SuppressWarnings("all")
    private static void doLoading(final String root, final String prefix) {
        Bt.init(root, Objects.isNull(prefix) ? Strings.EMPTY : prefix);
    }
}
