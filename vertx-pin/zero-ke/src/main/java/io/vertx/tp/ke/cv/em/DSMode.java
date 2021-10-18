package io.vertx.tp.ke.cv.em;

public enum DSMode {
    // Default Value ( Major Database configured by 'provider' )
    PRIMARY,
    // History Value ( configured by 'orbit' )
    HISTORY,
    // Dynamic Value ( Build by 'DataPool' here )
    DYNAMIC,
    // Extension Value ( configured by '<key>' that provided )
    EXTENSION,
}
