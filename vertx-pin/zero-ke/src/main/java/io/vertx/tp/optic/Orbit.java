package io.vertx.tp.optic;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;

/*
 * Interface for authorization resource key processing
 * 1) Request Uri: /api/group/search
 * 2) Pattern Uri:  /api/:actor/search
 * 3) Resolution for request key:
 *
 */
public interface Orbit {

    String ARG0 = KeField.URI;
    String ARG1 = KeField.URI_REQUEST;

    /*
     * Calculation method here, stack should be
     * parameter stack
     */
    String analyze(JsonObject arguments);
}
