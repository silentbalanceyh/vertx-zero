package jakarta.inject.infix;

import java.lang.annotation.*;

/**
 * # 「Co」Zero Infusion Annotation
 *
 *
 * This annotation is for zero infix usage. Zero provide extension system for different
 * infix development. And you can write infix as following ( `vertx-gaia/vertx-tp` module ):
 *
 *
 * ```java
 * // <pre><code>
 *
 * .@Infusion
 * .@SuppressWarnings("unchecked")
 * public class JooqInfix implements Infusion {
 *      // Content of this infix
 * }
 *
 * // </code></pre>
 * ```
 *
 * All the infix must implement `io.vertx.up.plugin.Infusion` interface, the infix will be
 * configured in `vertx-inject.yml` to mount into zero framework. Current annotation
 * is for developer to get `DSLContext` reference.
 *
 * But in zero system, we recommend to use `Ux.Jooq.on(xx)` instead of Jooq injection, it means
 * that you can write program with `Ux.Jooq` instead of `javax.inject.@Inject` annotation here.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Jooq {
}
