package io.horizon.spi.robin;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.data.DataGroup;

import java.util.Set;

/*
 * Switcher for DataAtom
 */
public interface Switcher {
    /*
     * Single record switching
     */
    Future<DataAtom> atom(JsonObject data, DataAtom defaultAtom);

    /*
     * Multi record switching
     */
    Future<Set<DataGroup>> atom(JsonArray data, DataAtom atom);
}
