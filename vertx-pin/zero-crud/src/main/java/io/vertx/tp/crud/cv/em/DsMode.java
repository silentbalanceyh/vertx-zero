package io.vertx.tp.crud.cv.em;

public enum DsMode {
    // Default Value ( Major Database configured by 'provider' )
    PRIMARY,
    // History Value ( configured by 'orbit' )
    HISTORY,
    // Dynamic Value ( Build by 'DataPool' here )
    DYNAMIC,
    // Extension Value ( configured by '<key>' that provided )
    EXTENSION,
}
