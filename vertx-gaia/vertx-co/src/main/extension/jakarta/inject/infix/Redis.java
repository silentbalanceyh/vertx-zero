package jakarta.inject.infix;

import java.lang.annotation.*;

/**
 * # 「Co」Zero Infusion Annotation
 *
 * > Vert.x native client
 *
 * This annotation is for Redis in zero framework, it provide extension to access Redis
 * database and you can get Redis client reference with this annotation. The infix usage
 * is as following ( `vertx-ifx/zero-ifx-native` module ):
 *
 * ```java
 * // <pre><code>
 *
 * .@Infusion
 * .@SuppressWarnings("unchecked")
 * public class RedisInfix implements Infusion {
 *      // Content here
 * }
 *
 * // </code></pre>
 * ```
 *
 * This infix will be upgraded in future because of Redis API modified in `vert.x 3.9.0`,
 * The new class `io.vertx.redis.client.Redis` will replace the old client
 * `io.vertx.redis.RedisClient`, I'll release this annotation after `0.5.3` with the latest
 * redis api here.
 *
 * When you use zero version `< 0.5.2`, you can still use `io.vertx.redis.RedisClient` to
 * access Redis.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Redis {
}
