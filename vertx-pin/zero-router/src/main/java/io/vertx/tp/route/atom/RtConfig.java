package io.vertx.tp.route.atom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/*
 * Configuration for RtAgent
 */
public class RtConfig implements Serializable {
    /*
     * Security routing for router ( agent )
     */
    @JsonProperty("ipc.auth.name")
    private transient String ipcAuth;

    public String getIpcAuth() {
        return ipcAuth;
    }

    public void setIpcAuth(final String ipcAuth) {
        this.ipcAuth = ipcAuth;
    }

    @Override
    public String toString() {
        return "RtConfig{" +
            "ipcAuth='" + ipcAuth + '\'' +
            '}';
    }
}
