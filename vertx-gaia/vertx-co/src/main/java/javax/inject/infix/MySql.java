package javax.inject.infix;

import java.lang.annotation.*;

/**
 * Extend from javax.inject ( JSR 330 )
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MySql {
}
