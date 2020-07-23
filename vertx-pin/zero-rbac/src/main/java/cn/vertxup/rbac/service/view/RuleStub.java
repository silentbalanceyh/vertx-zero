package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface RuleStub {
    /*
     * Process SPath
     */
    Future<JsonArray> procAsync(final List<SPath> paths);
}
