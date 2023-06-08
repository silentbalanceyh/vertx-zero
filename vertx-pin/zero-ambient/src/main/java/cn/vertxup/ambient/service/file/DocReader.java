package cn.vertxup.ambient.service.file;

import cn.vertxup.ambient.service.DatumStub;
import io.horizon.spi.business.ExIo;
import io.horizon.spi.business.ExUser;
import io.horizon.spi.feature.Arbor;
import io.horizon.spi.feature.Attachment;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DocReader implements DocRStub {
    private static final Cc<String, Arbor> CC_ARBOR = Cc.openThread();
    private static final Annal LOGGER = Annal.get(DocReader.class);
    @Inject
    private transient DatumStub stub;
    @Inject
    private transient Attachment attachment;


    // ------------------------- Document Method ( Other ) -------------------------

    @Override
    public Future<JsonArray> fetchDoc(final String sigma, final String directoryId) {
        Objects.requireNonNull(sigma);
        /*
         * Fetch attachment first
         * 1. Copy `directory` visitMode to attachment
         * 2. Fetch `directory` of children
         */
        return Ux.channel(ExIo.class, JsonArray::new, io -> io.dirRun(sigma, directoryId)).compose(directory -> {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.DIRECTORY_ID, directoryId);
            // active = true
            condition.put(KName.ACTIVE, Boolean.TRUE);
            condition.put(KName.SIGMA, sigma);
            return this.attachment.fetchAsync(condition).compose(files -> {
                directory.addAll(files);
                return Ux.future(directory);
            });
        });
    }

    @Override
    public Future<JsonArray> fetchTrash(final String sigma) {
        Objects.requireNonNull(sigma);
        return Ux.channel(ExIo.class, JsonArray::new, io -> io.dirTrash(sigma)).compose(directory -> {
            final JsonObject condition = Ux.whereAnd();
            // active = false
            condition.put(KName.ACTIVE, Boolean.FALSE);
            condition.put(KName.SIGMA, sigma);
            return this.attachment.fetchAsync(condition).compose(files -> {
                directory.addAll(files);
                return Ux.future(directory);
            });
        });
    }

    @Override
    public Future<JsonArray> searchDoc(final String sigma, final String keyword) {
        Objects.requireNonNull(sigma);
        /* Attachment Only */
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        /*
         * createdBy is the owner of attachment record because here the attachment
         * file could not be updated, there are two operation only:
         * 1 - Upload
         * 2 - Replaced
         *  */
        return Ux.channel(ExUser.class, JsonArray::new, user -> user.searchUser(keyword)).compose(keys -> {
            if (Ut.isNotNil(keys)) {
                // User Matched
                final JsonObject criteria = Ux.whereOr();
                criteria.put(KName.NAME + ",c", keyword);
                criteria.put(KName.CREATED_BY + ",i", keys);
                condition.put("$Qr$", criteria);
            } else {
                condition.put(KName.NAME + ",c", keyword);
            }
            return this.attachment.fetchAsync(condition);
        });
    }

    // ------------------------- Document Method ( Download ) -------------------------

    @Override
    public Future<Buffer> downloadDoc(final String key) {
        return this.attachment.downloadAsync(key);
    }

    @Override
    public Future<Buffer> downloadDoc(final Set<String> keys) {
        return this.attachment.downloadAsync(keys);
    }
}
