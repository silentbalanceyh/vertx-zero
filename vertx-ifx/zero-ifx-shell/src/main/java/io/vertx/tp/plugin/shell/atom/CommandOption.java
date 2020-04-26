package io.vertx.tp.plugin.shell.atom;

import org.apache.commons.cli.Option;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandOption implements Serializable {
    private transient String name;
    private transient String simple;
    private transient String description;
    private transient boolean required = false;

    public String getSimple() {
        return this.simple;
    }

    public void setSimple(final String simple) {
        this.simple = simple;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Option option() {
        final Option option =
                new Option(this.simple, this.name, true, this.description);
        option.setRequired(this.required);
        option.setArgName(this.name);
        return option;
    }
}
