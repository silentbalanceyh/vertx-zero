package io.vertx.up.uca.visitor;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.horizon.exception.ZeroException;
import io.vertx.up.exception.heart.LimeFileException;
import io.vertx.up.uca.options.Opts;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class YamlOptsTc extends ZeroBase {

    @Test(expected = LimeFileException.class)
    public void testYaml() throws ZeroException {
        final Opts<JsonObject> opts = Opts.get();
        final JsonObject errors = opts.ingest("erro");
        System.out.println(errors);
    }
}
