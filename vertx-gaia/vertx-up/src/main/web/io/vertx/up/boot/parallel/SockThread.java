package io.vertx.up.boot.parallel;

import io.horizon.uca.log.Annal;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.SockExtractor;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockThread extends Thread {
    private static final Annal LOGGER = Annal.get(SockThread.class);
    private final Set<Remind> reminds = new HashSet<>();


    private final transient Extractor<Set<Remind>> extractor =
        Ut.instance(SockExtractor.class);

    private final transient Class<?> reference;

    public SockThread(final Class<?> clazz) {
        this.setName("zero-web-socket-scanner-" + this.getId());
        this.reference = clazz;
    }


    @Override
    public void run() {
        if (null != this.reference) {
            this.reminds.addAll(this.extractor.extract(this.reference));
            LOGGER.info(INFO.SCANNED_SOCKS, this.reference.getName(),
                this.reminds.size());
        }
    }

    public Set<Remind> getEvents() {
        return this.reminds;
    }
}
