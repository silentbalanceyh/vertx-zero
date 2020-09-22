package io.vertx.up.uca.micro.ipc;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.servicediscovery.Record;
import io.vertx.tp.ipc.eon.IpcEnvelop;
import io.vertx.tp.ipc.eon.IpcRequest;
import io.vertx.tp.ipc.eon.IpcResponse;
import io.vertx.tp.ipc.eon.em.Format;
import io.vertx.up.atom.rpc.IpcData;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.em.IpcType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.container.VirtualUser;
import io.vertx.up.util.Ut;

/**
 * Data serialization to set data
 * Envelop -> IpcData -> IpcRequest -> ...
 * IpcResponse -> IpcData -> Envelop
 */
public class DataEncap {

    private static final Annal LOGGER = Annal.get(DataEncap.class);

    public static void in(final IpcData data, final Record record) {
        if (null != record) {
            data.setHost(record.getLocation().getString("host"));
            data.setPort(record.getLocation().getInteger("port"));
            data.setName(record.getName());
        }
    }

    public static void in(final IpcData data, final Envelop envelop) {
        if (null != envelop) {
            // User
            final User user = envelop.user();
            final JsonObject sendData = new JsonObject();
            if (null != user) {
                sendData.put("user", user.principal());
            }
            // Header
            final MultiMap headers = envelop.headers();
            if (null != headers) {
                final JsonObject headerData = new JsonObject();
                headers.forEach((entry) -> headerData.put(entry.getKey(), entry.getValue()));
                sendData.put("header", headerData);
            }
            // Data
            final Object content = envelop.data();
            sendData.put("data", content);
            sendData.put("config", data.getConfig());
            // Data Prepared finished.
            sendData.put("address", data.getAddress());
            data.setData(sendData.toBuffer());
        }
    }

    /**
     * @param data
     * @return
     */
    public static IpcRequest in(final IpcData data) {
        final IpcEnvelop envelop = IpcEnvelop.newBuilder()
                .setBody(data.getData().toString())
                .setType(Format.JSON).build();
        return IpcRequest.newBuilder()
                .setEnvelop(envelop)
                .build();
    }

    /**
     * Middle process
     *
     * @param request
     * @param type
     */
    public static IpcData consume(final IpcRequest request, final IpcType type) {
        final IpcData ipcData = new IpcData();
        final IpcEnvelop envelop = request.getEnvelop();
        final String data = envelop.getBody();
        final JsonObject json = new JsonObject(data);
        // Address convert
        if (json.containsKey("address")) {
            ipcData.setAddress(json.getString("address"));
            json.remove("address");
        }
        ipcData.setData(Buffer.buffer(data));
        ipcData.setType(type);
        return ipcData;
    }

    /**
     * Final hitted
     *
     * @param data
     * @return
     */
    public static Envelop consume(final IpcData data) {
        final JsonObject json = data.getData().toJsonObject();
        return build(json);
    }

    public static IpcResponse out(final IpcData data) {
        final IpcEnvelop result = IpcEnvelop.newBuilder()
                .setBody(data.getData().toString())
                .setType(Format.JSON)
                .build();
        return IpcResponse.newBuilder().setEnvelop(result).build();
    }

    private static Envelop build(final JsonObject json) {
        Envelop envelop = Envelop.ok();
        // 1. Headers
        if (null != json) {
            // 2.Rebuild
            if (json.containsKey("data")) {
                envelop = Envelop.success(json.getValue("data"));
            }
            // 3.Header
            if (null != json.getValue("header")) {
                final MultiMap headers = MultiMap.caseInsensitiveMultiMap();
                final JsonObject headerData = json.getJsonObject("header");
                for (final String key : headerData.fieldNames()) {
                    final Object value = headerData.getValue(key);
                    if (null != value) {
                        headers.set(key, value.toString());
                    }
                }
                envelop.setHeaders(headers);
            }
            // 4.User
            if (null != json.getValue("user")) {
                envelop.setUser(new VirtualUser(json.getJsonObject("user")));
            }
        }
        return envelop;
    }

    public static Envelop out(final IpcResponse data) {
        return build(outJson(data));
    }

    public static JsonObject outJson(final IpcResponse data) {
        final String json = data.getEnvelop().getBody();
        return Fn.getSemi(Ut.notNil(json), LOGGER,
                () -> new JsonObject(json));
    }
}
