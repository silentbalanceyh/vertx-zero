package io.vertx.up.util;

import io.vertx.up.fn.Fn;
import io.vertx.up.log.Log;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IOCmd {

    private static final Logger LOGGER
        = LoggerFactory.getLogger(IO.class);

    static boolean rm(final String filename) {
        final File file = IO.getFile(filename);
        if (Objects.nonNull(file) && file.exists()) {
            Log.info(LOGGER, Info.IO_CMD_RM, file.getAbsolutePath());
            Fn.safeJvm(() -> FileUtils.forceDelete(file));
        }
        return false;
    }

    static boolean mkdir(final String filename) {
        final File file = new File(filename);
        if (!file.exists()) {
            Log.info(LOGGER, Info.IO_CMD_MKDIR, file.getAbsolutePath());
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
            Log.info(LOGGER, Info.IO_CMD_MOVE, fileSrc.getAbsolutePath(), fileTo.getAbsolutePath());
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
