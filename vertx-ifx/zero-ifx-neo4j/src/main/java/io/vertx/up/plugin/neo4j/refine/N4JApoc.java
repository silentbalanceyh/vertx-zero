package io.vertx.up.plugin.neo4j.refine;

@SuppressWarnings("all")
class N4JApoc {
    /*
     *
     */
    static String graphic(final String graph, final Integer level) {
        final StringBuilder cql = new StringBuilder();
        cql.append("MATCH (start:").append(graph).append(" {key:$key}) ");
        cql.append("CALL apoc.path.subgraphAll(start, {maxLevel:");
        cql.append(String.valueOf(level));
        cql.append("}) YIELD relationships ");
        cql.append("UNWIND relationships as edge ");
        cql.append("RETURN startNode(edge) as from, edge, endNode(edge) as to");
        return cql.toString();
    }

    /*
     * Graphic Remove
     */
    static String graphicReset(final String graph) {
        final StringBuffer cql = new StringBuffer();
        cql.append("MATCH (n:").append(graph).append(") ");
        cql.append("OPTIONAL MATCH (n)-[r]-() DELETE n,r");
        return cql.toString();
    }
}
