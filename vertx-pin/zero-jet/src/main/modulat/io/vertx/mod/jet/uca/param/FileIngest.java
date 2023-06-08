package io.vertx.mod.jet.uca.param;

import io.horizon.spi.jet.JtIngest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.cv.em.ParamMode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KWeb;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/*
 * The new type for uploading file here
 *
 * This type also support QUERY/PATH part
 *
 * But no body, stream body instead of other information
 * 1) New key for ID.PARAM_STREAM to store file here
 * 2) Here we stored Buffer of this file into stream body
 */
class FileIngest implements JtIngest {
    private transient final Supplier<JtIngest> supplier = Pool.INNER_INGEST.get(ParamMode.QUERY);

    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        final JtIngest ingest = this.supplier.get();
        final Envelop envelop = ingest.in(context, uri);
        /*
         * Stream part processing
         */
        final JsonArray files = new JsonArray();
        final Set<FileUpload> uploadSet = new HashSet<>(context.fileUploads());
        if (!uploadSet.isEmpty()) {
            /*
             * File Uploading here, this file will be consumed right now
             * It won't be stored, it means that there should be Buffer stored only
             */
            uploadSet.stream().map(upload -> {
                final JsonObject item = new JsonObject();
                item.put("path", upload.uploadedFileName());
                item.put("mime", upload.contentType());
                item.put("size", upload.size());
                return item;
            }).forEach(files::add);
        }
        envelop.value(KWeb.ARGS.PARAM_STREAM, files);
        return envelop;
    }
}
