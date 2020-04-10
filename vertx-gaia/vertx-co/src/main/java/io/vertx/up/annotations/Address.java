package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * Event bus annotation
 * If it's annotated on method, it means that this action enabled event bus.
 * Otherwise use sync response directly.
 * Address/Out should be matching
 * Address -> The component will send message to event bus ( EndPoint )
 * Out -> The component will consume message to event bus. ( Worker )
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Address {
    /**
     * Target address
     *
     * @return String address value on EventBus
     */
    String value();
}
