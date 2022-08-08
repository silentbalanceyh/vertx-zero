package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.SourceGroup;
import io.vertx.tp.rbac.cv.em.SourceType;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RuleRobin {

    static Future<JsonObject> procRule(final SPath path) {
        final JsonObject serialized = Ut.serializeJson(path);
        return Ut.ifJObject(
            "uiCondition",
            "uiConfig",
            "groupCondition",
            "groupConfig",
            "groupMapping"
        ).apply(serialized).compose(processed -> {
            /*
             * Group Process first, there are some conflicts between Group/Source
             *
             * 1) It means that if here contains group definition, the source will be used in future
             * 2) Otherwise when there is source definition only, the group may be discard.
             */
            final SourceGroup group = Ut.toEnum(path::getDmType, SourceGroup.class, SourceGroup.NONE);
            if (SourceGroup.NONE == group) {
                /*
                 * There is no group defined, you can fetch data from ui condition directly instead of
                 * group here.
                 */
                final SourceType type = Ut.toEnum(path::getUiType, SourceType.class, SourceType.NONE);
                if (SourceType.NONE == type) {
                    return Ux.future(processed);
                } else {
                    /* node `ui` processing */
                    return Ux.future(processed).compose(RuleRobin.toUi(type, path));
                }
            } else {
                /*
                 * There must be a group action on node selection in each group, for example:
                 *
                 * node1
                 * |- node11
                 *      |- node111
                 *      |- node112
                 * |- node12
                 *
                 * You must select one `node` as condition for secondary fetching
                 */
                return Ux.future(processed).compose(RuleRobin.toGroup(group, path));
            }
        });
    }

    private static Function<JsonObject, Future<JsonObject>> toGroup(final SourceGroup group, final SPath path) {
        return processed -> {
            final RuleSource dao = RuleSource.GROUPS.get(group);
            if (Objects.isNull(dao)) {
                return Ux.future(processed);
            } else {
                return dao.procAsync(toCriteria(path), processed).compose(ui -> {
                    /*
                     * group node for group source
                     */
                    processed.put(KName.GROUP, ui);
                    processed.remove("groupComponent");
                    processed.remove("groupCondition");
                    processed.remove("groupType");
                    return Ux.future(processed);
                });
            }
        };
    }

    private static Function<JsonObject, Future<JsonObject>> toUi(final SourceType type, final SPath path) {
        return processed -> {
            final RuleSource dao = RuleSource.UIS.get(type);
            if (Objects.isNull(dao)) {
                return Ux.future(processed);
            } else {
                return dao.procAsync(toCriteria(path), processed).compose(ui -> {
                    /*
                     * ui node for ui source
                     * */
                    processed.put("ui", ui);
                    processed.remove("uiCondition");
                    processed.remove("uiComponent");
                    processed.remove("uiType");
                    return Ux.future(processed);
                });
            }
        };
    }

    private static JsonObject toCriteria(final SPath path) {
        final JsonObject inputData = new JsonObject();
        inputData.put(KName.SIGMA, path.getSigma());
        inputData.put(KName.LANGUAGE, path.getLanguage());
        return inputData;
    }
}
