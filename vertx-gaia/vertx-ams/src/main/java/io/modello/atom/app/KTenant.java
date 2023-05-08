package io.modello.atom.app;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTenant implements Serializable {
    private final String id;

    public KTenant(final String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }
}
