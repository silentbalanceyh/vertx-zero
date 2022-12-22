package jakarta.ws.rs;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointParam {
    /*
     * Point parameters
     * Here are dim configuration
     *
     * 2 - [x,y]
     * 3 - [x,y,z]
     * 4 - [x,y,z,j]
     * */
    String value();
}
