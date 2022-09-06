package cn.originx.migration.backup;

import cn.originx.migration.AbstractStep;
import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.File;
import java.util.Objects;

public class EnvPath extends AbstractStep {

    public EnvPath(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        return Fn.orJvm(() -> {
            this.banner("001.2 初始化目录");
            Ox.Log.infoShell(this.getClass(), "输出目录参数：output = {0}", config.getString("output"));
            final String folder = this.ioRoot(config);

            Pool.FOLDERS.stream().map(each -> folder + each).forEach(this::mkdir);
            return Ux.future(config);
        });
    }

    private void mkdir(final String folder) {
        final File file = Ut.ioFile(folder);
        if (Objects.isNull(file)) {
            final File created = new File(folder);
            final boolean isOk = created.mkdirs();
            Ox.Log.infoShell(this.getClass(), "创建目录：{0}, created = {1}",
                created.getAbsolutePath(), isOk);
        } else {
            Ox.Log.infoShell(this.getClass(), "目录存在：{0}，跳过创建", folder);
        }
    }
}
