package io.horizon.uca.fs;

import io.horizon.eon.VMessage;
import io.horizon.fn.HFn;
import io.horizon.specification.storage.HFS;
import io.horizon.uca.log.LogAs;
import io.horizon.util.HUt;
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
            LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_MKDIR, file.getAbsolutePath());
            HFn.jvmAt(() -> FileUtils.forceMkdir(file));
        }
        return true;
    }

    @Override
    public boolean rm(final String filename) {
        final File file = HUt.ioFile(filename);
        if (Objects.nonNull(file) && file.exists()) {
            LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_RM, file.getAbsolutePath());
            HFn.jvmAt(() -> FileUtils.forceDelete(file));
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
                    LogAs.Fs.info(this.getClass(),
                        VMessage.HFS.IO_CMD_CP, nameFrom, nameTo, "copyFileToDirectory");
                    HFn.jvmAt(() -> FileUtils.copyFileToDirectory(fileSrc, fileDst, true));
                } else {
                    if (fileSrc.getName().equals(fileDst.getName())) {
                        // Directory -> Directory ( Overwrite )
                        LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_CP, nameFrom, nameTo, "copyDirectory");
                        HFn.jvmAt(() -> FileUtils.copyDirectory(fileSrc, fileDst, true));
                    } else {
                        // Directory -> Directory / ( Children )
                        LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_CP, nameFrom, nameTo, "copyDirectoryToDirectory");
                        HFn.jvmAt(() -> FileUtils.copyDirectoryToDirectory(fileSrc, fileDst));
                    }
                }
            } else {
                // File -> File
                if (fileSrc.isFile()) {
                    LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_CP, nameFrom, nameTo, "copyFile");
                    HFn.jvmAt(() -> FileUtils.copyFile(fileSrc, fileDst, true));
                }
            }
        } else {
            LogAs.Fs.warn(this.getClass(), VMessage.HFS.ERR_CMD_CP, nameFrom, nameTo);
        }
        return true;
    }

    @Override
    public boolean rename(final String nameFrom, final String nameTo) {
        final File fileSrc = new File(nameFrom);
        if (fileSrc.exists()) {
            final File fileTo = new File(nameTo);
            final File fileToP = fileTo.getParentFile();
            LogAs.Fs.info(this.getClass(), VMessage.HFS.IO_CMD_MOVE, fileSrc.getAbsolutePath(), fileToP.getAbsolutePath());
            if (fileSrc.isDirectory()) {
                // 目录拷贝：目录 -> 目录
                HFn.jvmAt(() -> FileUtils.moveDirectory(fileSrc, fileTo));
            } else {
                // 文件拷贝（替换原始文件）
                if (HUt.ioExist(nameTo)) {
                    // Fix: org.apache.commons.io.FileExistsException:
                    //      File element in parameter 'null' already exists:
                    this.rm(nameTo);
                }
                HFn.jvmAt(() -> FileUtils.moveFile(fileSrc, fileTo, StandardCopyOption.REPLACE_EXISTING));
            }
        }
        return true;
    }
}
