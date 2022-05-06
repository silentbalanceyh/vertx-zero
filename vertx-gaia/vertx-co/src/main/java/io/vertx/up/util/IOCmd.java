package io.vertx.up.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Log;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IOCmd {

    /*
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(IO.class);

    static Buffer zip(final Set<String> fileSet) {
        // Create Tpl zip file path
        return Fn.getJvm(() -> {
            final ByteArrayOutputStream fos = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

            // Buffer Size
            final byte[] buffers = new byte[Values.CACHE_SIZE];
            fileSet.forEach(filename -> Fn.safeJvm(() -> {
                // create .zip and put file here
                final File file = new File(filename);
                final ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                // Read File content and put them in the zip
                final FileInputStream fis = new FileInputStream(file);
                final BufferedInputStream bis = new BufferedInputStream(fis, Values.CACHE_SIZE);
                int read;
                while ((read = bis.read(buffers, 0, Values.CACHE_SIZE)) != -1) {
                    zos.write(buffers, 0, read);
                }
                bis.close();
            }));
            zos.close();
            return Buffer.buffer(fos.toByteArray());
        });
    }

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

    static boolean rename(final String filename, final String to) {
        final File fileSrc = new File(filename);
        if (fileSrc.exists()) {
            final File fileTo = new File(to);
            final File fileToP = fileTo.getParentFile();
            Log.info(LOGGER, Info.IO_CMD_MOVE, fileSrc.getAbsolutePath(), fileToP.getAbsolutePath());
            if (fileSrc.isDirectory()) {
                // Directory Copy, folder to folder
                Fn.safeJvm(() -> FileUtils.moveDirectory(fileSrc, fileTo));
            } else {
                // File ( Replace Old One )
                if (Ut.ioExist(to)) {
                    // Fix: org.apache.commons.io.FileExistsException: File element in parameter 'null' already exists:
                    rm(to);
                }
                Fn.safeJvm(() -> FileUtils.moveFile(fileSrc, fileTo, StandardCopyOption.REPLACE_EXISTING));
            }
        }
        return false;
    }
}
