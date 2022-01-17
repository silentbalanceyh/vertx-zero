package cn.originx.quiz.develop;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
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
        doLoading(DevDefault.pathCmdb(), null, false);
    }

    public static void oobCab() {
        doLoading(DevDefault.pathCab(), null, false);
    }


    public static void oobData() {
        doLoading(DevDefault.pathData(), null, false);
    }

    public static void oobEnvironment() {
        doLoading(DevDefault.pathEnvironment(), null, false);
    }

    public static void oobRole(final String role) {
        doLoading(DevDefault.pathRole(role), null, false);
    }

    public static void oobUi(final String identifier) {
        doLoading(DevDefault.pathUi(identifier), null, false);
    }

    public static void oobLoader(final String prefix) {
        doLoading(DevDefault.pathOob(), prefix, true);
    }

    public static void oobLoader() {
        doLoading(DevDefault.pathOob(), null, true);
    }

    // ----------------------- DevModeller Object -------------------------

    public static DevModeller modeller() {
        return modeller(DevDefault.ROOT_INPUT, DevDefault.ROOT_OUTPUT);
    }

    public static DevModeller modeller(final String input, final String output) {
        Objects.requireNonNull(input, output);
        final String hashKey = Ut.encryptMD5(input + Strings.COLON + output);
        return Fn.pool(DevDefault.MODELLER, hashKey, () -> new DevModeller(input, output));
    }

    // ----------------------- Private Method -------------------------
    @SuppressWarnings("all")
    private static void doLoading(final String root, final String prefix, final boolean isOob) {
        final Annal logger = Annal.get(DevKit.class);
        Ke.infoKe(logger, "Data Loading from `{0}`", root);
        Bt.init(root, Objects.isNull(prefix) ? Strings.EMPTY : prefix, isOob);
    }
}
