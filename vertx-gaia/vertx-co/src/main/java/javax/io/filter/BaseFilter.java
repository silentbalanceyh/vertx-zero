package javax.io.filter;

import io.vertx.up.eon.Strings;

import java.io.File;

/**
 * File filter structure
 */
public abstract class BaseFilter {

    private transient boolean recursive = true;

    protected BaseFilter() {
        this(true);
    }

    protected BaseFilter(final boolean recursive) {
        this.recursive = recursive;
    }

    public boolean accept(final File file) {
        return (recursive && file.isDirectory())
                || (file.getName().endsWith(Strings.DOT + getFileExtension()));
    }

    public abstract String getFileExtension();
}
