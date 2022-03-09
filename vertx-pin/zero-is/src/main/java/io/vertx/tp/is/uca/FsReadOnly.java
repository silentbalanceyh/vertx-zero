package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.cv.em.TypeDirectory;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsReadOnly extends FsDefault {
    @Override
    protected IDirectory syncDirectory(final IDirectory directory) {
        /*
         * Store
         */
        directory.setCode(Ut.encryptMD5(directory.getStorePath()));
        /*
         * metadata: {
         *      "deletion": false,
         *      "edition": false
         * }
         */
        final JsonObject metadata = new JsonObject();
        metadata.put("deletion", Boolean.FALSE);
        metadata.put("edition", Boolean.FALSE);
        directory.setMetadata(metadata.encode());
        return directory.setType(TypeDirectory.STORE.name());
    }
}
