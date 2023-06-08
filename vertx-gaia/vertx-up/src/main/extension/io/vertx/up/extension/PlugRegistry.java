package io.vertx.up.extension;

import java.util.Set;

/**
 * 「Extension」
 * Name: Etcd Registry Extension
 * This plugin is required when you enabled zero-crud extension.
 * When it's used in Micro mode, you should not put the same uri such as:
 * `/api/:actor` to different service here, here must exist parsing workflow.
 */
public interface PlugRegistry {
    /*
     * Process extension for routes.
     * Convert /api/:actor because of CRUD module.
     * This method must modify the routes inner method to get the final result instead
     * of old.
     */
    Set<String> analyze(Set<String> routes);
}
