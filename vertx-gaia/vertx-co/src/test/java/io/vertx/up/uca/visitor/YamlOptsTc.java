package io.vertx.up.uca.visitor;

import io.horizon.exception.ProgramException;
import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.internal.LimeMissingException;
import io.vertx.up.uca.options.Opts;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class YamlOptsTc extends ZeroBase {

    @Test(expected = LimeMissingException.class)
    public void testYaml() throws ProgramException {
        final Opts<JsonObject> opts = Opts.get();
        final JsonObject errors = opts.ingest("erro");
        System.out.println(errors);
    }
}
