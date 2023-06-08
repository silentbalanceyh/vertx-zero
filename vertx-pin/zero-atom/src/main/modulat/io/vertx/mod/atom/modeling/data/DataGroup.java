package io.vertx.mod.atom.modeling.data;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;

import java.util.Objects;

public class DataGroup {
    private final transient JsonArray data = new JsonArray();
    private final transient DataAtom atom;
    private final transient String identifier;

    private DataGroup(final DataAtom atom) {
        this.atom = atom;
        this.identifier = atom.identifier();
    }

    public static DataGroup create(final DataAtom atom) {
        return new DataGroup(atom);
    }

    public DataGroup add(final JsonObject data) {
        this.data.add(data);
        return this;
    }

    public DataGroup add(final JsonArray data) {
        this.data.addAll(data);
        return this;
    }

    public DataGroup replace(final JsonArray data) {
        this.data.clear();
        this.data.addAll(data);
        return this;
    }

    public JsonArray data() {
        return this.data;
    }

    public DataAtom atom() {
        return this.atom;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DataGroup)) return false;
        final DataGroup dataGroup = (DataGroup) o;
        return this.identifier.equals(dataGroup.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier);
    }
}
