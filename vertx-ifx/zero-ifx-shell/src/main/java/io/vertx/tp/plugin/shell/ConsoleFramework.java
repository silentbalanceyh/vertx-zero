package io.vertx.tp.plugin.shell;

import io.horizon.eon.em.Environment;
import io.horizon.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.tp.error.InternalConflictException;
import io.vertx.tp.plugin.shell.refine.Sl;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConsoleFramework {
    private static final Annal LOGGER = Annal.get(ConsoleFramework.class);
    private static final ConcurrentMap<String, Consumer<String>> INTERNAL =
        new ConcurrentHashMap<>();

    static {
        Sl.init();
    }

    private final transient Vertx vertxRef;

    private ConsoleFramework(final Vertx vertxRef) {
        this.vertxRef = vertxRef;
        if (INTERNAL.isEmpty()) {
            /*
             * config dev
             * config ( Default for production )
             */
            INTERNAL.put("config", arg -> {
                /*
                 * Callback consume for execution
                 */
                final ConsoleInteract interact;
                if ("dev".equals(arg)) {
                    interact = ConsoleInteract.start(this.vertxRef, Environment.Development);
                } else {
                    LOGGER.info("The console will go through production mode");
                    interact = ConsoleInteract.start(this.vertxRef, Environment.Production);
                }
                interact.run(arg);
            });
        }
    }

    public static ConsoleFramework start(final Vertx vertxRef) {
        return new ConsoleFramework(vertxRef);
    }

    public ConsoleFramework bind(final Consumer<String> consumer) {
        if (INTERNAL.containsKey("start")) {
            LOGGER.warn("There exist 'start' consumer, you'll overwrite previous.");
        }
        return this.bind("start", consumer);
    }

    /**
     * The start point of zero console for console started up
     *
     * @param args Input command here
     */
    public void run(final String[] args) {
        /*
         * Arguments process
         */
        if (Sl.ready(args)) {
            /*
             * Argument
             */
            final String input = args[0];
            final Consumer<String> consumer = INTERNAL.get(input);
            if (Objects.nonNull(consumer)) {
                final String consumerArgs = 2 == args.length ? args[1] : null;
                consumer.accept(consumerArgs);
            } else {
                LOGGER.warn("No consumer found for argument `{0}`", input);
                System.exit(-1);
            }
        } else {
            System.exit(-1);
        }
    }

    /*
     * 绑定 args 中的执行
     */
    public ConsoleFramework bind(final String name, final Consumer<String> consumer) {
        if ("config".equals(name)) {
            throw new InternalConflictException(ConsoleFramework.class);
        }
        INTERNAL.put(name, consumer);
        return this;
    }
}
