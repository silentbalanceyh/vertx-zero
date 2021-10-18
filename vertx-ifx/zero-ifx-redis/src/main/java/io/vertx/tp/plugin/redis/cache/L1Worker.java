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
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
                /*
                 * Flag must not be NONE
                 */
                if (Objects.nonNull(body)) {
                    /*
                     * Buffer converted to data
                     */
                    final JsonObject jsonBody = body.toJsonObject();
                    /*
                     * Data Updating
                     */
                    this.updateCache(jsonBody);

                    /*
                     * Data Deleting
                     */
                    this.deleteCache(jsonBody);
                }
            });
        }
    }

    private void deleteCache(final JsonObject jsonBody) {
        final ChangeFlag flag = Ut.toEnum(() -> jsonBody.getString("flag"), ChangeFlag.class, ChangeFlag.NONE);
        if (ChangeFlag.DELETE == flag) {
            // Delete Cache for L1
            final Object cacheKey = jsonBody.getValue("data");
            if (Objects.nonNull(cacheKey)) {
                /*
                 * Deleted by key with `L1Channel`
                 */
                final L1Channel channel = new L1Channel();
                /*
                 * Call erase
                 */
                channel.eraseTree(cacheKey.toString());
            }
        }
    }

    private void updateCache(final JsonObject jsonBody) {
        final ChangeFlag flag = Ut.toEnum(() -> jsonBody.getString("flag"), ChangeFlag.class, ChangeFlag.NONE);
        if (ChangeFlag.UPDATE == flag) {
            /*
             * L1Channel created
             */
            final L1Channel channel = new L1Channel();

            /*
             * L1Algorithm
             */
            final L1Algorithm algorithm = this.create(jsonBody);
            final ConcurrentMap<String, Object> mapped = algorithm.buildData(jsonBody);
            channel.write(mapped, flag);

            /*
             * Key Processing
             */
            final ConcurrentMap<String, Object> mappedKey = algorithm.buildReference(jsonBody);
            /*
             * Calculate the prefix of current type
             */
            channel.append(mappedKey);
        }
    }

    private L1Algorithm create(final JsonObject jsonBody) {
        final Boolean isCollection = jsonBody.getBoolean("collection", Boolean.FALSE);
        final L1Algorithm algorithm;
        if (isCollection) {
            algorithm = Ut.singleton(AlgorithmCollection.class);
        } else {
            algorithm = Ut.singleton(AlgorithmRecord.class);
        }
        return algorithm;
    }
}
