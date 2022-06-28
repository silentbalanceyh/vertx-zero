package io.vertx.tp.ke.secure;

import io.vertx.up.eon.KName;
import io.vertx.up.experiment.mixture.HED;
import io.vertx.up.experiment.specification.KPair;
import io.vertx.up.util.Ut;

import java.util.Properties;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroLiquibaseEncryption extends Properties {
    private static final HED ZERO_HED = new HEDExtension();

    public ZeroLiquibaseEncryption() {
        super.defaults = new Properties();
    }

    @Override
    public synchronized Object put(final Object paramK, final Object paramV) {
        if (KName.PASSWORD.equals(paramK)) {
            final KPair pair = ZERO_HED.loadRSA();
            final String decryptPassword = Ut.decryptRSAV(paramV.toString(), pair.getPrivateKey());
            return super.defaults.put(paramK, decryptPassword);
        } else {
            return super.defaults.put(paramK, paramV);
        }
    }
}
