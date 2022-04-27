package cn.originx.refine;

import cn.originx.cv.OxCv;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ## 字段工具
 *
 * ### 1. 基本介绍
 *
 * 用来计算字段相关信息的专用<strong>字段工具</strong>类信息。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxField {
    /**
     * <value>key, createdAt, createdBy, updatedAt, updatedBy, confirmStatus</value>，默认忽略字段。
     *
     * - key：主键
     * - createdAt：创建时间
     * - createdBy：创建人
     * - updatedAt：更新时间
     * - updatedBy：更新人
     * - confirmStatus：确认状态（confirmed，已确认 / unconfirmed，未确认）
     */
    private static final Set<String> IGNORE_FIELDS = new HashSet<String>() {
        {
            /* 主键，上下关系 */
            this.add(KName.KEY);
            /* Auditor，字段信息 */
            this.add(KName.CREATED_AT);
            this.add(KName.CREATED_BY);
            this.add(KName.UPDATED_AT);
            this.add(KName.UPDATED_BY);
            /* confirmStatus，在 Tracking 中取消 */
            this.add(OxCv.Field.CONFIRM_STATUS);
            this.add(OxCv.Field.LIFE_CYCLE);
            this.add(KName.GLOBAL_ID);
        }
    };
    private static final Set<String> IGNORE_API = new HashSet<String>() {
        {
            this.add(KName.KEY);
            this.add(KName.CODE);
            this.add(OxCv.Field.CATEGORY_THIRD);
            this.add(OxCv.Field.CATEGORY_SECOND);
            this.add(OxCv.Field.CATEGORY_FIRST);
            /* confirmStatus，在 Tracking 中取消 */
            this.add(OxCv.Field.LIFE_CYCLE);
            this.add(OxCv.Field.CONFIRM_STATUS);
        }
    };

    /*
     * 私有构造函数（工具类转换）
     */
    private OxField() {
    }

    /**
     * 返回默认忽略字段集 + 输入忽略字段集，合并返回。
     *
     * @param ignores 输入的忽略字段集。
     *
     * @return {@link Set}<{@link String}> 默认字段集
     */
    static Set<String> toIgnores(final Set<String> ignores) {
        final Set<String> ignoreSet = new HashSet<>(IGNORE_FIELDS);
        ignoreSet.addAll(ignores);
        return ignoreSet;
    }

    /*
     * ATOM-001/ATOM-003
     * 这个流程需要创建待确认，所以它的 ignoreSet 的计算为：
     * 1. 本身不执行变更历史生成的字段：track = false
     * 2. 拉取数据过程中准入禁用的字段：syncIn = false
     */
    static Set<String> ignorePull(final DataAtom atom) {
        final Set<String> fieldSet = toIgnores(atom.falseTrack());
        fieldSet.addAll(atom.falseIn());
        return fieldSet;
    }

    static Set<String> ignorePush(final DataAtom atom) {
        return atom.falseOut();
    }

    /*
     * compareEdit比对流程专用
     */
    static Set<String> ignoreIn(final DataAtom atom) {
        return toIgnores(atom.falseIn());
    }

    static Set<String> ignorePure(final DataAtom atom) {
        return toIgnores(atom.falseTrack());
    }

    static Set<String> ignoreApi(final DataAtom atom) {
        final RuleUnique unique = atom.ruleAtom();
        final Set<String> fieldSet = new HashSet<>(IGNORE_API);
        if (Objects.nonNull(unique)) {
            unique.rulePure().stream()
                .flatMap(term -> term.getFields().stream())
                .forEach(fieldSet::add);
        }
        return fieldSet;
    }

    /**
     * 元数据执行器
     *
     * 支持功能：
     *
     * - 针对字段`metadata`执行Json转换。
     * - 按`visible = true/false`执行过滤，如果不存在则默认为`true`，筛选元素。
     * - 针对字段`columns`执行Json转换。
     *
     * @param input 输入的最终响应数据
     *
     * @return {@link JsonArray} 同步执行结果
     */
    static JsonArray metadata(final JsonArray input) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(input)
            .map(item -> Ut.ifJObject(item, KName.METADATA))
            .filter(item -> {
                final JsonObject metadata = item.getJsonObject(KName.METADATA);
                if (Ut.notNil(metadata)) {
                    return metadata.getBoolean("visible", Boolean.TRUE);
                } else {
                    return true;
                }
            })
            .map(item -> Ut.ifString(item, KName.Ui.COLUMNS))
            .forEach(normalized::add);
        return normalized;
    }
}
