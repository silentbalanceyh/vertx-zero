package cn.originx.quiz.develop;

import cn.vertxup.ambient.domain.tables.daos.XMenuDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DevHelper {
    /*
     * Menu Helper for feature usage
     */
    public static Future<JsonArray> fetchMenuTree() {
        final JsonObject query = new JsonObject();
        query.put("sorter", new JsonArray().add("type,ASC").add("order,ASC"));
        return Ux.Jooq.on(XMenuDao.class).searchAsync(query).compose(pageData -> {
            final JsonArray source = pageData.getJsonArray(KName.LIST);
            final JsonArray calculate = source.copy();
            final JsonArray result = new JsonArray();
            // APP-MENU
            Ut.itJArray(calculate)
                .filter(json -> "APP-MENU".equals(json.getString(KName.TYPE)))
                .forEach(result::add);
            // TOP-MENU
            Ut.itJArray(calculate)
                .filter(json -> "TOP-MENU".equals(json.getString(KName.TYPE)))
                .forEach(result::add);
            // SIDE-MENU root
            final JsonArray root = new JsonArray();
            Ut.itJArray(calculate)
                .filter(json -> Objects.isNull(json.getString("parentId")))
                .forEach(root::add);

            Ut.itJArray(root).forEach(each -> {
                result.add(each);
                addChild(each, source, result);
            });
            return Ux.future(result);
        });
    }

    private static void addChild(final JsonObject root, final JsonArray source, final JsonArray result) {
        // 1st children
        final JsonArray children = findChildren(root, source);
        Ut.itJArray(children).forEach(child -> {
            // result add child
            result.add(child);
            // result add child's
            addChild(child, source, result);
        });
    }

    private static JsonArray findChildren(final JsonObject root, final JsonArray source) {
        final JsonArray children = new JsonArray();
        Ut.itJArray(source)
            .filter(item -> root.getString(KName.KEY).equals(item.getString("parentId")))
            .forEach(children::add);
        return children;
    }
}
