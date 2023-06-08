package io.mature.extension.quiz.oclick;

import io.horizon.eon.VString;
import io.horizon.eon.em.Environment;
import io.horizon.exception.web._400BadRequestException;
import io.mature.extension.quiz.develop.DevModeller;
import io.mature.extension.quiz.develop.DevSite;
import io.vertx.mod.ke.booter.Bt;
import io.vertx.up.util.Ut;

import static io.vertx.mod.ke.refine.Ke.LOG;

public class InstClick {
    private final transient Class<?> target;

    private InstClick(final Class<?> target) {
        this.target = target;
    }

    public static InstClick instance(final Class<?> target) {
        return new InstClick(target);
    }

    public void runLoad(final String[] args) {
        if (1 > args.length) {
            // 无参数不可执行 HExtension
            throw new _400BadRequestException(this.target);
        }
        // 参数数量必须 > 1
        //   0 - isOob,
        //   1 - inputPath
        final boolean isOob = Boolean.parseBoolean(args[0]);
        final String inputPath = 1 == args.length || Ut.isNil(args[1])
            ? "init/oob" : args[1];
        /*
         *  路径构造，此处路径构造必须是 Production
         *  由于 inst 工具构造的所有信息都是在 nd-app 目录下执行（及IDEA运行时的Work目录）
         *  1）开发路径：src/main/resources/
         *  2）生产路径：target/（运行的PWD路径）
         *  如果此处使用开发路径会出现无法导入数据情况
         */
        final String path = Ut.ioPath(inputPath, Environment.Production);
        LOG.Ke.info(this.target, "加载路径：{0}, 开启OOB：{1}", inputPath, isOob);
        Bt.init(path, VString.EMPTY, isOob);
    }

    public void runAtom(final String[] args) {
        if (1 > args.length) {
            throw new _400BadRequestException(this.target);
        }
        // 0 - module ( cmdb )
        // 1 - absolute path ( PWD )
        final String module = args[0];
        final String path = args[1];
        final String modDir = path + "/app@runtime/@atom/" + module;
        final DevModeller modeller = DevModeller.instance(modDir, modDir);
        modeller.preprocess(() -> {
            LOG.Ke.info(this.target, "「Pre」建模预处理完成！");
            modeller.initialize(() -> {
                LOG.Ke.info(this.target, "「Init」模型初始化完成！");
                System.exit(0);
            });
        });
    }

    public void runMenu(final String[] args) {
        final String inputPath;
        if (0 == args.length) {
            inputPath = "init/map/menu.yml";
        } else {
            inputPath = Ut.isNil(args[0]) ? "init/map/menu.yml" : args[0];
        }
        final String path = Ut.ioPath(inputPath, Environment.Production);
        DevSite.planOn(path).onComplete(res -> {
            if (res.result()) {
                LOG.Ke.info(this.target, "「Menu」菜单规划完成！");
                System.exit(0);
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
