package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Post<T> {

    static Post apeak() {
        return Fn.poolThread(Pooled.POST_MAP, ApeakPost::new, ApeakPost.class.getName());
    }

    Future<T> outAsync(T active, T standBy);
}
