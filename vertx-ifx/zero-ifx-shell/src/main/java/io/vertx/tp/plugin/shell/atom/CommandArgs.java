package io.vertx.tp.plugin.shell.atom;

import io.vertx.up.util.Ut;
import org.apache.commons.cli.Options;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandArgs implements Serializable {
    private final ConcurrentMap<String, String> inputValue = new ConcurrentHashMap<>();
    private Options options = new Options();

    private CommandArgs(final List<String> names, final List<String> values) {
        Ut.itList(names, (name, index) -> {
            final String value = values.get(index);
            this.inputValue.put(name, value);
        });
    }

    public static CommandArgs create(final List<String> names, final List<String> values) {
        return new CommandArgs(names, values);
    }

    public String get(final String name) {
        return this.inputValue.get(name);
    }

    public ConcurrentMap<String, String> get() {
        return this.inputValue;
    }

    public Options options() {
        return this.options;
    }

    public CommandArgs bind(final Options options) {
        this.options = options;
        return this;
    }
}
