package io.vertx.tp.jet.uca.valve;

import io.vertx.tp.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;

/*
 * IN_RULE
 * IN_MAPPING
 * IN_PLUG
 * IN_SCRIPT
 */
public interface JtIn {
    static JtIn rule() {
        return Fn.poolThread(Pool.IN_RULE, RuleValve::new);
    }

    static JtIn mapping() {
        return Fn.poolThread(Pool.IN_MAPPING, MappingValve::new);
    }

    static JtIn plug() {
        return Fn.poolThread(Pool.IN_PLUG, PlugValve::new);
    }

    static JtIn script() {
        return Fn.poolThread(Pool.IN_SCRIPT, ScriptValve::new);
    }

    /*
     * IN_RULE
     */
    default Envelop execute(final Envelop envelop, final JtUri uri) {
        /* Default do nothing */
        return envelop;
    }

}

