package io.horizon.uca.qr;

import io.horizon.eon.VString;
import io.horizon.fn.HFn;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Sorter implements Serializable {
    /**
     * Field
     */
    private final List<String> field = new ArrayList<>();
    /**
     * Sort Mode
     */
    private final List<Boolean> asc = new ArrayList<>();

    private Sorter(final String field,
                   final Boolean asc) {
        HFn.runAt(() -> {
            this.field.add(field);
            this.asc.add(asc);
        }, field);
    }

    public static Sorter create(final String field,
                                final Boolean asc) {
        return new Sorter(field, asc);
    }

    public static Sorter create() {
        return new Sorter(null, false);
    }

    public static Sorter create(final JsonArray sorter) {
        // Sorter Parsing
        final Sorter target = Sorter.create();
        HUt.itJArray(sorter, String.class).forEach((field) -> {
            if (field.contains(VString.COMMA)) {
                final String sortField = field.split(VString.COMMA)[0];
                final boolean asc = field.split(VString.COMMA)[1].equalsIgnoreCase("asc");
                target.add(sortField, asc);
            } else {
                target.add(field, true);
            }
        });
        return target;
    }

    public static Sorter create(final JsonObject sorterJ) {
        if (HUt.isNil(sorterJ)) {
            return null;
        }
        final Sorter sorter = create();
        HUt.itJObject(sorterJ, Boolean.class).forEach((entry) ->
            sorter.add(entry.getKey(), entry.getValue()));
        //        HUt.<Boolean>itJObject(sorterJ, (mode, field) ->
        //            sorter.add(field, mode));
        return sorter;
    }

    public <T> JsonObject toJson(final Function<Boolean, T> function) {
        final JsonObject sort = new JsonObject();
        IntStream.range(0, this.field.size())
            .mapToObj(i -> new AbstractMap.SimpleEntry<Integer, String>(i, this.field.get(i)))
            .forEach(entry -> {
                // index / field
                final int index = entry.getKey();
                final String field = entry.getValue();
                // process
                final boolean mode = this.asc.get(index);
                final T result = function.apply(mode);
                sort.put(field, result);
            });
        return sort;
    }

    public JsonObject toJson() {
        return this.toJson(mode -> mode);
        //        final JsonObject sort = new JsonObject();
        //        HUt.itList(this.field, (item, index) -> {
        //            final boolean mode = this.asc.get(index);
        //            sort.put(item, mode);
        //        });
        //        return sort;
    }

    public Sorter add(final String field,
                      final Boolean asc) {
        this.field.add(field);
        this.asc.add(asc);
        return this;
    }

    public Sorter clear() {
        this.field.clear();
        this.asc.clear();
        return this;
    }

    public boolean valid() {
        return !this.field.isEmpty() && !this.asc.isEmpty();
    }

    @Override
    public String toString() {
        final List<String> kv = new ArrayList<>();
        for (int idx = 0; idx < this.field.size(); idx++) {
            kv.add(VString.LEFT_BRACKET + this.field.get(idx) +
                VString.COMMA + this.asc.get(idx) + VString.RIGHT_BRACKET);
        }
        return VString.LEFT_SQUARE + HUt.fromJoin(kv) + VString.RIGHT_SQUARE;
    }
}
