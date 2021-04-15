package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.atom.IxViewParam;
import io.vertx.tp.ke.cv.KeField;

import java.util.Objects;

/*
 * {
 *      "identifier": "column identifier bind",
 *      "dynamic": "Whether use dynamic mode to get column",
 *      "view": "DEFAULT, the view name"
 * }
 */
class ApeakActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final IxModule module) {
        /* Column Definition */
        final IxViewParam column = module.getColumn();
        if (Objects.nonNull(column)) {
            /*
             * In static mode, identifier could found ui file
             * In dynamic mode, identifier & sigma could let system fetch columns
             * from database directly.
             * Here add new parameter `view` for future usage to support multi views
             */
            data.put(KeField.IDENTIFIER, column.getIdentifier());
            data.put(KeField.DYNAMIC, column.getDynamic());
            data.put(KeField.VIEW, column.getView());
        }
        return data;
    }
}
