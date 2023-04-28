package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import org.neo4j.driver.Result;
import org.neo4j.driver.Value;

import java.util.List;
import java.util.Set;

public class N4J {

    public static List<String> constraint(final String graph, final Set<String> properties, final String alias) {
        return N4JNode.constraint(graph, properties, alias);
    }

    // ------------ DDL

    public static String nodeAdd(final String graph, final JsonObject node, final String alias) {
        return N4JNode.add(graph, node, alias);
    }

    // ------------ Node

    public static List<String> nodeAdd(final String graph, final JsonArray node, final String alias) {
        return N4JNode.add(graph, node, alias);
    }

    public static String nodeUpdate(final String graph, final JsonObject condition, final JsonObject data, final String alias) {
        return N4JNode.update(graph, condition, data, alias);
    }

    public static List<String> nodeUpdate(final String graph, final JsonArray conditions, final JsonArray data, final String alias) {
        return N4JNode.update(graph, conditions, data, alias);
    }

    public static String nodeDelete(final String graph, final JsonObject condition, final String alias) {
        return N4JNode.delete(graph, condition, alias);
    }

    public static List<String> nodeDelete(final String graph, final JsonArray conditions, final String alias) {
        return N4JNode.delete(graph, conditions, alias);
    }

    public static JsonObject nodeUnique(final JsonObject data) {
        return N4JCond.nodeUnique(data);
    }

    public static JsonArray nodeUnique(final JsonArray data) {
        return N4JCond.nodeUnique(data);
    }

    public static String nodeFind(final String graph, final JsonObject condition, final String alias) {
        return N4JNode.find(graph, condition, alias);
    }

    // ------------ Node
    public static String edgeAdd(final String graph, final JsonObject edge) {
        return N4JEdge.add(graph, edge);
    }

    public static List<String> edgeAdd(final String graph, final JsonArray node) {
        return N4JEdge.add(graph, node);
    }

    public static String edgeDelete(final String graph, final JsonObject edge) {
        return N4JEdge.delete(graph, edge);
    }

    public static List<String> edgeDelete(final String graph, final JsonArray edge) {
        return N4JEdge.delete(graph, edge);
    }

    public static String edgeFind(final String graph, final JsonObject edge) {
        return N4JEdge.find(graph, edge);
    }

    public static List<String> edgeFind(final String graph, final JsonArray edge) {
        return N4JEdge.find(graph, edge);
    }

    // ------------- Sub Graphic
    public static String graphicByKey(final String graph, final Integer level) {
        return N4JApoc.graphic(graph, level);
    }

    public static String graphicReset(final String graph) {
        return N4JApoc.graphicReset(graph);
    }

    public static JsonObject nodeMarker(final JsonObject node) {
        return N4JInput.marker(node);
    }
    // -------------- Tools

    public static JsonArray nodeMarker(final JsonArray node) {
        return N4JInput.marker(node);
    }

    public static Value parameters(final JsonObject input) {
        return N4JInput.parameters(input);
    }

    public static JsonObject toJson(final Result result, final String alias) {
        return N4JOutput.toJson(result, alias);
    }

    public static JsonObject toJson(final Result result) {
        return N4JOutput.toJson(result);
    }

    public static JsonObject toJson(final Value value) {
        return N4JOutput.toJson(value.asMap());
    }

    public static JsonObject graphicDefault() {
        return new JsonObject().put("nodes", new JsonArray()).put("edges", new JsonArray());
    }

    public interface LOG {
        String INFIX = "γραφικό";

        LogModule CQL = Log.modulat(INFIX).infix("CQL");
        LogModule Node = Log.modulat(INFIX).infix("Node");
        LogModule Edge = Log.modulat(INFIX).infix("Edge");
    }

}
