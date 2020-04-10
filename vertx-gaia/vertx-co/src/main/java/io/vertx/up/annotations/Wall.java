package io.vertx.up.annotations;

import io.vertx.up.eon.Values;

import java.lang.annotation.*;

/**
 * Secure to provide wall to limit request
 * 1. 401 Response
 * 2. 403 Response
 * It's for security requirement.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Wall {
    /**
     * Default value for all request limitation.
     *
     * @return the path that will be in security filter.
     */
    String path() default "/*";

    /**
     * Required for this wall, it provide following component building
     * 1. Handler building
     * 2. Provider building
     * It's mapped to node "secure" configuration, this node could configure following information
     * 1. type: Standard type could identify Handler/Provider
     * 2. provider: ( Standard Skip )
     * 3. handler: ( Standard Skip )
     * 4. user: ( Required )
     * 5. config: JsonObject for provider
     *
     * @return The required value for current building.
     */
    String value();

    /**
     * Value for wall sequence, it's for auth handler chain.
     * 1. All the wall class must contains different value
     * 2. The major wall should be 0, others could invoke be 1, 2, 3.
     * 3. The wall handler sequence should be triggered by 0,1,2,3...
     * Multi handler mode needed for this value.
     *
     * @return handler order value that will be built into security chain.
     */
    int order() default Values.ZERO;

    /**
     * Whether current authorization mode is user-defined
     * 1. The default value is false, it means that you must implement AuthHandler method annotated with @Authenticate
     * 2. If default value is true, you must implement tha phase method as following:
     * Annotated with @Phase
     * Phase(HEADER): JsonObject parse(MultiMap headers, JsonObject body)
     * Phase(AUTHORIZE): void authorize(JsonObject, AsyncHandler<User>)
     * Phase(ACCESS): User isAuthorized(String, Handler<AsyncResult<Boolean>> resultHandler)
     *
     * @return The phase that occurs in security limitation.
     */
    boolean define() default false;
}
