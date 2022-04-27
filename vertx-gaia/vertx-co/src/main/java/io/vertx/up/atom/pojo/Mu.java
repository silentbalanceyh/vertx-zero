package io.vertx.up.atom.pojo;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * M/U means modal unit, its format is following:
 *
 * // <pre><code class="yaml">
 * atom:
 *    identifier:                       # Uniform Model identifier
 *    alias:        `value`
 *    trackable:     true/false         # Enable the model tracking
 *    attributes:                       # Attributes Information ( name = alias )
 *        name:     `alias`             # Default Format for fast usage
 *    matrix:
 *        name:     `X,X,X,X,X,X,X,X`   # ACTIVE, TRACK, LOCK, ARRAY, REFER, CONFIRM, IN, OUT ( Be careful of the flag position )
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Mu implements Serializable {
    private ConcurrentMap<String, String> attributes = new ConcurrentHashMap<>();
    private Boolean trackable = Boolean.TRUE;       // Default is True
    private ConcurrentMap<String, String> matrix = new ConcurrentHashMap<>();
    private String identifier;
    private String alias;

    public ConcurrentMap<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(final ConcurrentMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public Boolean getTrackable() {
        return this.trackable;
    }

    public void setTrackable(final Boolean trackable) {
        this.trackable = trackable;
    }

    public ConcurrentMap<String, String> getMatrix() {
        return this.matrix;
    }

    public void setMatrix(final ConcurrentMap<String, String> matrix) {
        this.matrix = matrix;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }
}
