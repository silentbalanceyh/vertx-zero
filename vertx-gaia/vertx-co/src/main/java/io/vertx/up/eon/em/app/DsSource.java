package io.vertx.up.eon.em.app;

public enum DsSource {
    // Default Value ( Major Database configured by 'provider' )
    PRIMARY,
    // History Value ( configured by 'orbit' )
    HISTORY,
    // Workflow Value ( configured by 'workflow' )
    WORKFLOW,
    // Dynamic Value ( Build by 'DataPool' here )
    DYNAMIC,
    // Extension Value ( configured by '<key>' that provided )
    EXTENSION,
}
