package io.modello.emf.atom;

import io.modello.specification.element.HNs;

/**
 * @author lang : 2023-05-12
 */
public class KXml implements HNs {
    protected final String name;
    protected final String uri;
    protected final String prefix;

    private KXml(final String name, final String uri, final String prefix) {
        this.name = name;
        this.uri = uri;
        this.prefix = prefix;
    }

    public static KXml of(final String name, final String uri, final String prefix) {
        return new KXml(name, uri, prefix);
    }

    public String qName() {
        return this.prefix + ":" + this.name + " " + this.uri;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String nsUri() {
        return this.uri;
    }

    @Override
    public String nsPrefix() {
        return this.prefix;
    }
}
