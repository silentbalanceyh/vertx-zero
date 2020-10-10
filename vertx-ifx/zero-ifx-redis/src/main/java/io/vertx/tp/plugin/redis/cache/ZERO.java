package io.vertx.tp.plugin.redis.cache;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
interface CacheMsg {
    String HIT_FAILURE = "( Cache ) L1: The key `{0}` has not been Hit !!!.";
    String HIT_SECONDARY = "( Cache ) L1: Read data by nested key `{0}` with original `{1}`.";

    String DATA_REFRESHED = "( Cache ) The key `{0}` has been refreshed. ";
    String DATA_TREE = "( Cache ) The key `{0}` has been synced on tree.";
}
