package io.vertx.up.experiment.specification.evaluate;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KExpression implements Serializable {
    /*
     * name         - expression name
     * expression   - for parameters parsing
     * message      - for message parsing
     * tpl          - for tpl of parameters
     * config       - for expression configuration
     * component    - Component class of interface
     *
     * 1) Extract Data from X_ACTIVITY_RULE
     * 2) Set the value from:
     *    RULE_EXPRESSION   -> expression   JEXL
     *    RULE_TPL          -> tpl          ( JsonObject )
     *    RULE_CONFIG       -> config       ( JsonObject )
     *    RULE_MESSAGE      -> message
     *    RULE_NAME         -> name
     *    RULE_COMPONENT    -> component    ( Java Class )
     */
    private String name;
}
