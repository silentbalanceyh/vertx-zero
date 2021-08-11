package cn.vertxup.graphic.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.graphic.cv.Addr;
import io.vertx.up.eon.KName;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.neo4j.Neo4jClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.Constants;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 *
 */
@Queue
public class GraphActor {

    private static final Annal LOGGER = Annal.get(GraphActor.class);

    @Plugin
    private transient Neo4jClient client;

    @Address(Addr.GRAPH_ANALYZE)
    public Future<JsonObject> analyze(final String key, final String graph, final Integer level) {
        final String graphName = Ut.isNil(graph) ? Constants.DEFAULT_GROUP : graph;
        if (Ut.isNil(key)) {
            return Ux.future(new JsonObject());
        } else {
            LOGGER.info("[ ZERO ] Graphic analyzing for graph = {0}, key = {1}", graphName, key);
            if (this.client.connected()) {
                return this.client.connect(graphName).graphicByKey(key, level).compose(graphic -> {
                    final JsonArray nodeRef = graphic.getJsonArray(KName.Graphic.NODES);
                    Ut.itJArray(nodeRef).forEach(node -> Ke.mount(node, KName.DATA));
                    final JsonArray edgeRef = graphic.getJsonArray(KName.Graphic.EDGES);
                    Ut.itJArray(edgeRef).forEach(node -> Ke.mount(node, KName.DATA));
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
