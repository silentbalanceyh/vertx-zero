package cn.originx.refine;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.InputStream;
import java.util.List;

/**
 * ## Shell命令工具类
 *
 * ### 1. 基本介绍
 *
 * Shell命令工具类。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxConsole {

    /**
     * Zero专用日志器
     */
    private static final Annal LOGGER = Annal.get(OxConsole.class);

    /*
     * 私有构造函数（工具类转换）
     */
    private OxConsole() {
    }

    /**
     * 「Async」ElasticSearch异步执行器，重建索引。
     *
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link Future}<{@link ElasticSearchClient}>
     */
    static Future<ElasticSearchClient> runEs(final DataAtom atom) {
        final ElasticSearchClient client = ElasticSearchInfix.getClient();
        final String identifier = atom.identifier();
        Sl.output("标识：{0}", identifier);
        try {
            client.deleteIndex(identifier);
            client.createIndex(identifier, atom.type());
        } catch (final Throwable ex) {
            ex.printStackTrace();
            Sl.failWarn("当前索引不存在：identifier = {0}, details", identifier, ex.getMessage());
        }
        return Ux.future(client);
    }

    /**
     * 命令执行器，批量调用操作系统中的命令提示符运行操作命令。
     *
     * @param commands {@link List}<{@link String}> 待执行的命令清单
     */
    static void runCmd(final List<String> commands) {

        try {
            final Process process = Runtime.getRuntime().exec(Ut.fromJoin(commands, " "));

            final int waited = process.waitFor();
            final int exit = process.exitValue();
            if (0 == exit) {
                final InputStream in = process.getInputStream();
                final String content = Ut.ioString(in, "\n");
                System.out.println(content.replaceAll("#\\[", "\n#["));
                Sl.output("执行完成，执行结果：{0}", 0 == waited ? "SUCCESS" : "FAILURE");
            } else {
                final InputStream in = process.getErrorStream();
                final String content = Ut.ioString(in);
                System.err.println(content.replaceAll("at ", "\n\tat "));
            }
        } catch (final Exception ex) {
            Ox.Log.warnShell(LOGGER, "执行出错！ex = {0}", ex.getMessage());
        }
    }
}
