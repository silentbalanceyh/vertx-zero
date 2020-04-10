package io.vertx.up.annotations;

import io.vertx.up.eon.Constants;
import io.vertx.up.eon.em.MessageModel;

import java.lang.annotation.*;

/**
 * Worker will consume event bus data by address
 * 1.Agent -- 2.Endpoint -- 3.( Jsr311 ) -- 4.Address Method ( To )
 * Finally the data should be passed into EventBus
 * 1.EventBus -- 2.Worker -- 3.Method -- 4.Address Method ( From )
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Worker {
    /**
     * Event but interaction mode definition
     *
     * @return One of three supported message model that belong to vertx.
     */
    MessageModel value() default MessageModel.REQUEST_RESPONSE;

    /**
     * Worker Instance Number
     * Default: 32
     *
     * @return default instance number
     */
    int instances() default Constants.DEFAULT_INSTANCES;

    /**
     * Isolation Group
     * Default: __VERTX_ZERO__
     *
     * @return default vert.x group
     */
    String group() default Constants.DEFAULT_GROUP;

    /**
     * @return whether support HA feature for current worker.
     */
    boolean ha() default Constants.DEFAULT_HA;
}
