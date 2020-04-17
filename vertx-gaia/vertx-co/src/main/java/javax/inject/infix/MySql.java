package javax.inject.infix;

import java.lang.annotation.*;

/**
 * # 「Co」Zero Infix Annotation
 *
 * > Vert.x native client
 *
 * This annotation is for MySql database in zero with jdbc ( MySql Async Client ) instead of
 * Jooq, it provide extension to access MySql database and you can get MySql client reference
 * with this annotation. The infix usage is as following ( `vertx-ifx/zero-ifx-native` module ):
 *
 * ```java
 * // <pre><code>
 *
 * .@Plugin
 * .@SuppressWarnings("unchecked")
 * public class MySqlInfix implements Infix {
 *      // Content of MySql infix
 * }
 * // </code></pre>
 * ```
 *
 * Here you can provide two mode to get `io.vertx.ext.sql.SQLClient`
 *
 * ```java
 * // <pre><code>
 *
 *      // Uniform injection
 *      .@Plugin
 *      private transient SQLClient client;
 *
 *      // Specific injection
 *      .@MySql
 *      private transient SQLClient client;
 *
 * // </code></pre>
 * ```
 *
 * It could be used in any `Agent/Worker` instance directly.
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MySql {
}
