package io.vertx.aeon.uca.alive;

import io.aeon.eon.HName;
import io.aeon.eon.HPath;
import io.horizon.runtime.Macrocosm;
import io.horizon.specification.storage.HFS;
import io.macrocosm.atom.boot.KRepo;
import io.macrocosm.eon.em.EmCloud;
import io.macrocosm.specification.program.HNova;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.eon.KWeb;
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

    protected Future<Boolean> normalize(final ConcurrentMap<EmCloud.Runtime, KRepo> repoMap) {
        final KRepo kzeroP = repoMap.get(EmCloud.Runtime.kzero);
        final KRepo kiddP = repoMap.get(EmCloud.Runtime.kidd);
        final KRepo kinectP = repoMap.get(EmCloud.Runtime.kinect);

        final HFS fs = HFS.common();
        final String language = Ut.envWith(Macrocosm.Z_LANG, KWeb.ARGS.V_LANGUAGE);
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
