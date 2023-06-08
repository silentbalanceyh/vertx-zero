package io.vertx.up.runtime;

interface Info {

    String CLASSES = "Zero system scanned `{0}` classes in total.";
    String IGNORES = "Ignored packages: {0}, please check whether contains yours";

    interface ZeroStore {
        String PLUGIN_LOAD = "The raw data ( node = {0} ) has been detected plugin ( {1} = {2} )";
    }
}