package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.vertx.tp.plugin.neo4j.refine.N4J.LOG;

/*
 * {
 *     "id": "xxx",
 *     "name": "xxxx",
 *     "data": "data"
 * }
 */
class N4JNode {
    /* Add */
    static List<String> add(final String graph, final JsonArray node, final String alias) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(node).map(item -> add(graph, item, alias)).forEach(commands::add);
        if (!commands.isEmpty()) {
            LOG.CQL.info(N4JNode.class, "Add Command: {0}", commands.get(0));
        }
        return commands;
    }

    static String add(final String graph, final JsonObject node, final String alias) {
        final StringBuilder cql = new StringBuilder();
        cql.append("CREATE (").append(alias).append(":").append(graph).append(" ");
        cql.append("{");
        /*
         * Data Part
         */
        final List<String> kv = new ArrayList<>();
        node.fieldNames().forEach(field -> kv.add(field + ":$" + field));
        {
            /*
             * Auditor
             */
            kv.add("createdAt:timestamp()");
            kv.add("updatedAt:timestamp()");
        }
        cql.append(Ut.fromJoin(kv, ","));
        cql.append("})");
        cql.append(" RETURN ").append(alias);
        return cql.toString();
    }

    /* Update */
    static List<String> update(final String graph, final JsonArray conditions, final JsonArray data, final String alias) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(conditions, JsonObject.class, (item, index) ->
            commands.add(update(graph, item, data.getJsonObject(index), alias)));
        if (!commands.isEmpty()) {
            LOG.CQL.info(N4JNode.class, "Update Command: {0}", commands.get(0));
        }
        return commands;
    }

    static String update(final String graph, final JsonObject condition, final JsonObject node, final String alias) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MERGE ").append(N4JCond.graphCondition(graph, condition, alias));
        cql.append("ON MATCH SET ");
        /*
         * Set Part
         */
        final List<String> set = new ArrayList<>();
        Ut.itJObject(node, (value, field) -> set.add(alias + "." + field + "=$" + field));
        {
            /*
             * Auditor
             */
            set.add(alias + ".updatedAt=timestamp()");
        }
        cql.append(Ut.fromJoin(set, ","));
        cql.append(" RETURN ").append(alias);
        return cql.toString();
    }

    /* Delete */
    static List<String> delete(final String graph, final JsonArray conditions, final String alias) {
        final List<String> commands = new ArrayList<>();
        Ut.itJArray(conditions, JsonObject.class, (item, index) -> commands.add(delete(graph, item, alias)));
        if (!commands.isEmpty()) {
            LOG.CQL.info(N4JNode.class, "Delete Command: {0}", commands.get(0));
        }
        return commands;
    }

    @SuppressWarnings("all")
    static String delete(final String graph, final JsonObject condition, final String alias) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MATCH ").append(N4JCond.graphCondition(graph, condition, alias));
        /*
         * Condition Part
         */
        cql.append("DETACH DELETE ").append(alias);
        return cql.toString();
    }

    /* Find / DDL */
    static String find(final String graph, final JsonObject condition, final String alias) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MATCH (").append(alias).append(":").append(graph).append(") ");
        cql.append("WHERE ");

        final List<String> wheres = new ArrayList<>();
        Ut.itJObject(condition, (value, field) -> wheres.add(alias + "." + field + "=$" + field));
        cql.append(Ut.fromJoin(wheres, " AND "));
        cql.append(" RETURN ").append(alias);
        return cql.toString();
    }

    static List<String> constraint(final String graph, final Set<String> properties, final String alias) {
        /*
         * Unique set
         */
        final List<String> kv = new ArrayList<>();
        properties.forEach(property -> kv.add("CREATE CONSTRAINT ON (" + alias + ":" + graph + ") " +
            "ASSERT " + alias + "." + property + " IS UNIQUE"));
        return kv;
    }
}
