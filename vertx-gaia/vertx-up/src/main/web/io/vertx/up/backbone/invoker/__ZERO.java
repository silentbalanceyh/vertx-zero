package io.vertx.up.backbone.invoker;

import io.horizon.uca.cache.Cc;

interface INFO {

    String MSG_DIRECT = "( Invoker ) Invoker = {0}, ReturnType = {1}, Method = {2}, Class = {3}.";

    String MSG_RPC = "( Invoker Rpc ) Invoker = {0}, ReturnType = {1}, Method = {2}, Class = {3}.";

    String MSG_HANDLE = "( Invoker Handle ) Invoker = {0}, ReturnType = {1}, Method = {2}, Class = {3}.";
}

interface CACHE {
    // Invoker Cache for Multi Thread
    Cc<String, Invoker> CCT_INVOKER = Cc.openThread();
}
