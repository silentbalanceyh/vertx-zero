package io.vertx.up.eon.em;

public enum ServerType {
    // Http Server
    HTTP("http"),
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
