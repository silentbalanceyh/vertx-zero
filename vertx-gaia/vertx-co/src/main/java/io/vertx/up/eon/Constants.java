package io.vertx.up.eon;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface Constants {

    String DEFAULT_GROUP = "__VERTX_ZERO__";

    String DEFAULT_HOLDER = "HOLDER";

    int DEFAULT_INSTANCES = 32;

    boolean DEFAULT_HA = true;

    String DEFAULT_JOB = "jobs";

    String DEFAULT_JOB_NAMESPACE = "zero.vertx.jobs";

    String DEFAULT_JOOQ_HISTORY = "orbit";

    String DEFAULT_JOOQ = "provider";

    String DEFAULT_FREEDOM = "freedom";

    String DEFAULT_EXPORT = "file-exported";

    String DEFAULT_LANGUAGE = "cn";

    String DEFAULT_HOST = "0.0.0.0";

    String DEFAULT_WORKFLOW_SOURCE = "zero.framework";

    String ALGORITHM_RSA = "RSA";

    String DEFAULT_WEBSOCKET = "/api/ws";

    int DEFAULT_EXPIRED_DATA = 300;
    /**
     * Scanned data to distinguish mode
     * 1) Only Interface Style could have the indexes key such as 0,1,2 consider as data key.
     * 2) The mode impact different flow of Envelop
     */
    ConcurrentMap<Integer, String> INDEXES = new ConcurrentHashMap<Integer, String>() {
        {
            this.put(0, "0");
            this.put(1, "1");
            this.put(2, "2");
            this.put(3, "3");
            this.put(4, "4");
            this.put(5, "5");
            this.put(6, "6");
            this.put(7, "7");
        }
    };

    interface Pool {
        /* Deployment */
        String DEPLOYMENT = "zero.pool.deployment";
        /* Component Memory Hash Map */
        String COMPONENT = "zero.pool.component";
    }
}
