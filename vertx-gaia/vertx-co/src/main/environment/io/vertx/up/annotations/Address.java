package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * = 「Zero Annotation」
 *
 * This annotation is for EventBus address description, in vert.x the event bus address is typed String instead of
 * complex data structure. Current annotation could be used on method only, it means that current action has enabled
 * EventBus, support async operations:
 *
 * - Return type is `Future`.
 * - The parameters could be Message / Handler.
 * - You can write the code in callback style / future style both.
 *
 * In zero framework, there are two scenarios that will be use @Address cross EventBus.
 *
 * ## 1. Scenario 1: Standard RESTful Api
 *
 * You can use this annotation in standard component in async mode: Agent/Worker
 *
 * 1. The async Agent should be put in class that has been annotated with @EndPoint, and then the method should be
 * annotated with @Address.
 * 2. The async Worker should be put in class that has been annotated with @Queue, and then the method should be
 * annotated with @Address.
 * 3. Both Agent/Worker must be in pair ( 1:1 ), they are communicated with the same `value()` in @Address.
 *
 * ## 2. Scenario 2: Broker Input
 *
 * You can use this annotation in websocket method that has been annotated with @Broker
 *
 * 1. WebSocket annotation @Broker must not be in class annotated with @Queue.
 * 2. In WebSocket method ( annotated by @Broker ), the @Address means the input of current method came from @Address
 * position and it also support async operations.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Address {
    /**
     * EventBus address that communicated between Agent/Worker.
     *
     * @return String address value on EventBus
     */
    String value();
}
