package io.vertx.up.uca.jooq.cache;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface INFO {
    String AOP_EXIST_ASYNC = "[ Aop ]  `{0}` exist aspecting... ( Async ) {1}";
    String AOP_EXIST_SYNC = "[ Aop ] `{0}` exist aspecting... ( Sync ) {1}";

    String AOP_READ_ASYNC = "[ Aop ] `{0}` read aspecting... ( Async ) {1}";
    String AOP_READ_SYNC = "( Aop ) `{0}` read aspecting... ( Sync ) {1}";

    String AOP_WRITE_ASYNC = "[ Aop ] `{0}` write aspecting... ( Async ) {1}";
    String AOP_WRITE_SYNC = "[ Aop ] `{0}` delete aspecting... ( Sync ) {1}";
}
