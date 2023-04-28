package io.vertx.up.uca.job.store;

import io.horizon.eon.info.VMessage;
import io.vertx.core.json.JsonObject;
import io.horizon.uca.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

public class JobPin {

    private static final Annal LOGGER = Annal.get(JobPin.class);
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static final String JOB = "job";
    private static JobConfig CONFIG;
    private static JobStore STORE;

    static {
        final JsonObject config = VISITOR.read();
        if (config.containsKey(JOB)) {
            final JsonObject job = config.getJsonObject(JOB);
            if (!Ut.isNil(job)) {
                /* Extension job-store */
                CONFIG = Ut.deserialize(job, JobConfig.class);
            } else {
                CONFIG = new JobConfig();
            }
            LOGGER.info(VMessage.JOB_PIN_CONFIG, CONFIG);
        }
    }

    public static JobConfig getConfig() {
        return CONFIG;
    }

    public static JobStore getStore() {
        /*
         * Singleton for UnityStore ( package scope )
         */
        synchronized (JobStore.class) {
            if (null == STORE) {
                STORE = new UnityStore();
            }
            return STORE;
        }
    }
}
