package io.vertx.up.verticle;

import io.horizon.eon.em.container.MessageModel;
import io.reactivex.Observable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.up.annotations.Worker;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.Runner;
import io.vertx.up.uca.micro.discovery.ApiOrigin;
import io.vertx.up.uca.micro.discovery.Origin;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Backend for service discovery
 */
@Worker(value = MessageModel.DISCOVERY_PUBLISH, instances = 2)
public class ZeroApiWorker extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroApiWorker.class);

    private static final Origin ORIGIN = Ut.singleton(ApiOrigin.class);

    private static final ConcurrentMap<String, Record> REGISTRITIONS
        = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> ID_MAP
        = new ConcurrentHashMap<>();
    private static final ConcurrentHashSet<String> REGISTRY = new ConcurrentHashSet<>();

    private static final AtomicBoolean initialized =
        new AtomicBoolean(false);

    @Override
    public void start() {
        final ServiceDiscovery discovery = ServiceDiscovery.create(this.vertx);
        // AtomicBoolean checking
        if (!initialized.getAndSet(true)) {
            // initialized once.
            this.initializeServices(discovery);
        }
        // TODO: Discovery
        /**
         this.vertx.setPeriodic(5000, id -> {
         // Clean ko services ( Ipc & Api )
         Fn.safeJvm(() -> EtcdEraser.create().begin(), LOGGER);
         });**/

        // Scan the services every 3s
        this.vertx.setPeriodic(5000, id -> {
            // Read the latest services
            final ConcurrentMap<String, Record> services = ORIGIN.getRegistryData();
            // Read the down services
            final ConcurrentMap<Flag, Set<String>> resultMap = this.calculateServices(services);

            // Do the modification with thread.
            Fn.safeJvm(() -> {
                final CountDownLatch counter = new CountDownLatch(3);
                final Set<String> deleted = resultMap.get(Flag.DELETE);
                final Set<String> updated = resultMap.get(Flag.UPDATE);
                final Set<String> added = resultMap.get(Flag.NEW);
                Runner.run(() -> this.discoveryDeleted(counter, discovery, deleted), "discovery-deleted");
                Runner.run(() -> this.discoveryUpdate(counter, discovery, updated), "discovery-updated");
                Runner.run(() -> this.discoveryAdded(counter, discovery, added, services), "discovery-added");
                // Wait for result
                counter.await();
                LOGGER.info(Info.REG_REFRESHED, added.size(), updated.size(), deleted.size());
            }, LOGGER);
        });
    }

    private void discoveryUpdate(final CountDownLatch counter,
                                 final ServiceDiscovery discovery,
                                 final Set<String> updated) {
        this.updateService(discovery, updated);
        counter.countDown();
    }

    private void discoveryDeleted(final CountDownLatch counter,
                                  final ServiceDiscovery discovery,
                                  final Set<String> keys) {
        this.deleteService(discovery, keys);
        counter.countDown();
    }

    private void discoveryAdded(final CountDownLatch counter,
                                final ServiceDiscovery discovery,
                                final Set<String> added,
                                final ConcurrentMap<String, Record> services) {
        this.addService(discovery, added, services);
        counter.countDown();
    }

    private void deleteService(final ServiceDiscovery discovery,
                               final Set<String> ids) {
        // Delete service from current zero system.
        Observable.fromIterable(ids)
            .subscribe(id -> {
                final String item = ID_MAP.get(id);
                discovery.unpublish(item, result -> {
                    if (result.succeeded()) {
                        // Delete successfully
                        final Record record = REGISTRITIONS.get(id);
                        this.successLog(record);
                        // Sync deleted
                        REGISTRITIONS.remove(id);
                        ID_MAP.remove(id);
                        // Remove from Set
                        REGISTRY.remove(id);
                    } else {
                        LOGGER.info(Info.REG_FAILURE, result.cause().getMessage(), "Delete");
                    }
                });
            })
            .dispose();
    }

    private void updateService(final ServiceDiscovery discovery,
                               final Set<String> ids) {
        // Update service into current zero system.
        Observable.fromIterable(ids)
            .map(REGISTRITIONS::get)
            .subscribe(item -> discovery.update(item, result -> {
                if (result.succeeded()) {
                    final Record record = result.result();
                    // Update successfully
                    this.successFinished(record);
                } else {
                    LOGGER.info(Info.REG_FAILURE, result.cause().getMessage(), "Update");
                }
            }))
            .dispose();
    }

    private void addService(final ServiceDiscovery discovery,
                            final Set<String> ids,
                            final ConcurrentMap<String, Record> services) {
        // Add service into current zero system.
        Observable.fromIterable(ids)
            .map(services::get)
            .subscribe(item -> this.publishSerivce(discovery, "Add").accept(item))
            .dispose();
    }

    private Consumer<Record> publishSerivce(final ServiceDiscovery discovery, final String flag) {
        return (item) -> {
            // Avoid duplicated add
            if (null == item.getRegistration()
                || !REGISTRY.contains(item.getRegistration())) {
                discovery.publish(item, result -> {
                    if (result.succeeded()) {
                        final Record record = result.result();
                        // Add successfully
                        this.successFinished(record);
                        // Add to Sets
                        REGISTRY.add(item.getRegistration());
                    } else {
                        LOGGER.info(Info.REG_FAILURE, result.cause().getMessage(), flag);
                    }
                });
            }
        };
    }


    private void initializeServices(final ServiceDiscovery discovery) {
        // Read the services
        final Set<Record> services = new HashSet<>(ORIGIN.getRegistryData().values());
        Observable.fromIterable(services)
            .subscribe(item -> this.publishSerivce(discovery, "Init").accept(item))
            .dispose();
    }

    private ConcurrentMap<Flag, Set<String>> calculateServices(
        final ConcurrentMap<String, Record> services) {
        // Read new services.
        final Set<String> populated = new HashSet<>();
        Observable.fromIterable(services.keySet())
            .subscribe(populated::add)
            .dispose();

        // Existed = Yes, Populated = No
        final Set<String> deleted = new HashSet<>(REGISTRITIONS.keySet());
        deleted.removeAll(populated);

        // Existed = Yes, Populated = Yes
        final Set<String> updated = new HashSet<>(REGISTRITIONS.keySet());
        updated.retainAll(populated);

        // Existed = No, Populated = Yes
        final Set<String> added = new HashSet<>(populated);
        added.removeAll(REGISTRITIONS.keySet());

        // Mapping data
        final ConcurrentMap<Flag, Set<String>> result = new ConcurrentHashMap<>();
        result.put(Flag.DELETE, deleted);
        result.put(Flag.NEW, added);
        result.put(Flag.UPDATE, updated);
        return result;
    }

    private void successFinished(final Record record) {
        // Build key
        final String key = this.getID(record);
        final String id = record.getRegistration();
        this.successLog(record);
        // Fill container
        REGISTRITIONS.put(key, record);
        ID_MAP.put(key, id);
    }

    private void successLog(final Record record) {
        final String key = this.getID(record);
        final String id = record.getRegistration();
        final String endpoint = MessageFormat.format("http://{0}:{1}{2}",
            record.getLocation().getString(Origin.HOST),
            String.valueOf(record.getLocation().getInteger(Origin.PORT)),
            record.getMetadata().getString(Origin.PATH));
        LOGGER.debug(Info.REG_SUCCESS, record.getStatus(),
            record.getType(), record.getName(),
            endpoint, key, id);
    }

    private String getID(final Record record) {
        final JsonObject metadata = record.getMetadata();
        return metadata.getString(Origin.ID);
    }

    private enum Flag {
        NEW,
        UPDATE,
        DELETE
    }
}
