package javax.inject.infix;

import java.lang.annotation.*;

/**
 * Standard annotation for Vert.x module to inject instance variable
 * Extend from javax.inject ( JSR 330 )
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Mongo {
}
