package javax.io.filter;

import io.horizon.eon.VPath;

import java.io.FileFilter;

;

/**
 * # 「Tp」Java Io Extension
 *
 * This class inherit from {@link javax.io.filter.BaseFilter} to extend `java.io.FileFilter` and it read `.class` file
 * only.
 *
 * The usage in zero framework code segments are as following:
 *
 * ```java
 * // <pre><code class="java">
 *
 *      //Read the folder of current packaqge
 *      final File file = new File(packPath)
 *      // If it does not exist, return directly
 *      if(!file.exists() || !file.isDirectory()){
 *          return;
 *      }
 *      // If exist, list all files include directory
 *      final File[] dirfiles = file.listFiles(new ClassFileFilter());
 *
 * // </code></pre>
 * ```
 *
 * Above code is in `package scanner` component of zero framework.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ClassFileFilter extends BaseFilter
    implements FileFilter {
    @Override
    public String getFileExtension() {
        return VPath.SUFFIX.CLASS;
    }
}
