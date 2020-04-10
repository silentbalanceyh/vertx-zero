package io.vertx.up.commune.config;

import java.io.Serializable;
import java.util.Objects;

/*
 * Identifier structure for identifier
 * 1) static identifier: the definition of direct
 * 2) dynamic identifier: the identifier came from identifierComponent
 */
public class Identity implements Serializable {
    private transient String identifier;
    private transient String sigma;
    private transient Class<?> identifierComponent;

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public Class<?> getIdentifierComponent() {
        return this.identifierComponent;
    }

    public void setIdentifierComponent(final Class<?> identifierComponent) {
        this.identifierComponent = identifierComponent;
    }

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Identity)) return false;
        final Identity identity = (Identity) o;
        return this.identifier.equals(identity.identifier) &&
                this.sigma.equals(identity.sigma) &&
                this.identifierComponent.equals(identity.identifierComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier, this.sigma, this.identifierComponent);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "identifier='" + this.identifier + '\'' +
                ", sigma='" + this.sigma + '\'' +
                ", identifierComponent=" + this.identifierComponent +
                '}';
    }
}
