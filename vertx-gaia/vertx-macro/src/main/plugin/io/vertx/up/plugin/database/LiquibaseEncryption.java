package io.vertx.up.plugin.database;

import io.horizon.runtime.Macrocosm;
import io.horizon.uca.log.Annal;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Properties;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class LiquibaseEncryption extends Properties {
    private static final Annal LOGGER = Annal.get(LiquibaseEncryption.class);

    public LiquibaseEncryption() {
        super.defaults = new Properties();
    }

    @Override
    public synchronized Object put(final Object paramK, final Object paramV) {
        final Boolean enabled = Ut.envWith(Macrocosm.HED_ENABLED, false, Boolean.class);
        LOGGER.info("[ HED ] Encrypt of HED enabled: {0}", enabled);
        if (KName.PASSWORD.equals(paramK) && enabled) {
            // HED_ENABLED=true
            final String decryptPassword = Ut.decryptRSAV(paramV.toString());
            return super.defaults.put(paramK, decryptPassword);
        } else {
            return super.defaults.put(paramK, paramV);
        }
    }
}
