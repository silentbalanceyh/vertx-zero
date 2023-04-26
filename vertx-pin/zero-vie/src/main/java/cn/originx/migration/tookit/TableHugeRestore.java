package cn.originx.migration.tookit;

import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import static cn.originx.refine.Ox.LOG;

public class TableHugeRestore extends AbstractStatic {
    public TableHugeRestore(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        /*
         * 目录创建
         */
        final String targetFolder = this.ioBackup(config, "sql");
        this.tableEmpty(this.jooq.table());
        /*
         * 文件名
         */
        final String file = targetFolder + "/" + this.jooq.table() + ".sql";
        final boolean done = this.restoreTo(file);
        LOG.Shell.info(this.getClass(), "数据还原文件：{0}，执行结果：{1}",
            file, done);
        return Ux.future(config);
    }

    private boolean restoreTo(final String file) {
        final StringBuilder cmd = new StringBuilder();
        final Database database = Database.getCurrent();
        cmd.append("mysql")
            .append(" -h").append(database.getHostname())
            .append(" -u").append(database.getUsername())
            .append(" -p").append(database.getSmartPassword())
            .append(" --default-character-set=utf8 ")
            .append(" ").append(database.getInstance());
        return Fn.orJvm(() -> {
            final File fileObj = Ut.ioFile(file);
            final BasicFileAttributes fileAttributes = Files.readAttributes(fileObj.toPath(), BasicFileAttributes.class);
            if (fileObj.exists() && fileAttributes.isRegularFile()) {
                LOG.Shell.info(this.getClass(), "文件名：{2}，执行命令：{0}，" +
                        "文件长度：{1} MB",
                    cmd.toString(), String.valueOf(fileAttributes.size() / 1024 / 1024), file);
                final Process process = Runtime.getRuntime().exec(cmd.toString());
                /*
                 * 开始时间
                 */
                final long start = System.nanoTime();
                final OutputStream outputStream = process.getOutputStream();
                /* 直接写输出流 */
                Ut.ioOut(file, outputStream);
                final long end = System.nanoTime();
                /* 纳秒 -> 毫秒 */
                final long spend = (end - start) / 1000 / 1000;
                LOG.Shell.info(this.getClass(), "执行完成，耗时 {0} ms！ Successfully",
                    String.valueOf(spend));
                return true;
            } else {
                LOG.Shell.info(this.getClass(), "文件不存在！file = {0}", file);
                return false;
            }
        });
    }
}
