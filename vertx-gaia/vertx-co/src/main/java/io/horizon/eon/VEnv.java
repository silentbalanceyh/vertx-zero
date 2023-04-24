package io.horizon.eon;

/**
 * @author lang : 2023/4/24
 */
public interface VEnv {

    interface APP {
        // Modeler Namespace of DEFAULT
        String NS = "cn.originx.{0}";
        // Job Namespace
        String NS_JOB = "zero.vertx.jobs";
    }

    interface PROP {
        String OS_NAME = "os.name";
    }
}
