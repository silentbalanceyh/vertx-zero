package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.AbstractExPath;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExPath extends AbstractExPath {

    @Override
    public Future<JsonArray> mkdir(final JsonArray data, final JsonObject config) {
        return this.componentRun(data, (fs, dataGroup) -> fs.mkdir(dataGroup, config));
    }
}
