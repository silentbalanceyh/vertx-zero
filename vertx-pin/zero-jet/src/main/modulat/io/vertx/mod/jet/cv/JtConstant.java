package io.vertx.mod.jet.cv;

import io.vertx.mod.jet.uca.micro.JtAiakos;
import io.vertx.mod.jet.uca.micro.JtMinos;

public interface JtConstant {
    /* Default namespace build by JtApp */
    String NAMESPACE_PATTERN = "zero.jet.{0}";
    String EVENT_ADDRESS = "Πίδακας δρομολογητή://EVENT-JET/ZERO/UNIFORM";
    /*
     * Component Default
     * - Worker
     * - Consumer
     */
    Class<?> COMPONENT_DEFAULT_WORKER = JtMinos.class;
    Class<?> COMPONENT_DEFAULT_CONSUMER = JtAiakos.class;
    /*
     * Parameter component extract key
     */
    String COMPONENT_INGEST_KEY = "zero.jet.param.ingest";

    String DEFAULT_POOL_DATABASE = "OX_MULTI_APP_DATABASE";
}
