package cn.vertxup.rbac.service.view.source;

import cn.vertxup.rbac.service.view.RuleSource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RadixGroup implements RuleSource {

    @Override
    public Future<JsonObject> procAsync(final JsonObject inputData, final JsonObject config) {
        final JsonObject exprTpl = config.getJsonObject("groupCondition");

        final JsonObject condition = Ut.fromExpression(exprTpl, inputData);
        // RadixTool.toCriteria(inputData, config.getJsonObject("groupCondition"));
        Sc.infoView(this.getClass(), "Group Condition for Rule: input = {0}, normalized = {1}",
            inputData.encode(), condition.encode());
        final UxJooq dao = RadixTool.toDao(config.getString("groupComponent"));
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
