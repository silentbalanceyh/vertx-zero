package io.vertx.up.plugin.neo4j.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
class N4JEdge {

    static List<String> add(final String graph, final JsonArray edges) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(edges).map(item -> add(graph, item)).forEach(commands::add);
        return commands;
    }

    static String add(final String graph, final JsonObject edge) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MATCH (source:").append(graph).append("),");
        cql.append("(target:").append(graph).append(") ");
        cql.append("WHERE source.key").append(" = $source");
        cql.append(" AND target.key").append(" = $target");
        cql.append(" CREATE (source)-[relation:").append(edge.getValue("type")).append(" ");
        /*
         * Data information of current relation
         */
        final List<String> kv = new ArrayList<>();
        edge.fieldNames().forEach(key -> kv.add(key + ":$" + key));
        cql.append("{").append(Ut.fromJoin(kv, ",")).append("}");
        cql.append("]->(target)");
        cql.append(" RETURN relation");
        return cql.toString();
    }

    static List<String> find(final String graph, final JsonArray edge) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(edge).map(item -> find(graph, item)).forEach(commands::add);
        return commands;
    }

    private static String findCond(final String graph, final JsonObject edge) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MATCH (source:").append(graph).append(" { key:$source })-");
        cql.append("[ r:").append(edge.getValue("type")).append(" ]->");
        cql.append("(target:").append(graph).append(" { key:$target })");
        return cql.toString();
    }

    static String find(final String graph, final JsonObject edge) {
        return findCond(graph, edge) + " RETURN r";
    }


    static List<String> delete(final String graph, final JsonArray edges) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(edges).map(item -> delete(graph, item)).forEach(commands::add);
        return commands;
    }

    static String delete(final String graph, final JsonObject edge) {
        return findCond(graph, edge) + " DELETE r";
    }
}
