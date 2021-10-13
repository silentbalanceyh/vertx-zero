package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Ignore
public class ElasticTc extends ZeroBase {
    static {
        ElasticSearchInfix.init(VERTX);
    }

    @Test
    public void testSearch(final TestContext context) {
        final ElasticSearchClient client = ElasticSearchInfix.getClient();
        final JsonObject params = this.ioJObject("request.json");

        final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        final JsonObject condition = params.getJsonObject("precision");
        condition.forEach(entry -> map.put(entry.getKey(), entry.getValue().toString()));
        final JsonObject response = client.search(params, map);
        System.out.println(response.encodePrettily());
    }
}
