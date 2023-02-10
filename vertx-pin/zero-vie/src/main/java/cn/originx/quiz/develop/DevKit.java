package cn.originx.quiz.develop;

import cn.originx.quiz.oclick.InstClick;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.HAtom;
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
    /*
     * All the methods of this part is standalone, it will ignore OOB folder
     *
     * isOob = false
     */
    // CMDB
    public static void oobCmdb() {
        doLoading(DevDefault.pathCmdb(), null, false);
    }

    // CAB
    public static void oobCab() {
        doLoading(DevDefault.pathCab(), null, false);
    }

    // DATA
    public static void oobData() {
        doLoading(DevDefault.pathData(), null, false);
    }

    // ENVIRONMENT
    public static void oobEnvironment() {
        doLoading(DevDefault.pathEnvironment(), null, false);
    }

    // MODULAT
    public static void oobModulat() {
        doLoading(DevDefault.pathModulat(), null, false);
    }

    // INTEGRATION
    public static void oobIntegration() {
        doLoading(DevDefault.pathIntegration(), null, false);
    }

    // WORKFLOW
    public static void oobRule() {
        doLoading(DevDefault.pathActivity(null), null, false);
    }

    public static void oobRule(final String workflow) {
        doLoading(DevDefault.pathActivity(workflow), null, false);
    }

    // ROLE
    public static void oobRole(final String role) {
        doLoading(DevDefault.pathRole(role), null, false);
    }

    // CAB/UI FOR MODEL
    public static void oobUi(final String identifier) {
        doLoading(DevDefault.pathUi(identifier), null, false);
    }

    public static void oobUi(final String identifier, final String prefix) {
        doLoading(DevDefault.pathUi(identifier), prefix, false);
    }

    // ----------------------- Data Loading for Initializing -------------------------
    /*
     * Following two APIs are related to standard data loading of oob, the default actions are:
     *
     * - oobLoader()
     *
     * When you start this loader, zero extension framework will load the data into database to do initializing
     * on the empty database here.
     */
    public static void oobLoader(final String prefix) {
        doLoading(DevDefault.pathOob(), prefix, true);
    }

    public static void oobLoader() {
        doLoading(DevDefault.pathOob(), null, true);
    }

    public static void oobLoader(final String path, final boolean isOob) {
        doLoading(path, null, isOob);
    }

    // ----------------------- DevModeller Object -------------------------

    public static DevModeller modeller() {
        return modeller(DevDefault.ROOT_INPUT, DevDefault.ROOT_OUTPUT);
    }

    public static DevModeller modeller(final String input, final String output) {
        Objects.requireNonNull(input, output);
        final String hashKey = Ut.encryptMD5(input + Strings.COLON + output);
        return DevDefault.CC_MODELLER.pick(() -> new DevModeller(input, output), hashKey);
    }
    // ----------------------- DevReport output -------------------------

    public static void outAtom(final HAtom atom) {
        DevReport.outAtom(atom);
    }

    // ----------------------- Inst Method -------------------------
    public static void instLoad(final Class<?> target, final String[] args) {
        final InstClick click = InstClick.instance(target);
        click.runLoad(args);
    }

    // ----------------------- Private Method -------------------------
    @SuppressWarnings("all")
    private static void doLoading(final String root, final String prefix, final boolean isOob) {
        final Annal logger = Annal.get(DevKit.class);
        Ke.infoKe(logger, "Data Loading from `{0}`", root);
        Bt.init(root, Objects.isNull(prefix) ? Strings.EMPTY : prefix, isOob);
    }
}
