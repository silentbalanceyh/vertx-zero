package io.vertx.up.uca.deployment;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Agent;
import io.vertx.up.annotations.Worker;
import io.vertx.up.atom.agent.Arrange;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.DeployMode;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * DeploymentOptions here for three mode
 * 1) CODE mode, @Agent/@Worker mode setting here
 * 2) CONFIG mode, extract configuration information from xxx.yml
 */
public class DeployRotate implements Rotate {

    private static final Annal LOGGER = Annal.get(DeployRotate.class);
    private static final ConcurrentMap<Class<?>, JsonObject> OPTIONS
        = new ConcurrentHashMap<>();
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static final JsonObject delivery = new JsonObject();

    static {
        /* OPTIONS INIT */
        final JsonObject options = VISITOR.read();
        /* Deployment Options */
        if (options.containsKey(KName.DEPLOYMENT)) {
            final JsonObject deployOptions = options.getJsonObject(KName.DEPLOYMENT);
            /* Arrange */
            final Arrange arrange = Ut.deserialize(deployOptions, Arrange.class);
            /* DeployMode */
            final DeployMode mode = arrange.getMode();
            if (DeployMode.CONFIG == mode) {
                LOGGER.info(Info.INFO_ROTATE, mode);
                /* Options initialized */
                initOptions(arrange.getOptions());
            }
            if (Objects.nonNull(arrange.getDelivery())) {
                delivery.mergeIn(arrange.getDelivery());
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
        final DeploymentOptions options = this.spinOpt(clazz, annotation);

        /* Worker = false */
        options.setWorker(false);

        LOGGER.info(Info.VTC_OPT, options.getInstances(), options.getIsolationGroup(), options.isHa(), options.toJson());
        return options;
    }

    @Override
    public DeliveryOptions spinDelivery() {
        final DeliveryOptions options = new DeliveryOptions();
        options.setSendTimeout(delivery.getLong("timeout", options.getSendTimeout()));
        return options;
    }

    @Override
    public DeploymentOptions spinWorker(final Class<?> clazz) {
        /* @Agent */
        final Annotation annotation = clazz.getDeclaredAnnotation(Worker.class);
        final DeploymentOptions options = this.spinOpt(clazz, annotation);

        /* Worker = false */
        options.setWorker(true);

        LOGGER.info(Info.VTC_OPT, options.getInstances(), options.getIsolationGroup(), options.isHa(), options.toJson());
        return options;
    }

    private DeploymentOptions spinOpt(final Class<?> clazz, final Annotation annotation) {
        final DeploymentOptions options;
        if (!OPTIONS.isEmpty()) {
            /* JsonObject here for deployment options */
            final JsonObject configOpts = OPTIONS.getOrDefault(clazz, new JsonObject());
            /* BUG: workerPoolSize is not in fromJson */
            options = new DeploymentOptions(configOpts);
            if (configOpts.containsKey("workerPoolSize")) {
                options.setWorkerPoolSize(configOpts.getInteger("workerPoolSize"));
            }
        } else {
            options = new DeploymentOptions();
        }
        // 1. Instance
        final int instances = Ut.invoke(annotation, Key.INSTANCES);
        final boolean ha = Ut.invoke(annotation, Key.HA);
        final String group = Ut.invoke(annotation, Key.GROUP);
        // 2. Record Log information
        options.setHa(ha);
        options.setInstances(instances);
        // deprecated: options.setIsolationGroup(group);
        return options;
    }
}
