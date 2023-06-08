package io.vertx.up.backbone.regular;

import io.horizon.exception.WebException;
import io.horizon.uca.log.Annal;
import io.vertx.up.atom.Rule;
import io.vertx.up.exception.web._400ValidationRuleException;

public abstract class BaseRuler implements Ruler {

    protected WebException failure(
        final String field,
        final Object value,
        final Rule rule) {
        final String message = rule.getMessage();
        final WebException error = new _400ValidationRuleException(
            this.getClass(), field, value, message);
        error.readable(message);
        this.getLogger().info(INFO.MSG_FAILURE, error.toJson());
        return error;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
