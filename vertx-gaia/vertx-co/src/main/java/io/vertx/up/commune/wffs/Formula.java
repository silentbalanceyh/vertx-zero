package io.vertx.up.commune.wffs;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.playbook.Hooker;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Formula implements Serializable {

    private final String key;
    private final transient JsonObject hookerConfig = new JsonObject();
    private transient Playbook playbook;
    /*
     * name         - expression name
     * expression   - for parameters parsing
     * tpl          - for tpl of parameters
     *
     * 1) Extract Data from X_ACTIVITY_RULE
     * 2) Set the value from:
     *    RULE_EXPRESSION   -> expression   JEXL
     *    RULE_TPL          -> tpl          ( JsonObject )
     *    RULE_NAME         -> name
     *
     * The workflow for following four fields:
     * 1) name ( Optional )
     * 2) tpl                   -> 2.1. normalize the parameter template
     *    expression            -> 2.2. expression tpl ( String )
     */
    private String name;
    private transient Hooker hooker;

    public Formula(final String key) {
        this.key = key;
    }

    public Formula bind(final String expression, final JsonObject tpl) {
        this.playbook = Playbook.open(expression);
        this.playbook.bind(tpl);
        return this;
    }

    public Formula bind(final Class<?> component, final JsonObject hookerConfig) {
        if (Objects.nonNull(component) && Ut.isImplement(component, Hooker.class)) {
            final JsonObject configJ = Ut.valueJObject(hookerConfig);
            this.hookerConfig.mergeIn(configJ, true);
            this.hooker = Ut.instance(component);
            this.hooker.bind(this.hookerConfig);
        }
        return this;
    }

    public Future<JsonObject> run(final JsonObject params, final Supplier<Future<JsonObject>> executor) {
        // Playbook Satisfy Checking
        return this.playbook.isSatisfy(params).compose(satisfy -> {
            if (satisfy) {
                return executor.get().compose(processed -> {
                    if (Objects.isNull(this.hooker)) {
                        // The Condition is valid and run executor
                        return Future.succeededFuture(params);
                    } else {
                        // The Hooker will be executed
                        return this.hooker.execAsync(processed);
                    }
                });
            } else {
                return Future.succeededFuture(params);
            }
        });
    }

    public Formula name(final String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return this.name;
    }

    public String key() {
        return this.key;
    }
}
