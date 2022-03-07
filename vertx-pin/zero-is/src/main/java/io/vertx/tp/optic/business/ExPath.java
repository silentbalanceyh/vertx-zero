package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.AbstractExPath;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExPath extends AbstractExPath {

    @Override
    public Future<JsonArray> mkdir(final JsonArray data, final JsonObject config) {
        // Compress all operations
        return this.compress(data).compose(normalized ->
            // Group compressed
            this.componentRun(data, (fs, dataGroup) -> fs.mkdir(dataGroup, config)));
    }
}
