package io.vertx.tp.crud.uca.normalize;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.view.KColumn;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ApeakPre implements Pre {
    /*
     * {
     *      "identifier": "column identifier bind",
     *      "dynamic": "Whether use dynamic mode to get column",
     *      "view": "DEFAULT, the view name"
     * }
     */
    @Override
    public Future<JsonObject> setUp(final JsonObject data, final IxIn in) {
        final KModule module = in.module();
        /* Column definition */
        final KColumn column = module.getColumn();
        assert null != column : "The column definition should not be null";
        /*
         * In static mode, identifier could found ui file
         * In dynamic mode, identifier & sigma could let system fetch columns
         * from database directly.
         * Here add new parameter `view` for future usage to support multi views
         */
        data.put(KName.IDENTIFIER, column.getIdentifier());
        data.put(KName.DYNAMIC, column.getDynamic());
        Fn.safeSemi(Objects.isNull(data.getValue(KName.VIEW)), () -> data.put(KName.VIEW, column.getView()));
        return Ux.future(data);
    }
}
