package io.vertx.up.specification.action;

import io.horizon.specification.typed.TJson;
import io.modello.specification.atom.HRule;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.em.EmTraffic;

/*
 * Underway communication channel between
 * 1) API - Service
 * 2) Task - Service
 *
 * Instead the new version support two mode to connect service component
 * 1) Request-Response, from Api to Service
 * 2) Publish-Subscribe, from Task to Service
 */
public interface Commercial extends Application, ServiceDefinition, TJson {
    /*
     * Get channel type of definition ( 1 of 4 )
     * The channel class is fixed in current version, mapped to channel type.
     */
    EmTraffic.Channel channelType();

    /*
     * Get channel class here, it will be initialized by other positions
     */
    Class<?> channelComponent();

    /*
     * Get business component class, it will be initialized by other positions
     */
    Class<?> businessComponent();

    /*
     * Get record class, it will be initialized by other positions
     */
    Class<?> recordComponent();

    /*
     * Get database reference
     */
    Database database();

    /*
     * Get integration reference
     */
    Integration integration();

    /*
     * Get channel RuleUnique
     */
    @Override
    HRule rule();

    /*
     * Static identifier here for usage.
     */
    String identifier();
}

interface Application {
    /*
     * EmApp Id
     */
    String app();
}
