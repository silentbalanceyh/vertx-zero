package io.vertx.up.util;

import io.vertx.up.fn.Fn;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IOCmd {

    static boolean rm(final String filename) {
        final File file = IO.getFile(filename);
        if (Objects.nonNull(file) && file.exists()) {
            Fn.safeJvm(() -> FileUtils.forceDelete(file));
            // return rmLoop(file);
        }
        return false;
    }

    static boolean mkdir(final String filename) {
        final File file = new File(filename);
        if (!file.exists()) {
            Fn.safeJvm(() -> FileUtils.forceMkdir(file));
        }
        return true;
    }
    /*
    private static boolean rmLoop(final File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (Objects.isNull(files)) {
                files = new File[]{};
            }
            for (final File file : files) {
                final boolean success = rmLoop(file);
                if (!success) {
                    return false;
                }
            }
        }
        return Fn.getJvm(directory::delete);
    }
    */

    static boolean rename(final String filename, final String to) {
        final File fileSrc = new File(filename);
        if (fileSrc.exists()) {
            final File fileTo = new File(to);
            if (fileSrc.isDirectory()) {
                // Directory
                Fn.safeJvm(() -> FileUtils.moveDirectoryToDirectory(fileSrc, fileTo, true));
            } else {
                // File
                Fn.safeJvm(() -> FileUtils.moveFile(fileSrc, fileTo));
            }
        }
        return false;
    }
}
