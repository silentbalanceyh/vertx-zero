package jakarta.ws.rs;

import java.lang.annotation.*;

/**
 * Rpc parameter, getNull data from remote
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcParam {
}
