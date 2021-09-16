package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DiFTreePre implements Pre {
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        return Ix.onTFrom(data, in).compose(map -> T.treeAsync(data, in, map));
    }
}
