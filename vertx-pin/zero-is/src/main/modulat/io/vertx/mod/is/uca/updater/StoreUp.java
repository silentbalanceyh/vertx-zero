package io.vertx.mod.is.uca.updater;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 1. Integration ID changing
 * 2. StorePath changing
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface StoreUp {

    Future<IDirectory> migrate(IDirectory directory, JsonObject directoryJ);
}
