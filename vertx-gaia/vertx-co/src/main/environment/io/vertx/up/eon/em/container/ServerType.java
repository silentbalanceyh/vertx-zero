package io.vertx.up.eon.em.container;

public enum ServerType {
    // Http Server
    HTTP("http"),
    // Sock Server
    SOCK("sock"),
    // Rx Server
    RX("rx"),
    // Rpc Server
    IPC("ipc"),
    // Api Gateway
    API("api");

    private transient final String literal;

    ServerType(final String literal) {
        this.literal = literal;
    }

    public String key() {
        return this.literal;
    }

    public boolean match(final String literal) {
        return this.literal.equals(literal);
    }
}
