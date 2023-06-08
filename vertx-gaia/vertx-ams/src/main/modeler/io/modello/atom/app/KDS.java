package io.modello.atom.app;

import io.horizon.eon.em.EmDS;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author lang : 2023-06-06
 */
public class KDS<T extends KDatabase> implements Function<KDS<T>, KDS<T>> {

    private final ConcurrentMap<EmDS.Stored, T> database = new ConcurrentHashMap<>();
    private final Set<T> databaseSet = new LinkedHashSet<>();

    public T database() {
        return this.database.getOrDefault(EmDS.Stored.PRIMARY, null);
    }

    public T history() {
        return this.database.getOrDefault(EmDS.Stored.HISTORY, null);
    }

    public T workflow() {
        return this.database.getOrDefault(EmDS.Stored.WORKFLOW, null);
    }

    public KDS<T> registry(final EmDS.Stored store, final T database) {
        if (Objects.nonNull(database)) {
            this.database.put(store, database);
            if (EmDS.Stored.DYNAMIC == store) {
                this.databaseSet.add(database);
            }
        }
        return this;
    }

    public KDS<T> registry(final Collection<T> databases) {
        this.databaseSet.clear();
        this.databaseSet.addAll(databases);
        if (this.database.isEmpty()) {
            databases.stream().findFirst()
                .ifPresent(dynamic -> this.database.put(EmDS.Stored.DYNAMIC, dynamic));
        }
        return this;
    }

    public Set<T> dynamicSet() {
        return this.databaseSet;
    }

    public T dynamic() {
        return this.database.getOrDefault(EmDS.Stored.DYNAMIC, null);
    }

    @Override
    public KDS<T> apply(final KDS<T> target) {
        if (Objects.nonNull(target)) {
            this.database.putAll(target.database);
            this.databaseSet.addAll(target.databaseSet);
        }
        return this;
    }
}
