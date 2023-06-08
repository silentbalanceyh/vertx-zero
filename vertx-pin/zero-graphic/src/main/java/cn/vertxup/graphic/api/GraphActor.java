package cn.vertxup.graphic.api;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.graphic.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.plugin.neo4j.Neo4jClient;
import io.vertx.up.plugin.neo4j.Neo4jInfix;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 *
 */
@Queue
public class GraphActor {

    private static final Annal LOGGER = Annal.get(GraphActor.class);

    @Address(Addr.GRAPH_ANALYZE)
    public Future<JsonObject> analyze(final String key, final String graph, final Integer level) {
        final String graphName = Ut.isNil(graph) ? KWeb.DEPLOY.VERTX_GROUP : graph;
        if (Ut.isNil(key)) {
            return Ux.future(new JsonObject());
        } else {
            final Neo4jClient client = Neo4jInfix.getClient();
            LOGGER.info("[ ZERO ] Graphic analyzing for graph = {0}, key = {1}", graphName, key);
            if (client.connected()) {
                return client.connect(graphName).graphicByKey(key, level).compose(graphic -> {
                    final JsonArray nodeRef = graphic.getJsonArray(KName.Graphic.NODES);
                    Ut.valueToJArray(nodeRef, KName.DATA);
                    // Ut.itJArray(nodeRef).forEach(node -> Ke.mount(node, KName.DATA));
                    final JsonArray edgeRef = graphic.getJsonArray(KName.Graphic.EDGES);
                    Ut.valueToJArray(edgeRef, KName.DATA);
                    // Ut.itJArray(edgeRef).forEach(node -> Ke.mount(node, KName.DATA));
                    return Ux.future(graphic);
                });
            } else {
                return Ux.future(new JsonObject()
                    .put(KName.Graphic.NODES, new JsonArray())
                    .put(KName.Graphic.EDGES, new JsonArray())
                );
            }
        }
    }
}
