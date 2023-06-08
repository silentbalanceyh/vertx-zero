package jakarta.inject.infix;

import java.lang.annotation.*;

/**
 * # 「Co」Zero Infusion Annotation
 *
 * This annotation is for zero infix usage, it's specific annotation when you want to develop
 * Micro Service applications here. In zero framework, `gRpc` is default service communication
 * component, this infix is as following:
 *
 * ```java
 * // <pre><code>
 *
 * .@Infusion
 * .@SuppressWarnings("unchecked")
 * public class RpcInfix implements Infusion {
 *      // Content of gRpc client
 * }
 * // </code></pre>
 * ```
 *
 * In current architecture, gRpc is default component to do service communicating between different
 * micro service module, it will replace HTTP/HTTPS service communication here. Zero framework
 * support `etcd3/k8s` registry server in default, it means that when you want to use Micro Service
 * in zero, there is only one choice in current version here.
 *
 * In this kind of situation, you want use this infix class to process it.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Rpc {

}
