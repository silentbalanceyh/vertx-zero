package io.vertx.tp.crud.atom;

import io.vertx.up.util.Ut;

import java.io.Serializable;

public class IxViewParam implements Serializable {

    private transient Boolean dynamic = Boolean.FALSE;
    private transient String identifier;
    private transient String view = "DEFAULT";  // Default name

    public Boolean getDynamic() {
        /* Basic calculation for column analyze mode */
        this.dynamic = Ut.isNil(this.identifier);
        return this.dynamic;
    }

    public void setDynamic(final Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getView() {
        return this.view;
    }

    public void setView(final String view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "IxColumn{" +
                "dynamic=" + this.dynamic +
                ", identifier='" + this.identifier + '\'' +
                ", view='" + this.view + '\'' +
                '}';
    }

}
