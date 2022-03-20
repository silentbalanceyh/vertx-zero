package io.vertx.tp.is.uca.command;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.cv.em.TypeDirectory;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsReadOnly extends FsDefault {

    @Override
    public IDirectory initTree(final JsonObject directoryJ) {
        /*
         * Store
         */
        final IDirectory directory = super.initTree(directoryJ);
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
