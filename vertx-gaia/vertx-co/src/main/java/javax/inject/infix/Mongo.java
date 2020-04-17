package javax.inject.infix;

import java.lang.annotation.*;

/**
 * # 「Co」Zero Infix Annotation
 *
 * > Vert.x native client
 *
 * This annotation is for Mongo database in zero, it provide extension to access
 * Mongo database and you can get mongo client reference with this annotation. The infix usage
 * is as following ( `vertx-ifx/zero-ifx-mongo` module ):
 *
 * ```java
 * // <pre><code>
 *
 * .@Plugin
 * .@SuppressWarnings("unchecked")
 * public class MongoInfix implements Infix {
 *
 *     private static final String NAME = "ZERO_MONGO_POOL";
 *
 *     // Mongo Infix
 * }
 * // </code></pre>
 * ```
 *
 * Here you can provide two mode to get `io.vertx.ext.mongo.MongoClient`
 *
 * ```java
 * // <pre><code>
 *
 *      // Uniform injection
 *      .@Plugin
 *      private transient MongoClient client;
 *
 *      // Specific injection
 *      .@Mongo
 *      private transient MongoClient client;
 *
 * // </code></pre>
 *
 * It could be used in any `Agent/Worker` instance directly.
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Mongo {
}
