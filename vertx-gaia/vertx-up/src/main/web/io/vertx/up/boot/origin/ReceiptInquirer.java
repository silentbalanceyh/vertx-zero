package io.vertx.up.boot.origin;

import io.horizon.uca.log.Annal;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.boot.parallel.QueueThread;
import io.vertx.up.fn.Fn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Receipt annotation inquirer
 * This thread is for Receipt extraction
 */
public class ReceiptInquirer implements Inquirer<Set<Receipt>> {

    private static final Annal LOGGER = Annal.get(ReceiptInquirer.class);

    @Override
    public Set<Receipt> scan(final Set<Class<?>> queues) {
        final List<QueueThread> threadReference = new ArrayList<>();
        /* 3.1. Build Metadata **/
        for (final Class<?> queue : queues) {
            final QueueThread thread =
                new QueueThread(queue);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.jvmAt(() -> {
            for (final QueueThread item : threadReference) {
                item.join();
            }
        }, LOGGER);
        /* 3.3. Return **/
        final Set<Receipt> receipts = new HashSet<>();
        Fn.jvmAt(() -> threadReference.stream()
            .map(QueueThread::getReceipts)
            .forEach(receipts::addAll), LOGGER);
        /* 3.4. New Receipts replaced with Aeon System ( Enabled ) */
        return receipts;
    }
}
