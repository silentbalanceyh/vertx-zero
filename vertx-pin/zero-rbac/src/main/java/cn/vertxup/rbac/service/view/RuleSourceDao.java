package cn.vertxup.rbac.service.view;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RuleSourceDao implements RuleSource {
    @Override
    public Future<JsonObject> procAsync(final JsonObject inputData, final JsonObject config) {
        /*
         * Condition Processing
         */
        final JsonObject condition = this.toCriteria(inputData, config.getJsonObject("uiCondition"));
        final UxJooq dao = this.toDao(config);
        if (Objects.isNull(dao)) {
            return Ux.futureJObject();
        } else {
            return dao.findAsync(condition).compose(Ux::fnJArray).compose(data -> {
                final JsonObject normalized = new JsonObject();
                normalized.put(KeField.DATUM, data);
                return Ux.future(normalized);
            });
        }
    }

    private UxJooq toDao(final JsonObject config) {
        final String daoClsStr = config.getString("uiComponent");
        if (Ut.isNil(daoClsStr)) {
            return null;
        } else {
            final Class<?> daoCls = Ut.clazz(daoClsStr, null);
            if (Objects.isNull(daoCls)) {
                return null;
            } else {
                return Ux.Jooq.on(daoCls);
            }
        }
    }

    private JsonObject toCriteria(final JsonObject inputData, final JsonObject tpl) {
        final JsonObject normalized = tpl.copy();
        Ut.itJObject(tpl, (item, field) -> {
            if (item instanceof String) {
                final String literal = item.toString();
                if (literal.contains("`")) {
                    final String formatted = Ut.fromExpression(literal, inputData);
                    normalized.put(field, formatted);
                }
            } else if (item instanceof JsonObject) {
                final JsonObject input = ((JsonObject) item);
                final JsonObject formatted = this.toCriteria(inputData, input);
                normalized.put(field, formatted);
            }
        });
        return normalized;
    }
}
