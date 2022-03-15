package io.vertx.up.util;

import java.io.File;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IOCmd {

    static boolean rm(final String filename) {
        final File file = IO.getFile(filename);
        if (Objects.nonNull(file) && file.exists()) {
            return rmLoop(file);
        }
        return false;
    }

    private static boolean rmLoop(final File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (Objects.isNull(files)) {
                files = new File[]{};
            }
            // Remove in loop
            for (final File file : files) {
                final boolean success = rmLoop(file);
                if (!success) {
                    return false;
                }
            }
        }
        // If Directory, it's empty
        return directory.delete();
    }

    static boolean mkdir(final String filename) {
        final File file = new File(filename);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    static boolean rename(final String filename, final String to) {
        final File file = new File(filename);
        if (file.isDirectory() && file.exists()) {
            return file.renameTo(new File(to));
        }
        return false;
    }

    static boolean rmFile(final String filename) {
        final File file = IO.getFile(filename);
        boolean deleted = false;
        if (Objects.nonNull(file) && file.exists()) {
            deleted = file.delete();
        }
        return deleted;
    }
}
