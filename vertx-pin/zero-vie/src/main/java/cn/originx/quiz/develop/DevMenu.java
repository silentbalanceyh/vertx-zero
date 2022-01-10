package cn.originx.quiz.develop;

import cn.vertxup.ambient.domain.tables.daos.XMenuDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DevMenu {

    static final String MENU_INIT = "init/permission/ui.menu/role/";
    static final String MENU_OUT = "init/permission/ui.menu/";

    private static List<String> fetchDefault() {
        final List<String> types = new ArrayList<>();
        types.add("APP-MENU");
        types.add("TOP-MENU");
        types.add("SIDE-MENU");
        types.add("NAV-MENU");
        return types;
    }

    /*
     * Menu Helper for feature usage
     * Fetch menus and output the formatted menu include name / text
     */
    private static Future<JsonArray> fetchMenuTree(final List<String> types, final JsonArray required) {
        final JsonObject query = new JsonObject();
        query.put("sorter", new JsonArray()
            .add("type,ASC")
            .add("level,ASC")
            .add("order,ASC")
        );
        return Ux.Jooq.on(XMenuDao.class).searchAsync(query).compose(pageData -> {
            final JsonArray source = pageData.getJsonArray(KName.LIST);
            // Filter by required
            JsonArray filtered = new JsonArray();
            if (Ut.notNil(required)) {
                Ut.itJArray(source).filter(json -> required.contains(json.getValue("text")))
                    .forEach(filtered::add);
            } else {
                filtered = source;
            }
            final JsonArray calculate = filtered.copy();
            final JsonArray result = new JsonArray();
            // APP-MENU / TOP-MENU
            types.forEach(type -> Ut.itJArray(calculate)
                .filter(json -> type.equals(json.getString(KName.TYPE)))
                .forEach(result::add));
            // SIDE-MENU root
            final JsonArray root = new JsonArray();
            Ut.itJArray(calculate)
                .filter(json -> Values.ONE == json.getInteger("level"))
                .filter(json -> Objects.isNull(json.getString("parentId")))
                .forEach(root::add);
            return Ux.future(buildTree(root, result));
        });
    }

    /*
     * Menu fetching from database to Json String
     */
    static Future<JsonArray> menuFetch(final List<String> types, final boolean readable) {
        final List<String> menuTypes = (Objects.isNull(types) || types.isEmpty()) ? fetchDefault() : types;
        return fetchMenuTree(menuTypes, null).compose(menus -> {
            final JsonArray menuJArray = new JsonArray();
            Ut.itJArray(menus).forEach(json -> buildDisplay(json, menuJArray, readable ? Display.TEXT : Display.CONFIGURED));
            return Ux.future(menuJArray);
        });
    }

    static Future<ConcurrentMap<String, JsonArray>> menuInitialize(final Set<String> roles) {
        final ConcurrentMap<String, Future<JsonArray>> menuFuture = new ConcurrentHashMap<>();
        roles.forEach(role -> {
            final JsonArray required = buildRequired(role);
            menuFuture.put(role, fetchMenuTree(fetchDefault(), required));
        });
        return Ux.thenCombine(menuFuture).compose(menuData -> {
            final ConcurrentMap<String, JsonArray> menuMap = new ConcurrentHashMap<>();
            menuData.forEach((role, menuEach) -> {
                final JsonArray menuJArray = new JsonArray();
                Ut.itJArray(menuEach)
                    .forEach(json -> buildDisplay(json, menuJArray, Display.NAME));
                menuMap.put(role, menuJArray);
            });
            return Ux.future(menuMap);
        });
    }

    private static JsonArray buildRequired(final String role) {
        final JsonArray required = Ut.ioJArray(MENU_INIT + role + ".json");
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(required, String.class, (menu, index) -> normalized.add(menu.trim()));
        return normalized;
    }

    private static void buildDisplay(final JsonObject json, final JsonArray csv, final Display mode) {
        final Integer level = json.getInteger("level");
        final String text = json.getString("text");
        final String name = json.getString("name");
        // Display Capture
        if (Display.NAME == mode) {
            csv.add(name);
        } else {
            final String literal;
            if (mode == Display.TEXT) {
                literal = text;
            } else {
                literal = text + "," + name;
            }
            if (level == 1) {
                csv.add(literal);
            } else if (level == 2) {
                csv.add("   " + literal);
            } else if (level == 3) {
                csv.add("       " + literal);
            }
        }
        final JsonArray children = json.getJsonArray(KName.CHILDREN);
        if (Ut.notNil(children)) {
            Ut.itJArray(children).forEach(child -> buildDisplay(child, csv, mode));
        }
    }

    private static JsonArray buildTree(final JsonArray root, final JsonArray source) {
        final JsonArray items = new JsonArray();
        Ut.itJArray(root).forEach(node -> {
            // Fetch Children
            final JsonObject current = node.copy();
            final JsonArray children = findChildren(node, source);
            if (Ut.notNil(children)) {
                final JsonArray childrenTree = buildTree(children, source);
                current.put(KName.CHILDREN, childrenTree);
            }
            // Add current
            items.add(current);
        });
        return items;
    }

    private static JsonArray findChildren(final JsonObject root, final JsonArray source) {
        final JsonArray children = new JsonArray();
        Ut.itJArray(source)
            .filter(item -> root.getString(KName.KEY).equals(item.getString("parentId")))
            .forEach(children::add);
        return children;
    }

    private enum Display {
        TEXT,
        CONFIGURED,
        NAME,
    }
}
