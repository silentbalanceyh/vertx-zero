package io.vertx.up.uca.job.store;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;

public class JobPin {

    private static final Annal LOGGER = Annal.get(JobPin.class);
    private static JobConfig CONFIG;
    private static JobStore STORE;

    static {
        if (ZeroStore.is(YmlCore.job.__KEY)) {
            CONFIG = ZeroStore.option(YmlCore.job.__KEY, JobConfig.class, JobConfig::new);
            //            final JsonObject job = config.getJsonObject(YmlCore.job.__KEY);
            //            if (!Ut.isNil(job)) {
            //                /* Extension job-store */
            //                CONFIG = Ut.deserialize(job, JobConfig.class);
            //            } else {
            //                CONFIG = new JobConfig();
            //            }
            LOGGER.info(VMessage.Job.PIN.PIN_CONFIG, CONFIG);
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
