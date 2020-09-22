package io.vertx.up.uca.micro.discovery;

import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.Status;
import io.vertx.up.eon.em.EtcdPath;

import java.util.concurrent.ConcurrentMap;

public class IpcOrigin extends ApiOrigin {

    @Override
    public ConcurrentMap<String, Record> getRegistryData() {
        final ConcurrentMap<String, Record> map = this.readData(EtcdPath.IPC);
        for (final Record record : map.values()) {
            record.setStatus(Status.UP);
            record.setType("IPC");
            // Alpn Enabled for Rpc, ssl must be true.
            record.getLocation().put("ssl", Boolean.TRUE);
        }
        return map;
    }

    @Override
    public EtcdPath getPath() {
        return EtcdPath.IPC;
    }
}
