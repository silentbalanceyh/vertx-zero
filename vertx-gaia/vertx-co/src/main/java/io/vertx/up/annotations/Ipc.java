package io.vertx.up.annotations;

import io.vertx.up.eon.em.container.IpcType;

import java.lang.annotation.*;

/**
 * Internal Rpc Channel
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Ipc {
    /**
     * Communication type ( 4 categories )
     *
     * @return different categories of Ipc
     */
    IpcType type() default IpcType.UNITY;

    /**
     * Default Rpc Service Name
     * 1. name != "" -> Sender
     * 2. name == "" -> Consumer ( Worker )
     *
     * @return identify rpc roles: 3 categories
     */
    String name() default "";

    /**
     * Event Bus address, this address must be used with name(), it means that
     * current Ipc should be send message to
     * 1. Service ( name = xxx, from = xxx )
     *
     * @return Used in originasor and coordinator
     */
    String to() default "";

    /**
     * Event Bus address, this address must be used standalone, it means that
     * current Ipc should read message only and do not send out.
     * 1. Service ( name = current )
     * 2. value is used instead of from direction.
     *
     * @return Default value help to identify roles.
     */
    String value() default "";
}
