package io.vertx.up.uca.fs;

import io.vertx.aeon.refine.HLog;
import io.vertx.aeon.specification.app.HFS;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class LocalFs implements HFS {
    @Override
    public boolean mkdir(final String dirName) {
        final File file = new File(dirName);
        if (!file.exists()) {
            HLog.infoFs(this.getClass(), Info.IO_CMD_MKDIR, file.getAbsolutePath());
            Fn.safeJvm(() -> FileUtils.forceMkdir(file));
        }
        return true;
    }

    @Override
    public boolean rm(final String filename) {
        final File file = Ut.ioFile(filename);
        if (Objects.nonNull(file) && file.exists()) {
            HLog.infoFs(this.getClass(), Info.IO_CMD_RM, file.getAbsolutePath());
            Fn.safeJvm(() -> FileUtils.forceDelete(file));
        }
        return true;
    }


    @Override
    public boolean cp(final String nameFrom, final String nameTo) {
        final File fileSrc = new File(nameFrom);
        final File fileDst = new File(nameTo);
        if (fileSrc.exists() && fileDst.exists()) {
            if (fileDst.isDirectory()) {
                // The target must be directory
                if (fileSrc.isFile()) {
                    // File -> Directory
                    HLog.infoFs(this.getClass(), Info.IO_CMD_CP, nameFrom, nameTo, "copyFileToDirectory");
                    Fn.safeJvm(() -> FileUtils.copyFileToDirectory(fileSrc, fileDst, true));
                } else {
                    if (fileSrc.getName().equals(fileDst.getName())) {
                        // Directory -> Directory ( Overwrite )
                        HLog.infoFs(this.getClass(), Info.IO_CMD_CP, nameFrom, nameTo, "copyDirectory");
                        Fn.safeJvm(() -> FileUtils.copyDirectory(fileSrc, fileDst, true));
                    } else {
                        // Directory -> Directory / ( Children )
                        HLog.infoFs(this.getClass(), Info.IO_CMD_CP, nameFrom, nameTo, "copyDirectoryToDirectory");
                        Fn.safeJvm(() -> FileUtils.copyDirectoryToDirectory(fileSrc, fileDst));
                    }
                }
            } else {
                // File -> File
                if (fileSrc.isFile()) {
                    HLog.infoFs(this.getClass(), Info.IO_CMD_CP, nameFrom, nameTo, "copyFile");
                    Fn.safeJvm(() -> FileUtils.copyFile(fileSrc, fileDst, true));
                }
            }
        } else {
            HLog.warnFs(this.getClass(), Info.ERR_CMD_CP, nameFrom, nameTo);
        }
        return true;
    }

    @Override
    public boolean rename(final String nameFrom, final String nameTo) {
        final File fileSrc = new File(nameFrom);
        if (fileSrc.exists()) {
            final File fileTo = new File(nameTo);
            final File fileToP = fileTo.getParentFile();
            HLog.infoFs(this.getClass(), Info.IO_CMD_MOVE, fileSrc.getAbsolutePath(), fileToP.getAbsolutePath());
            if (fileSrc.isDirectory()) {
                // 目录拷贝：目录 -> 目录
                Fn.safeJvm(() -> FileUtils.moveDirectory(fileSrc, fileTo));
            } else {
                // 文件拷贝（替换原始文件）
                if (Ut.ioExist(nameTo)) {
                    // Fix: org.apache.commons.io.FileExistsException:
                    //      File element in parameter 'null' already exists:
                    this.rm(nameTo);
                }
                Fn.safeJvm(() -> FileUtils.moveFile(fileSrc, fileTo, StandardCopyOption.REPLACE_EXISTING));
            }
        }
        return true;
    }
}
