package io.vertx.up.commune;

interface MESSAGE {
    interface Ruler {
        /**
         *
         **/
        String RULE_FILE = "[V] Rule up.god.file = {0} has been hitted. ";
        /**
         *
         **/
        String RULE_CACHED_FILE = "[V] Rule up.god.file = {0}, read from memory directly.";

    }
}
