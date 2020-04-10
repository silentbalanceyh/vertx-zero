package io.vertx.tp.plugin.shared;

interface Info {

    String INFO_SYNC = "( Sync ) You are using sync mode to create LocalMap: {0}";

    String INFO_ASYNC_START = "( Async ) You are using async mode to create AsyncMap, begin to singleton.";

    String INFO_ASYNC_END = "( Async ) Your AsyncMap {0} has been created successfully, you can use it now.";

    String INFO_TIMER_PUT = "( Timer ) You called timer put method, the key \"{0}\" will be expired in {1} seconds";

    String INFO_TIMER_EXPIRE = "( Timer ) The key \"{0}\" refered data has been removed.";

    String INFO_TIMER_REMOVED = "( Timer ) The key \"{0}\" does not exist, it has been removed before.";
}
