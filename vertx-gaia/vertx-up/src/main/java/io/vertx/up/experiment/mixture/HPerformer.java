package io.vertx.up.experiment.mixture;

import io.vertx.core.Future;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HPerformer<T extends HModel> {
    /* 「Read Model」Async */
    Future<T> fetchAsync(String identifier);

    /* 「Read Model」Sync */
    T fetch(String identifier);
}
