package io.horizon.spi.jet;

import io.horizon.uca.cache.Cc;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.uca.param.DataIngest;
import io.vertx.up.commune.Envelop;

/*
 * 「Extension」
 *  When the paramMode is `DEFINE`, it means that you must set the extension class name into
 *  vertx-inject.yml
 *  The key is `zero.jet.param.ingest`
 *  In your content of vertx-inject.yml file, here you should define
 *
 *  zero.jet.param.ingest=xxxxxx
 *
 *  `xxxxx` means class name, the class definition must be:
 *  1) implements JtIngest interface
 *  2) This class must contain public constructor without any arguments
 */
public interface JtIngest {

    Cc<String, JtIngest> CC_INGEST = Cc.openThread();

    static JtIngest getInstance() {
        return CC_INGEST.pick(DataIngest::new);
        // Fn.po?lThread(Pool.POOL_INGEST, DataIngest::new);
    }

    /*
     * Different workflow will call component other
     */
    Envelop in(RoutingContext context, JtUri uri);
}
