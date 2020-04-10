package io.vertx.up.runtime.pkg;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/*
 * Get all package data in current environment
 * filter by `up/config/package-filter.json` configuration inner
 */
@SuppressWarnings("unchecked")
public class PackHunter {
    private static final Set<String> FILTERS = new TreeSet<>();
    private static final Annal LOGGER = Annal.get(PackHunter.class);

    static {
        /*
         * Read configuration to fill FILTERS;
         */
        final JsonObject filter = Ut.ioJObject(Values.CONFIG_INTERNAL_PACKAGE);
        if (filter.containsKey("skip")) {
            final JsonArray skiped = filter.getJsonArray("skip");
            if (Objects.nonNull(skiped)) {
                LOGGER.info(Info.IGNORES, skiped.encode());
                FILTERS.addAll(skiped.getList());
            }
        }
    }

    /*
     * Scanned target package here
     * Verified between
     * 1) development environment
     * 2) production environment
     */
    public static Set<String> getPackages() {
        final Set<String> packageDirs = new TreeSet<>();
        final Package[] packages = Package.getPackages();
        for (final Package pkg : packages) {
            final String pending = pkg.getName();
            final boolean skip = FILTERS.stream().anyMatch(pending::startsWith);
            if (!skip) {
                packageDirs.add(pending);
            }
        }
        LOGGER.info(Info.PACKAGES, String.valueOf(packageDirs.size()), String.valueOf(packages.length));
        return packageDirs;
    }
}
