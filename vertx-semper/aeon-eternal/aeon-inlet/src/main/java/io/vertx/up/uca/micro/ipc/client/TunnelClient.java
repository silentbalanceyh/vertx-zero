package io.vertx.up.uca.micro.ipc.client;

import io.horizon.eon.em.container.IpcType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.servicediscovery.Record;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.atom.agent.IpcData;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._501RpcAddressWrongException;
import io.vertx.up.exception.web._501RpcImplementException;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import io.vertx.up.uca.micro.discovery.IpcOrigin;
import io.vertx.up.uca.micro.discovery.Origin;
import io.vertx.up.uca.micro.ipc.DataEncap;
import io.vertx.up.uca.registry.UddiClient;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Rpc client, scanned etcd to getNull configuration.
 */
public class TunnelClient implements UddiClient {

    private static final Origin ORIGIN = Ut.singleton(IpcOrigin.class);
    private static final ConcurrentMap<IpcType, Spear> STUBS =
        new ConcurrentHashMap<IpcType, Spear>() {
            {
                this.put(IpcType.UNITY, Ut.singleton(UnitySpear.class));
                // put(IpcType.CONSUME, Ut.singleton(ConsumeStub.class));
                // put(IpcType.DUPLIEX, Ut.singleton(DupliexStub.class));
                // put(IpcType.PRODUCE, Ut.singleton(ProduceStub.class));
            }
        };
    private final transient Annal logger;
    private transient Vertx vertx;
    private transient Method event;

    private TunnelClient(final Class<?> clazz) {
        this.logger = Annal.get(clazz);
    }

    public static TunnelClient create(final Class<?> clazz) {
        return new TunnelClient(clazz);
    }

    @Override
    public UddiClient bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    @Override
    public UddiClient bind(final Method event) {
        this.event = event;
        return this;
    }

    @Override
    public Future<Envelop> connect(final Envelop envelop) {
        // 1. Extract address
        final String address = this.getValue("to");
        final IpcType type = this.getValue("type");
        // 2. Record extract
        final Record record = this.findTarget();
        // 3. Convert IpcData
        final IpcData data = new IpcData();
        data.setType(type);
        data.setAddress(address);
        // 4. In data
        DataEncap.in(data, record);
        DataEncap.in(data, envelop);
        // 5. Stub
        final Spear stub = STUBS.getOrDefault(type, Ut.singleton(UnitySpear.class));
        return stub.send(this.vertx, data);
    }

    private <T> T getValue(final String attr) {
        final Annotation annotation = this.event.getAnnotation(Ipc.class);
        return Ut.invoke(annotation, attr);
    }

    /**
     * Here's the logical of current IPC
     * 1. The address contains all the etcd address that published
     *
     * @return Found record for IPC
     */
    @SuppressWarnings("all")
    private Record findTarget() {
        final ConcurrentMap<String, Record> address = ORIGIN.getRegistryData();
        final String target = this.getValue("to");
        final String name = this.getValue("name");
        // 1. Find service names
        final List<Record> records = this.findRecords();
        final Record record = records.stream().filter(item ->
                target.equals(item.getMetadata().getString("path")))
            .findAny().orElse(null);
        // Service Name
        Fn.outWeb(null == record, this.logger,
            _501RpcImplementException.class, this.getClass(),
            name, target, this.event);
        // Address Wrong
        Fn.outWeb(null == record.getMetadata() ||
                !target.equals(record.getMetadata().getString("path")), this.logger,
            _501RpcAddressWrongException.class, this.getClass(),
            target, name);
        this.logger.info(Info.RECORD_FOUND, record.toJson());
        return record;
    }

    private List<Record> findRecords() {
        final ConcurrentMap<String, Record> address = ORIGIN.getRegistryData();
        final String name = this.getValue("name");
        // Find service records
        return address.values().stream()
            .filter(item -> name.equals(item.getName()))
            .collect(Collectors.toList());
    }
}
