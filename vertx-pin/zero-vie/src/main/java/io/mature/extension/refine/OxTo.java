package io.mature.extension.refine;

import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.cv.OxCv;
import io.modello.atom.app.KDS;
import io.modello.specification.action.HDao;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ## 数据工具
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxTo {

    /*
     * 私有构造函数（工具类转换）
     */
    private OxTo() {
    }

    /**
     * 根据<strong>应用标识</strong>和<strong>模型标识</strong>构造模型定义对象。
     *
     * @param key        {@link String} 应用标识，可以是`appId`、也可以是`sigma`
     * @param identifier {@link String} 模型统一标识符
     *
     * @return {@link DataAtom} 模型定义对象
     */
    static DataAtom toAtom(final String key, final String identifier) {
        //        final JtApp app = AmbientOld.getApp(key);
        final HArk ark = Ke.ark(key);
        final HApp app = ark.app();
        if (Objects.isNull(app)) {
            Ox.LOG.Uca.warn(OxTo.class, "应用为空！key = {0}, identifier = {1}", key, identifier);
            return null;
        } else {
            final String appName = app.name();
            return Ao.toAtom(appName, identifier);
        }
    }

    /**
     * 根据<strong>应用标识</strong>和<strong>模型标识</strong>构造数据库访问器`Dao`对象。
     *
     * @param key        {@link String} 应用标识，可以是`appId`、也可以是`sigma`
     * @param identifier {@link String} 模型统一标识符
     *
     * @return {@link HDao} 数据库访问对象
     */
    static HDao toDao(final String key, final String identifier) {
        final HArk ark = Ke.ark(key);
        final KDS<Database> ds = ark.database();
        //        final JtApp app = AmbientOld.getApp(key);
        final Database database = ds.dynamic(); // Objects.isNull(app) ? null : app.getSource();
        return Ao.toDao(toAtom(key, identifier), database);
    }

    /**
     * 「Node」图节点数组格式化专用方法。
     *
     * 格式化判断：
     *
     * - 如果出现相同的`globalId`则直接忽略，先执行节点合并（按`globalId`执行压缩）。
     * - 每一个节点内部调用`toNode`重载方法（{@link JsonObject}类型处理）。
     *
     * @param nodeData {@link JsonArray} 待格式化的图节点数组
     *
     * @return {@link JsonArray} 完成格式化的图节点数组
     */
    static JsonArray toNode(final JsonArray nodeData) {
        final JsonArray normalized = new JsonArray();
        final Set<String> globalIds = new HashSet<>();
        Ut.itJArray(nodeData).map(item -> {
            if (!globalIds.contains(item.getString(KName.GLOBAL_ID))) {
                globalIds.add(item.getString(KName.GLOBAL_ID));
                return item;
            } else {
                Ox.LOG.Uca.warn(OxTo.class, "忽略记录：key = {0}, id = {1}, data = {2}",
                    item.getString(KName.KEY), item.getString(KName.GLOBAL_ID), item.encode());
                return null;
            }
        }).filter(Objects::nonNull).map(OxTo::toNode).filter(Objects::nonNull).forEach(normalized::add);
        return normalized;
    }

    /**
     * 「Node」图节点格式化专用方法。
     *
     * 格式化细节：
     *
     * - 将`globalId`赋值给`key`属性。
     * - 拷贝`name`属性。
     * - 拷贝`code`属性。
     * - 将原始数据{@link JsonObject}拷贝到`data`属性中。
     *
     * @param nodeData {@link JsonObject} 待格式化的图节点对象
     *
     * @return {@link JsonObject} 完成格式化的图节点
     */
    static JsonObject toNode(final JsonObject nodeData) {
        if (Objects.isNull(nodeData.getValue(KName.GLOBAL_ID))) {
            return null;
        } else {
            final JsonObject node = new JsonObject();
            /*
             * key值是 globalId 的值，这一点必须注意
             */
            node.put(KName.KEY, nodeData.getValue(KName.GLOBAL_ID));
            node.put(KName.NAME, nodeData.getValue(KName.NAME));
            node.put(KName.CODE, nodeData.getValue(KName.CODE));
            node.put(KName.DATA, nodeData);
            return node;
        }
    }

    /**
     * 「Edge」图边数组格式化专用方法。
     *
     * > 内部调用`toEdge`的重载方法（{@link JsonObject}类型）。
     *
     * @param edgeData {@link JsonArray} 待格式化的边数据数组
     *
     * @return {@link JsonArray} 已格式化的边数据数组
     */
    static JsonArray toEdge(final JsonArray edgeData) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(edgeData).map(OxTo::toEdge).forEach(normalized::add);
        return normalized;
    }

    /**
     * 「Edge」图边格式化专用方法
     *
     * 格式化细节：
     *
     * - 拷贝`sourceGlobalId`到`source`属性中。
     * - 拷贝`targetGlobalId`到`target`属性中。
     * - 拷贝`type`边类型到`type`属性中。
     * - 将原始数据{@link JsonObject}拷贝到`data`属性中。
     *
     * @param edgeData {@link JsonObject} 待格式化的边对象
     *
     * @return {@link JsonObject} 已格式化的边对象
     */
    static JsonObject toEdge(final JsonObject edgeData) {
        final JsonObject edge = new JsonObject();
        edge.put(OxCv.Relation.SOURCE_PREFIX, edgeData.getValue(OxCv.Relation.SOURCE_JOINED));
        edge.put(OxCv.Relation.TARGET_PREFIX, edgeData.getValue(OxCv.Relation.TARGET_JOINED));
        edge.put(KName.TYPE, edgeData.getValue(KName.TYPE));
        edge.put(KName.DATA, edgeData);
        return edge;
    }

    /**
     * 提取上下游关系合并到一起。
     *
     * - down：下游属性。
     * - up：上游属性。
     *
     * @param source 输入的源类型数据
     * @param <T>    输入的源中元素的Java类型
     *
     * @return 拉平过后的关系信息
     */
    @SuppressWarnings("unchecked")
    static <T> JsonArray toLinker(final T source) {
        final JsonArray links = (JsonArray) Ut.itJson(source, json -> {
            final JsonArray normalized = new JsonArray();
            normalized.addAll(Ut.valueJArray(json.getJsonArray(OxCv.RELATION_DOWN)));
            normalized.addAll(Ut.valueJArray(json.getJsonArray(OxCv.RELATION_UP)));
            return (T) normalized;
        });
        return Ut.elementFlat(links);
    }
}
