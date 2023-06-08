package io.horizon.spi.ui;

import io.horizon.specification.typed.TCombiner;
import io.vertx.core.json.JsonObject;

/*
 * Combiner for processing
 * 1. UI_LIST processing, related tables:
 *    UI_LIST, V_QUERY, V_SEARCH, V_TABLE, V_FRAGMENT
 * 2. UI_FORM processing, related tables:
 *    UI_FORM, UI_FIELD
 * 3. UI_OP is standalone and it will not be combined by combiner but another api
 *    instead for frontend usage.
 */
public interface UiControl extends TCombiner<JsonObject> {
}
