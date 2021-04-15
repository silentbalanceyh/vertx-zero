package cn.vertxup.rbac.service.view.source;

import cn.vertxup.rbac.service.view.RuleSource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RadixUi implements RuleSource {
    private static final Annal LOGGER = Annal.get(RadixUi.class);

    @Override
    public Future<JsonObject> procAsync(final JsonObject inputData, final JsonObject config) {
        /*
         * Condition Processing
         *
         * uiCondition: Processing condition
         */
        final JsonObject condition = RadixTool.toCriteria(
                inputData, config.getJsonObject("uiCondition"));
        LOGGER.info("Condition for Rule: input = {0}, normalized = {1}",
                inputData.encode(), condition.encode());
        final UxJooq dao = RadixTool.toDao(config.getString("uiComponent"));
        if (Objects.isNull(dao)) {
            return RadixTool.toResponse(new JsonArray());
        } else {
            return dao.fetchAsync(condition).compose(Ux::futureA)
                    /*
                     * Response processing,
                     * `metadata` field should be serialized
                     */
                    .compose(RadixTool::toResponse);
        }
    }
}
