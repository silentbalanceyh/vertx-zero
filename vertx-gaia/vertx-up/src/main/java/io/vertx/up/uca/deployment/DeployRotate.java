package io.vertx.up.uca.deployment;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Agent;
import io.vertx.up.annotations.Worker;
import io.vertx.up.atom.agent.Arrange;
import io.vertx.up.eon.em.DeployMode;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * DeploymentOptions here for three mode
 * 1) CODE mode, @Agent/@Worker mode setting here
 * 2) CONFIG mode, extract configuration information from xxx.yml
 */
public class DeployRotate implements Rotate {

    private static final String KEY_DEPLOYMENT = "deployment";
    private static final Annal LOGGER = Annal.get(DeployRotate.class);
    private static final ConcurrentMap<Class<?>, JsonObject> OPTIONS
            = new ConcurrentHashMap<>();
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    static {
        /* OPTIONS INIT */
        final JsonObject options = VISITOR.read();
        /* Deployment Options */
        if (options.containsKey(KEY_DEPLOYMENT)) {
            final JsonObject deployOptions = options.getJsonObject(KEY_DEPLOYMENT);
            /* Arrange */
            final Arrange arrange = Ut.deserialize(deployOptions, Arrange.class);
            /* DeployMode */
            final DeployMode mode = arrange.getMode();
            if (DeployMode.CONFIG == mode) {
                LOGGER.info(Info.INFO_ROTATE, mode);
                /* Options initialized */
                initOptions(arrange.getOptions());
            }
        }
    }

    private static void initOptions(final JsonObject options) {
        if (!Ut.isNil(options)) {
            /* Options processing */
            options.fieldNames().forEach(className -> {
                /* clazz */
                final Class<?> clazz = Ut.clazz(className);
                /* options */
                final JsonObject option = options.getJsonObject(className);

                OPTIONS.put(clazz, option);
            });
        }
    }

    @Override
    public DeploymentOptions spinAgent(final Class<?> clazz) {
        /* @Agent */
        final Annotation annotation = clazz.getDeclaredAnnotation(Agent.class);
        final DeploymentOptions options = spinOpts(annotation);

        /* Clazz Deployment Options */
        spinConfig(clazz, options);

        /* Worker = false */
        options.setWorker(false);

        LOGGER.info(Info.VTC_OPT, options.getInstances(), options.getIsolationGroup(), options.isHa(), options.toJson());
        return options;
    }

    @Override
    public DeploymentOptions spinWorker(final Class<?> clazz) {
        /* @Agent */
        final Annotation annotation = clazz.getDeclaredAnnotation(Worker.class);
        final DeploymentOptions options = spinOpts(annotation);

        /* Clazz Deployment Options */
        spinConfig(clazz, options);

        /* Worker = false */
        options.setWorker(true);

        LOGGER.info(Info.VTC_OPT, options.getInstances(), options.getIsolationGroup(), options.isHa(), options.toJson());
        return options;
    }

    private void spinConfig(final Class<?> clazz, final DeploymentOptions options) {
        /* Empty checking */
        if (!OPTIONS.isEmpty()) {
            /* JsonObject here for deployment options */
            final JsonObject configOpts = OPTIONS.getOrDefault(clazz, new JsonObject());
            /* Old configuration */
            final JsonObject codeOpts = options.toJson();
            /* Updated */
            codeOpts.mergeIn(configOpts, true);
            options.fromJson(codeOpts);
        }
    }

    private DeploymentOptions spinOpts(final Annotation annotation) {
        // 1. Instance
        final int instances = Ut.invoke(annotation, Key.INSTANCES);
        final boolean ha = Ut.invoke(annotation, Key.HA);
        final String group = Ut.invoke(annotation, Key.GROUP);
        // 2. Record Log information
        final DeploymentOptions options = new DeploymentOptions();
        options.setHa(ha);
        options.setInstances(instances);
        options.setIsolationGroup(group);
        return options;
    }
}
