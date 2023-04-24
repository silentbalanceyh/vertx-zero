package io.vertx.aeon.uca.alive;

import io.aeon.atom.iras.HRepo;
import io.aeon.eon.HName;
import io.aeon.eon.HPath;
import io.aeon.eon.em.RTEAeon;
import io.aeon.specification.app.HFS;
import io.aeon.specification.program.HNova;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.eon.Constants;
import io.vertx.up.runtime.env.Macrocosm;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractNova implements HNova {
    protected Vertx vertx;

    @Override
    @SuppressWarnings("unchecked")
    public HNova bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    protected Future<Boolean> normalize(final ConcurrentMap<RTEAeon, HRepo> repoMap) {
        final HRepo kzeroP = repoMap.get(RTEAeon.kzero);
        final HRepo kiddP = repoMap.get(RTEAeon.kidd);
        final HRepo kinectP = repoMap.get(RTEAeon.kinect);

        final HFS fs = HFS.common();
        final String language = Ut.envWith(Macrocosm.Z_LANG, Constants.DEFAULT_LANGUAGE);
        // kzero -> kinect:  /kzero 配置拷贝
        final String zeroS = Ut.ioPath(kzeroP.inWS(), MessageFormat.format(HPath.SOURCE_ZERO, language));
        final String zeroT = Ut.ioPath(kinectP.getPath(), HName.KZERO);
        fs.cp(zeroS, zeroT);

        // kidd -> kinect:   /kidd  配置拷贝
        final String kiddS = Ut.ioPath(kiddP.inWS(), HName.KIDD);
        final String kiddT = Ut.ioPath(kinectP.getPath(), HName.KIDD);
        fs.cp(kiddS, kiddT);

        return Ux.futureT();
    }
}
