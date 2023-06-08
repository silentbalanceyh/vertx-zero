package io.vertx.up.backbone.mime.parse;

interface INFO {
    String RESOLVER = "( Resolver ) Select resolver {0} " +
        "for Content-Type {1} when request to {2}";

    String RESOLVER_DEFAULT = "( Resolver ) Select resolver {0} as [DEFAULT] " +
        "for Content-Type = null when request to {1}";

    String RESOLVER_CONFIG = "( Resolver ) Select resolver from " +
        "annotation config \"{0}\" for Content-Type {1}";
}
