package cn.vertxup.rbac.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.relation.IdcStub;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.excel.ExcelClient;
import io.vertx.up.plugin.excel.atom.ExRecord;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vertx.mod.rbac.refine.Sc.LOG;

@Queue
public class FileActor {

    @Infusion
    private transient ExcelClient client;

    @Address(Addr.User.IMPORT)
    public Future<Envelop> importFile(final Envelop request) {
        /* Import data here for result */
        final String filename = Ux.getString(request);

        final File file = new File(filename);
        if (!file.exists()) {
            return Ux.future(Envelop.success(Boolean.FALSE));
        }
        return Fn.failOr(() -> {
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
                LOG.Web.info(this.getClass(), "Table: {0}, Records: {1}", table.getName(), String.valueOf(records.size()));
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
                if (Ut.isIn(record, KName.USERNAME)) {
                    record.put(KName.LANGUAGE, KWeb.ARGS.V_LANGUAGE);
                    prepared.add(record);
                } else {
                    LOG.Web.warn(this.getClass(), "Ignored record: {0}", record.encode());
                }
            });
            final String sigma = headers.getString(KName.SIGMA);
            final IdcStub stub = IdcStub.create(sigma);

            final String user = request.userId();
            return stub.saveAsync(prepared, user)
                /*
                 * The User import has not response here for result
                 * The old code is `safeJvm` and without any response send to client,
                 * it means that it's wrong here for usage.
                 * Below line will resolve the issue of User Importing.
                 */
                .compose(userA -> Ux.future(Envelop.success(userA)));
        });
    }
}
