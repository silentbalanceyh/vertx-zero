package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * Queue defined the event bus handler root class.
 * Zero system will scan all the classes annotated with @Queue.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Queue {
}
