package io.vertx.up.commune.wffs;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.playbook.Hooker;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Formula implements Serializable {
    private final JsonObject tpl = new JsonObject();
    private final JsonObject config = new JsonObject();
    private final String expression;
    private final transient JsonObject hookerConfig = new JsonObject();
    /*
     * name         - expression name
     * expression   - for parameters parsing
     * tpl          - for tpl of parameters
     * config       - for expression configuration
     *
     * 1) Extract Data from X_ACTIVITY_RULE
     * 2) Set the value from:
     *    RULE_EXPRESSION   -> expression   JEXL
     *    RULE_TPL          -> tpl          ( JsonObject )
     *    RULE_CONFIG       -> config       ( JsonObject )
     *    RULE_NAME         -> name
     *
     * The workflow for following four fields:
     * 1) name ( Optional )
     * 2) tpl                   -> 2.1. normalize the parameter template
     *    expression            -> 2.2. expression tpl ( String )
     *    config                -> 2.3. config data of current expression
     */
    private String name;
    private transient Hooker hooker;

    public Formula(final String expression) {
        this.expression = expression;
    }

    public Formula bind(final JsonObject tpl, final JsonObject config) {
        final JsonObject tplJ = Ut.valueJObject(tpl);
        this.tpl.mergeIn(tplJ, true);
        final JsonObject configJ = Ut.valueJObject(config);
        this.config.mergeIn(configJ, true);
        return this;
    }

    public Formula bind(final Class<?> component, final JsonObject hookerConfig) {
        if (Objects.nonNull(component) && Ut.isImplement(component, Hooker.class)) {
            final JsonObject configJ = Ut.valueJObject(hookerConfig);
            this.hookerConfig.mergeIn(configJ, true);
            this.hooker = Ut.instance(component);
        }
        return this;
    }

    public Formula name(final String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return this.name;
    }
}
