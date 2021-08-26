package io.vertx.up.uca.web.thread;

import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.uca.rs.config.ReceiptExtractor;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

public class QueueThread extends Thread {

    private static final Annal LOGGER = Annal.get(QueueThread.class);

    private final Set<Receipt> receipts = new HashSet<>();

    private final transient Extractor<Set<Receipt>> extractor =
        Ut.instance(ReceiptExtractor.class);

    private final transient Class<?> reference;

    public QueueThread(final Class<?> clazz) {
        this.setName("zero-queue-scanner-" + this.getId());
        this.reference = clazz;
    }

    @Override
    public void run() {
        if (null != this.reference) {
            this.receipts.addAll(this.extractor.extract(this.reference));
            LOGGER.info(Info.SCANNED_RECEIPTS, this.reference.getName(),
                this.receipts.size());
        }
    }

    public Set<Receipt> getReceipts() {
        return this.receipts;
    }
}
