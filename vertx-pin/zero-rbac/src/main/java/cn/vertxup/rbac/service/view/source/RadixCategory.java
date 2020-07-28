package cn.vertxup.rbac.service.view.source;

import cn.vertxup.rbac.service.view.RuleSource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RadixCategory implements RuleSource {
    private static final Annal LOGGER = Annal.get(RadixDynamic.class);

    @Override
    public Future<JsonObject> procAsync(final JsonObject inputData, final JsonObject config) {
        final JsonObject condition = RadixTool.toCriteria(
                inputData, config.getJsonObject("groupCondition"));
        LOGGER.info("Group Condition for Rule: input = {0}, normalized = {1}",
                inputData.encode(), condition.encode());
        return null;
    }
}
