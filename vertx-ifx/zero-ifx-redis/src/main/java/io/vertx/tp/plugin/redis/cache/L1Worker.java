package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.AlgorithmCollection;
import io.vertx.tp.plugin.cache.hit.AlgorithmRecord;
import io.vertx.tp.plugin.cache.hit.L1Algorithm;
import io.vertx.tp.plugin.cache.l1.L1Config;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1Worker extends AbstractVerticle {

    @Override
    public void start() {
        final L1Config config = Ut.deserialize(this.config(), L1Config.class);
        if (Ut.notNil(config.getAddress())) {
            /*
             * EventBus processing
             */
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Consumer
             */
            eventBus.<Buffer>consumer(config.getAddress(), handler -> {
                /*
                 * Processing
                 */
                final Buffer body = handler.body();
                if (Objects.nonNull(body)) {
                    /*
                     * Buffer converted to data
                     */
                    final JsonObject jsonBody = body.toJsonObject();
                    /*
                     * Mapped data
                     */
                    final Boolean isCollection = jsonBody.getBoolean("collection", Boolean.FALSE);
                    final L1Algorithm algorithm;
                    if (isCollection) {
                        algorithm = Ut.singleton(AlgorithmCollection.class);
                    } else {
                        algorithm = Ut.singleton(AlgorithmRecord.class);
                    }
                    final ConcurrentMap<String, Object> mapped = algorithm.dataCache(jsonBody);
                    /*
                     * Redis
                     */
                    final ChangeFlag flag = Ut.toEnum(() -> jsonBody.getString("flag"), ChangeFlag.class, ChangeFlag.NONE);
                    if (ChangeFlag.NONE != flag) {
                        final L1Channel channel = new L1Channel();
                        channel.write(mapped, flag);
                        /*
                         * Key Processing
                         */
                        final ConcurrentMap<String, Object> mappedKey = algorithm.dataKey(jsonBody);
                        /*
                         * Calculate the prefix of current type
                         */
                        channel.combine(mappedKey);
                    }
                }
            });
        }
    }
}
