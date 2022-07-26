package io.vertx.aeon.component.alive;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.aeon.eon.em.TypeRepo;
import io.vertx.core.Future;
import io.vertx.up.unity.Ux;

/**
 * 代码库的链接过程
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FeedGitHub extends AbstractFeed {
    @Override
    public TypeRepo support() {
        return TypeRepo.GIT_HUB;
    }

    @Override
    public Future<Boolean> configure(final HRepo input) {
        return Ux.futureT();
    }
}
