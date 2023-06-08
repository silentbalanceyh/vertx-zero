package io.vertx.mod.jet.uca.valve;

import io.horizon.uca.cache.Cc;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;

/*
 * IN_RULE
 * IN_MAPPING
 * IN_PLUG
 * IN_SCRIPT
 */
public interface JtIn {
    Cc<String, JtIn> CC_IN = Cc.openThread();

    static JtIn rule() {
        return CC_IN.pick(RuleValve::new, RuleValve.class.getName());
    }

    static JtIn mapping() {
        return CC_IN.pick(MappingValve::new, MappingValve.class.getName());
    }

    static JtIn plug() {
        return CC_IN.pick(PlugValve::new, PlugValve.class.getName());
    }

    static JtIn script() {
        return CC_IN.pick(ScriptValve::new, ScriptValve.class.getName());
    }

    /*
     * IN_RULE
     */
    default Envelop execute(final Envelop envelop, final JtUri uri) {
        /* Default do nothing */
        return envelop;
    }

}

