package javax.io.filter;

import io.vertx.up.eon.FileSuffix;

import java.io.FileFilter;

/**
 * .class up.god.file filter
 */
public class ClassFileFilter extends BaseFilter
        implements FileFilter {
    @Override
    public String getFileExtension() {
        return FileSuffix.CLASS;
    }
}
