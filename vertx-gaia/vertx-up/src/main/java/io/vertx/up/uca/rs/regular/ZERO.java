package io.vertx.up.uca.rs.regular;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, Ruler> RULERS = new ConcurrentHashMap<String, Ruler>() {
        {
            this.put("required", new RequiredRuler());
            this.put("length", new LengthRuler());
            this.put("minlength", new MinLengthRuler());
            this.put("maxlength", new MaxLengthRuler());
            this.put("empty", new EmptyRuler());
            this.put("singlefile", new SingleFileRuler());
        }
    };
}

interface Info {

    String MSG_FAILURE = "Rule validation failure: {0}";
}
