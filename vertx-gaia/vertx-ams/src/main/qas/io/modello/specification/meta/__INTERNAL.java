package io.modello.specification.meta;

import io.horizon.spi.modeler.MetaOn;
import io.horizon.uca.cache.Cc;


interface CACHE {
    Cc<String, MetaOn> CCT_META_ON = Cc.openThread();
}
