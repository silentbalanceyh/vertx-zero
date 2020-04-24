package io.vertx.tp.plugin.shell;

import io.vertx.tp.error.InternalConflictException;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.log.Annal;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleFramework {
    private static final Annal LOGGER = Annal.get(ConsoleFramework.class);
    private static final ConcurrentMap<String, Consumer<String>> INTERNAL =
            new ConcurrentHashMap<>();

    static {
        Sl.init();
        /*
         * `start`      - this must be bind with method boot
         * `config`     - Default Production
         * `dev`        - Default Development
         */
        INTERNAL.put("config", ConsoleInteract.start(Environment.Production)::run);
        INTERNAL.put("dev", ConsoleInteract.start(Environment.Development)::run);
    }

    public static ConsoleFramework start() {
        return new ConsoleFramework();
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
                consumer.accept(input);
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
        if ("config".equals(name) || "dev".equals(name)) {
            throw new InternalConflictException(ConsoleFramework.class);
        }
        INTERNAL.put(name, consumer);
        return this;
    }
}
