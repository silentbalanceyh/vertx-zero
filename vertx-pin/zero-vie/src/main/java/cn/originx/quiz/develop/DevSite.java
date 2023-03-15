package cn.originx.quiz.develop;

import cn.vertxup.ambient.domain.tables.daos.XMenuDao;
import cn.vertxup.ambient.domain.tables.pojos.XMenu;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DevSite {
    private static final ConcurrentMap<Integer, Integer> SEQ_STORE = new ConcurrentHashMap<>() {
        {
            this.put(1, 10000);
            this.put(2, 1000);
            this.put(3, 1000);
            this.put(4, 1000);
        }
    };
    private static final ConcurrentMap<Integer, Integer> SEQ_STEP = new ConcurrentHashMap<>() {
        {
            this.put(1, 1000);
            this.put(2, 100);
            this.put(3, 5);
            this.put(4, 5);
        }
    };

    /*
     * 解析 menu.yml 菜单信息
     */
    public static Future<Boolean> planOn(final String path) {
        final JsonObject menuMap = Ut.ioYaml(path);

        final UxJooq jooq = Ux.Jooq.on(XMenuDao.class);

        return jooq.<XMenu>fetchAllAsync().compose(menus -> {
            // 先构造一个Map ( name = key )
            final ConcurrentMap<String, String> mapped = Ut.elementMap(menus, XMenu::getName, XMenu::getKey);
            // Remove Set（移除菜单处理）
            final ConcurrentMap<String, JsonObject> parsed = planTree(menuMap, mapped);
            /*
             * 根据 name 查找并更新相关数据
             */
            final List<XMenu> updateQ = new ArrayList<>();
            final Set<String> removeSet = new HashSet<>();
            menus.forEach(menu -> {
                if (parsed.containsKey(menu.getName())) {
                    final XMenu updated = Ux.updateT(menu, parsed.get(menu.getName()));
                    updateQ.add(updated);
                } else {
                    if (!"DEV-MENU".equals(menu.getType())) {
                        removeSet.add(menu.getName());
                    }
                }
            });
            return jooq.updateAsync(updateQ).compose(updated -> {
                Ke.infoKe(DevSite.class, "更新菜单数：{0}", String.valueOf(updated.size()));
                return Ux.future(removeSet);
            });
        }).compose(removeSet -> {
            Ke.infoKe(DevSite.class, "移除菜单数量：{0}", String.valueOf(removeSet.size()));
            final JsonObject nameQr = Ux.whereAnd();
            nameQr.put(KName.NAME + ",i", Ut.toJArray(removeSet));
            return jooq.deleteByAsync(nameQr);
        });
    }

    private static ConcurrentMap<String, JsonObject> planTree(final JsonObject menuMap,
                                                              final ConcurrentMap<String, String> mapped) {
        // sider  BAG + SIDE
        final ConcurrentMap<String, JsonObject> menuData =
            new ConcurrentHashMap<>(planTree(menuMap.getJsonObject("sider"), mapped, null));

        // NAV
        final ConcurrentMap<String, JsonObject> appData = planTree(menuMap.getJsonObject("dash.app"), mapped, null);
        appData.keySet().stream()
            .filter(key -> !menuData.containsKey(key))
            .forEach(each -> menuData.put(each, appData.get(each)));
        // SC
        menuData.putAll(planTree(menuMap.getJsonObject("dash.catalog"), mapped, null));

        // extra EXTRA + TOP
        menuData.putAll(planTree(menuMap.getJsonObject("extra.navigation"), mapped, null));
        menuData.putAll(planTree(menuMap.getJsonObject("extra.account"), mapped, null));
        return menuData;
    }

    private static ConcurrentMap<String, JsonObject> planTree(final JsonObject treeData,
                                                              final ConcurrentMap<String, String> mapped,
                                                              final Kv<String, Integer> parent) {
        /*
           zero.desktop@10000:
             zero.desktop.my@2000:
               zero.desktop.my.task:
               zero.desktop.my.report:
         */
        final Integer defaultLevel = Objects.isNull(parent) ? 1 : parent.getValue() + 1;
        final Integer defaultOrder = SEQ_STORE.get(defaultLevel);
        final Integer defaultStep = SEQ_STEP.get(defaultLevel);

        final DevSiteStep step = new DevSiteStep(defaultStep);
        step.jump(defaultOrder);

        final ConcurrentMap<String, JsonObject> parsed = new ConcurrentHashMap<>();

        treeData.fieldNames().forEach(field -> {
            // 解析当前节点
            final JsonObject menuJ = planParse(field, step);
            if (Objects.nonNull(parent)) {
                final String parentKey = mapped.get(parent.getKey());
                menuJ.put(KName.PARENT_ID, parentKey);
            }
            menuJ.put(KName.LEVEL, defaultLevel);
            parsed.put(menuJ.getString(KName.NAME), menuJ);

            /*  DEBUG专用
                final Integer indent = menuJ.getInteger(KName.LEVEL);
                Ut.itRepeat(indent, () -> System.out.print("    "));
                System.out.println(indent + "," + menuJ.getString(KName.NAME) + "," + menuJ.getInteger(KName.ORDER));
            */
            // 解析子节点
            final Object children = treeData.getValue(field);
            if (Objects.nonNull(children)) {
                final Kv<String, Integer> kv = Kv.create();
                kv.set(menuJ.getString(KName.NAME), menuJ.getInteger(KName.LEVEL));
                final ConcurrentMap<String, JsonObject> treeChild = planTree((JsonObject) children, mapped, kv);
                parsed.putAll(treeChild);
            }
        });
        return parsed;
    }

    private static JsonObject planParse(final String field, final DevSiteStep step) {
        final JsonObject menuData = new JsonObject();
        final String expr;
        if (field.contains("`")) {
            final String[] segments = field.split("`");
            expr = segments[0];
            menuData.put(KName.TEXT, segments[1]);
        } else {
            expr = field;
        }

        if (expr.contains("@")) {
            final String[] segments = expr.split("@");
            menuData.put(KName.NAME, segments[0]);
            final Integer seed = Integer.parseInt(segments[1]);
            menuData.put(KName.ORDER, seed);
            step.jump(seed);
        } else {
            menuData.put(KName.NAME, expr);
            menuData.put(KName.ORDER, step.value());
        }
        step.moveOn();
        return menuData;
    }
}