package cn.originx.scaffold.stdn;

import cn.originx.refine.Ox;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class HDiff {

    /**
     * 根据比对结果创建新容器。
     *
     * 1. ADD - 直接构造操作原始比对结果
     * 2. DELETE - 直接构造操作原始比对结果
     * 3. UPDATE - 下层逻辑
     *
     * 更新逻辑细节：
     *
     * 1. 条件过滤，调用内置`dataUnique()`方法（子类实现）
     * 2. 执行数据计算结果
     *
     * @param apt   {@link Apt} 比对结果容器
     * @param group {@link JsonArray} 单组容器
     *
     * @return {@link Apt}
     */
    static Apt createApt(final Apt apt, final JsonArray group, final String diffKey) {
        final ChangeFlag type = apt.type();
        if (ChangeFlag.ADD == type) {
            return Apt.create(new JsonArray(), group).compared(apt.compared());
        } else if (ChangeFlag.DELETE == type) {
            return Apt.create(apt.dataO(), new JsonArray()).compared(apt.compared());
        } else {
            final JsonArray lookup = new JsonArray();
            /* 读取 group 中的 key */
            final Set<String> keys = Ut.mapString(group, diffKey);
            /* 读取 新数据 */
            final JsonArray current = apt.dataN();
            Ut.itJArray(current).filter(json -> keys.contains(json.getString(diffKey)))
                .forEach(lookup::add);
            return Apt.create(apt.dataO(), lookup, diffKey).compared(apt.compared());
        }
    }

    /**
     * 该方法为更新过程中的专用方法，会执行核心的更新逻辑。
     *
     * <strong>第一层逻辑</strong>
     *
     * - 遍历{@link io.vertx.core.json.JsonObject}直接提取Json对象引用。
     * - 遍历{@link io.vertx.core.json.JsonArray}中的每一个对象，提取Json对象引用。
     * - 在每一个元素中执行 `Function<JsonObject,T>`函数。
     *
     * <strong>第二层逻辑</strong>
     *
     * 判断逻辑，根据输入的第二参数执行判断：
     *
     * - 「Creation」去掉所有`null`对象过后执行更新。
     * - 「Edition」执行第三层核心逻辑。
     *
     * <strong>第三层逻辑</strong>
     *
     * 从系统中提取所有不执行清空的属性`notnullFields`，这种类型的属性表如下：
     *
     * |属性名|备注|
     * |---:|:---|
     * |createdAt|创建时间。|
     * |createdBy|创建人。|
     * |updatedAt|更新时间。|
     * |updatedBy|更新人。|
     * |globalId|第三方集成专用全局ID（唯一键）。|
     * |confirmStatus|对象确认状态。|
     * |`inSync = false`|配置的不需要从集成端输入的属性。|
     *
     * <strong>第四层逻辑（位于`notnullFields`中）</strong>
     *
     * 1. 如果输入的`field = null`，则设置`normalized`中的`field = value`（原值），并且从原始记录中移除`field`。
     * 2. 如果输入的`field = value`，则直接将该值传入normalized中。
     *
     * <strong>第四层逻辑（不包含在`notnullFields`中）</strong>
     *
     * 1. 如果输入值中包含`field = value`，则更新`normalized`中的`value`值。
     * 2. 如果输入值中不包含`field`，则直接清空`normalized`中的值。
     *
     * > 最终计算的`normalized`会覆盖原始记录对象数据。
     *
     * @param original 原始数据
     * @param updated  {@link io.vertx.core.json.JsonObject} 待更新数据对象
     * @param atom     {@link DataAtom} 模型信息
     * @param <T>      内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return 返回执行完成后的内容对象
     */
    @SuppressWarnings("all")
    static <T> T execute(final T original, final JsonObject updated, final DataAtom atom) {
        return Ut.itJson(original, reference -> {
            // updated 为 null
            if (Objects.isNull(updated)) {
                final JsonObject normalized = new JsonObject();
                // 「Creation」移除 null 值
                atom.attributeNames().stream()
                    .filter(field -> Objects.nonNull(reference.getValue(field)))
                    .forEach(field -> normalized.put(field, reference.getValue(field)));
                return (T) normalized;
            } else {
                // 「Edition」复杂计算
                final JsonObject normalized = new JsonObject();
                final Set<String> notnullFields = Ox.ignoreIn(atom);     // inSync = false
                atom.attributeNames().forEach(field -> {
                    /*
                     * 三份数据
                     * 1. reference 旧数据
                     * 2. updated 新数据
                     * 3. notnullFields 不执行清空的属性
                     */
                    if (notnullFields.contains(field)) {
                        // 不支持清空
                        final Object updatedValue = updated.getValue(field);
                        if (Objects.isNull(updatedValue)) {
                            // 原值
                            normalized.put(field, reference.getValue(field));
                            /*
                             * 移除 null，解决原值问题
                             */
                            reference.remove(field);
                        } else {
                            // 新非空值
                            normalized.put(field, updatedValue);
                        }
                    } else {
                        // 支持清空
                        if (updated.containsKey(field)) {
                            // 直接提取
                            normalized.put(field, updated.getValue(field));
                        } else {
                            // 未输入就清空
                            normalized.putNull(field);
                        }
                    }
                });
                return (T) normalized;
            }
        });
    }
}
