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
public class CommandInput implements Serializable {
    protected final ConcurrentMap<String, CommandAtom> defineMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> inputValue = new ConcurrentHashMap<>();
    private transient Options options;

    private CommandInput(final List<String> names, final List<String> values) {
        if (values.size() >= names.size()) {
            // Fix: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            Ut.itList(names, (name, index) -> {
                final String value = values.get(index);
                this.inputValue.put(name, value);
            });
        }
    }

    public static CommandInput create(final List<String> names, final List<String> values) {
        return new CommandInput(names, values);
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

    public CommandInput bind(final CommandAtom atom) {
        this.options = atom.options();
        return this;
    }

    public CommandInput bind(final List<CommandAtom> atoms) {
        atoms.forEach(atom -> this.defineMap.put(atom.getSimple(), atom));
        return this;
    }

    public ConcurrentMap<String, CommandAtom> atom() {
        return this.defineMap;
    }
}
