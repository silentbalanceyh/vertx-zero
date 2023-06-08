package io.vertx.up.plugin.shared;

interface INFO {

    String INFO_SYNC = "( Sync ) You are using sync mode to create LocalMap: {1} = {0}";

    String INFO_ASYNC_START = "( Async ) You are using async mode to create AsyncMap, begin to singleton.";

    String INFO_ASYNC_END = "( Async ) Your AsyncMap {1} = {0} has been created successfully, you can use it now.";

    String INFO_TIMER_PUT = "( Timer ) You called timer put method, the key \"{0}\" will be expired in {1} seconds";

    String INFO_TIMER_EXPIRE = "( Timer ) The key \"{0}\" refered data has been removed.";

    String INFO_TIMER_REMOVED = "( Timer ) The key \"{0}\" does not exist, it has been removed before.";

    interface UxPool {
        String POOL_PUT = "( Shared ) key = {0}, value = {1} has been put into {2}.";
        String POOL_PUT_TIMER = "( Shared ) key = {0}, value = {1} has been put into {2} to keep {3} seconds";
        String POOL_REMOVE = "( Shared ) key = {0} has been removed from pool name = {2}.";
        String POOL_GET = "( Shared ) key = {0} has been picked from {1}, mode = {2}";
        String POOL_CLEAR = "( Shared ) pool = {0} has been cleared successfully.";
    }
}
