package javax.io.filter;

import io.horizon.eon.VString;

import java.io.File;

/**
 * # 「Tp」Java Io Extension
 *
 * In most business io file reading, we often distinguish file types by file extension instead of checking
 * file actual format. This class is base file filter that provide uniform file extension checking.
 *
 * The structure should be:
 *
 * ```shell
 * # <pre>
 *
 *   javax.io.filter.BaseFilter ----- provide -----> public boolean accept(File);
 *            ^
 *            |
 *   javax.io.filter.ClassFileFilter ----> implements ----> java.io.FileFilter;
 *
 * # </pre>
 * ```
 *
 * You can write your own file name filter for different file reading.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class BaseFilter {

    private transient boolean recursive = true;

    protected BaseFilter() {
        this(true);
    }

    protected BaseFilter(final boolean recursive) {
        this.recursive = recursive;
    }

    public boolean accept(final File file) {
        return (this.recursive && file.isDirectory())
            || (file.getName().endsWith(VString.DOT + this.getFileExtension()));
    }

    public abstract String getFileExtension();
}
