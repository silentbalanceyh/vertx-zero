package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.batch.IdcStub;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Queue
public class FileActor {

    @Plugin
    private transient ExcelClient client;

    @Address(Addr.User.IMPORT)
    public Future<Envelop> importFile(final Envelop request) {
        /* Import data here for result */
        final String filename = Ux.getString(request);

        final Promise<Envelop> promise = Promise.promise();
        final File file = new File(filename);
        if (file.exists()) {
            Fn.safeJvm(() -> {
                final JsonObject headers = request.headersX().copy();
                /*
                 * Read file to inputStream
                 */
                final InputStream inputStream = new FileInputStream(file);
                /*
                 * Set<ExTable>
                 */
                final Set<ExTable> tables = this.client.ingest(inputStream, true)
                    .stream().filter(Objects::nonNull)
                    .filter(item -> Objects.nonNull(item.getName()))
                    .filter(item -> item.getName().equals("S_USER"))
                    .collect(Collectors.toSet());
                /*
                 * No directory here of importing
                 */
                final JsonArray prepared = new JsonArray();
                tables.stream().flatMap(table -> {
                    final List<JsonObject> records = table.get().stream()
                        .filter(Objects::nonNull)
                        .map(ExRecord::toJson)
                        .collect(Collectors.toList());
                    Sc.infoWeb(this.getClass(), "Table: {0}, Records: {1}", table.getName(), String.valueOf(records.size()));
                    return records.stream();
                }).forEach(record -> {
                    /*
                     * Default value injection
                     * 1）App Env:
                     * -- "sigma": "X-Sigma"
                     * -- "appId": "X-Id"
                     * -- "appKey": "X-Key"
                     */
                    record.mergeIn(headers, true);
                    /*
                     * Required: username, mobile, email
                     */
                    if (Ke.isIn(record, KName.USERNAME)) {
                        // TODO:
                        record.put(KName.LANGUAGE, "cn");
                        prepared.add(record);
                    } else {
                        Sc.warnWeb(this.getClass(), "Ignored record: {0}", record.encode());
                    }
                });
                final String sigma = headers.getString(KName.SIGMA);
                final IdcStub stub = IdcStub.create(sigma);

                final String user = Ke.keyUser(request);
                return stub.saveAsync(prepared, user);
            });
        } else {
            promise.complete(Envelop.success(Boolean.FALSE));
        }
        return promise.future();
    }
}
