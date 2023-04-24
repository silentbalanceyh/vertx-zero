package io.horizon.specification.zero.action;

import io.aeon.experiment.rule.RuleUnique;
import io.horizon.specification.zero.object.TJson;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.em.ChannelType;

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
    ChannelType channelType();

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
    RuleUnique rule();

    /*
     * Static identifier here for usage.
     */
    String identifier();
}

interface Application {
    /*
     * Application Id
     */
    String app();
}
