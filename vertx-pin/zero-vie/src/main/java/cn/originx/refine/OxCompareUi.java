package cn.originx.refine;

import cn.originx.uca.ui.FieldReport;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.ui.cv.em.ControlType;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * ## 界面比对工具
 *
 * > 该工具类在`UI`报表命令中专用，主要用于生成属性报表，判断属性差异！
 *
 * 参考`{@link  FieldReport}`查看属性报表的基础数据结构。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxCompareUi {

    /*
     * 私有构造函数（工具类转换）
     */
    private OxCompareUi() {
    }

    /**
     * 构造比对报表。
     *
     * @param atom  {@link DataAtom}
     * @param forms {@link JsonArray} 当前模型关联的表单集`UI_FORM`
     * @param lists {@link JsonArray} 当前模型关联的列表集`UI_LIST`
     *
     * @return {@link JsonObject} 返回比对报表
     */
    static JsonArray compareUi(final DataAtom atom, final JsonArray forms, final JsonArray lists) {
        /*
         * Form 处理
         */
        final List<FieldReport> reportList = new ArrayList<>();
        Ut.itJArray(forms).map(form -> toForm(form, atom)).forEach(reportList::addAll);
        /*
         * List 处理
         */
        Ut.itJArray(lists).map(list -> toList(list, atom)).forEach(reportList::addAll);
        return Ux.toJson(reportList);
    }

    /**
     * 返回单列表报表。
     *
     * @param list {@link JsonObject} 当前模型关联的列表集`UI_LIST`
     * @param atom {@link DataAtom}
     *
     * @return {@link JsonObject} 返回列表报表
     */
    private static List<FieldReport> toList(final JsonObject list, final DataAtom atom) {
        final ConcurrentMap<String, Class<?>> types = atom.type();
        /* 分组 */
        final ConcurrentMap<String, JsonObject> listMap = Ut.elementMap(Ut.valueJArray(list.getJsonArray(KName.Ui.COLUMNS)), "dataIndex");
        /* 计算 */
        final Supplier<FieldReport> supplier = () -> {
            final FieldReport report = new FieldReport();
            report.setType(ControlType.LIST);
            report.setIdentifier(atom.identifier());
            report.setControl(list.getString(KName.CODE));
            return report;
        };
        return toDiff(listMap.keySet(), types.keySet(), supplier);
    }

    /**
     * 根据UI属性集和模型定义属性集返回最终的属性报表列表。
     *
     * @param uiSet    {@link Set}<{@link String}> UI中的配置属性集
     * @param modelSet {@link Set}<{@link String}> 模型中的配置属性集
     * @param supplier {@link Supplier}<{@link  FieldReport}> 生成属性报表的专用函数
     *
     * @return {@link List}<{@link  FieldReport}> 返回属性报表列表
     */
    private static List<FieldReport> toDiff(final Set<String> uiSet, final Set<String> modelSet, final Supplier<FieldReport> supplier) {
        final List<FieldReport> calculated = new ArrayList<>();
        final Set<String> uiRemain = Ut.diff(uiSet, modelSet);
        final Set<String> modelRemain = Ut.diff(modelSet, uiSet);
        /*
         * REMAIN
         */
        uiRemain.forEach(field -> {
            final FieldReport report = supplier.get();
            report.setUiField(field);
            report.calculate();
            calculated.add(report);
        });
        /*
         * INVALID
         */
        modelRemain.forEach(attribute -> {
            final FieldReport report = supplier.get();
            report.setAttribute(attribute);
            report.calculate();
            calculated.add(report);
        });
        return calculated;
    }

    /**
     * 返回表单配置报表。
     *
     * @param form {@link JsonObject} 当前模型关联的表单集`UI_FORM`
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link JsonObject} 返回表单配置报表
     */
    private static List<FieldReport> toForm(final JsonObject form, final DataAtom atom) {
        final ConcurrentMap<String, Class<?>> types = atom.type();
        /* 分组 */
        final ConcurrentMap<String, JsonObject> formMap = Ut.elementMap(Ut.valueJArray(form.getJsonArray(KName.Modeling.FIELDS)), KName.NAME);
        final ConcurrentMap<String, JsonObject> converted = new ConcurrentHashMap<>();

        formMap.keySet().forEach(item -> {
            final String key;
            if (item.contains(Strings.ACCENT_SIGN)) {
                key = item.split(Strings.ACCENT_SIGN)[0];
            } else {
                key = item;
            }
            converted.put(key, formMap.get(item));
        });
        /* FieldReport */
        final Supplier<FieldReport> supplier = () -> {
            final FieldReport report = new FieldReport();
            report.setType(ControlType.FORM);
            report.setIdentifier(atom.identifier());
            report.setControl(form.getString(KName.CODE));
            return report;
        };
        return toDiff(converted.keySet(), types.keySet(), supplier);
    }
}
