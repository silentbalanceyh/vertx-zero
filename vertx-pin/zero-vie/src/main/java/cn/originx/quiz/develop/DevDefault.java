package cn.originx.quiz.develop;

import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DevDefault {
    static final ConcurrentMap<String, DevModeller> MODELLER = new ConcurrentHashMap<>();
    private static final String ROOT_ROLE = "init/permission/ui.menu/role/";
    private static final String ROOT_MENU = "init/permission/ui.menu/";
    private static final String ROOT_OOB = "init/oob";
    private static final String JSON_EXTENSION = Strings.DOT + FileSuffix.JSON;

    public static JsonArray pathRole(final String role) {
        return Ut.ioJArray(ROOT_ROLE + role + JSON_EXTENSION);
    }

    public static String pathMenu(final String root, final String role) {
        return root + ROOT_MENU + role + JSON_EXTENSION;
    }

    public static String pathOob() {
        return ROOT_OOB;
    }

    public static String pathUi() {
        return ROOT_OOB + "/oob";
    }

    public static String pathCmdb() {
        return ROOT_OOB + "/cmdb";
    }


    public static Set<String> roles() {
        final List<String> files = Ut.ioFiles(ROOT_ROLE);
        final Set<String> roleSet = new HashSet<>();
        files.forEach(file -> {
            final String role = file.replace(".json", Strings.EMPTY);
            roleSet.add(role);
        });
        return roleSet;
    }
}
