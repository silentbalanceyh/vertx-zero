package cn.originx.quiz.develop;

import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DevDefault {
    static final ConcurrentMap<String, DevModeller> MODELLER = new ConcurrentHashMap<>();
    private static final String ROOT_MENU_ROLE = "init/permission/ui.menu/role/";
    private static final String ROOT_MENU = "init/permission/ui.menu/";
    private static final String ROOT_OOB = "init/oob";

    private static final String ROOT_INPUT = "atom/cmdb";
    private static final String ROOT_OUTPUT = "src/main/resources/atom/target";
    private static final String ROOT_ROLE = "init/oob/role";
    private static final String ROOT_CAB = "init/oob/cab";

    private static final String JSON_EXTENSION = Strings.DOT + FileSuffix.JSON;

    public static JsonArray pathMenu(final String role) {
        return Ut.ioJArray(ROOT_MENU_ROLE + role + JSON_EXTENSION);
    }

    public static String pathMenu(final String root, final String role) {
        return root + ROOT_MENU + role + JSON_EXTENSION;
    }

    public static String pathOob() {
        return ROOT_OOB;
    }

    public static String pathUi(final String identifier) {
        Objects.requireNonNull(identifier);
        return ROOT_OOB + "/cmdb/" + identifier + "/";
    }

    public static String pathRole(final String role) {
        Objects.requireNonNull(role);
        return ROOT_ROLE + "/" + role + "/";
    }

    public static String pathCmdb() {
        return ROOT_OOB + "/cmdb";
    }

    public static String pathCab() {
        return ROOT_CAB;
    }

    public static String pathIn() {
        return ROOT_INPUT;
    }

    public static String pathOut() {
        return ROOT_OUTPUT;
    }

    public static Set<String> roles() {
        final List<String> files = Ut.ioFiles(ROOT_MENU_ROLE);
        final Set<String> roleSet = new HashSet<>();
        files.forEach(file -> {
            final String role = file.replace(".json", Strings.EMPTY);
            roleSet.add(role);
        });
        return roleSet;
    }
}
