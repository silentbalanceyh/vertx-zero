package io.vertx.up.uca.job.store;

import io.vertx.up.atom.worker.Mission;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Bridge for different JobStore
 */
class ExtensionStore implements JobStore {
    private static final JobConfig CONFIG = JobPin.getConfig();
    private transient JobStore reference;
    private transient boolean isExtension;

    ExtensionStore() {
        if (Objects.nonNull(CONFIG)) {
            final Class<?> storeCls = CONFIG.getStore().getComponent();
            Optional.ofNullable(storeCls).ifPresent(clazz -> {
                reference = Ut.instance(clazz);
                if (Objects.nonNull(reference)) {
                    isExtension = true;
                }
            });
        }
    }

    @Override
    public Set<Mission> fetch() {
        return extensionCall(HashSet::new, () -> reference.fetch());
    }

    @Override
    public Mission fetch(final String name) {
        return extensionCall(() -> null, () -> reference.fetch(name));
    }

    @Override
    public JobStore remove(final Mission mission) {
        if (isExtension) {
            reference.remove(mission);
        }
        return this;
    }

    @Override
    public JobStore update(final Mission mission) {
        if (isExtension) {
            reference.update(mission);
        }
        return this;
    }

    @Override
    public JobStore add(final Mission mission) {
        if (isExtension) {
            reference.add(mission);
        }
        return this;
    }

    private <T> T extensionCall(final Supplier<T> defaultSupplier, final Supplier<T> extension) {
        if (isExtension) {
            return extension.get();
        } else {
            return defaultSupplier.get();
        }
    }
}
