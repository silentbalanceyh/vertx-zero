package io.vertx.up.commune.wffs;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Formula implements Serializable {
    private final JsonObject tpl = new JsonObject();
    private final JsonObject config = new JsonObject();
    /*
     * name         - expression name
     * expression   - for parameters parsing
     * message      - for message parsing
     * tpl          - for tpl of parameters
     * config       - for expression configuration
     *
     * 1) Extract Data from X_ACTIVITY_RULE
     * 2) Set the value from:
     *    RULE_EXPRESSION   -> expression   JEXL
     *    RULE_TPL          -> tpl          ( JsonObject )
     *    RULE_CONFIG       -> config       ( JsonObject )
     *    RULE_MESSAGE      -> message
     *    RULE_NAME         -> name
     *
     */
    private String name;
    private String expression;
    private String message;
}
