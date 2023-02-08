package io.vertx.aeon.kit;

import cn.originx.quiz.develop.DevKit;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.exception.web._400BadRequestException;
import io.vertx.up.util.Ut;

public class DevLoader {
    public static void main(String[] args) {
        if(1 > args.length){
            // 无参数不可执行 HLoader
            throw new _400BadRequestException(DevLoader.class);
        }
        // 参数数量必须 > 1
        //   0 - isOob,
        //   1 - inputPath
        final boolean isOob = Boolean.parseBoolean(args[0]);
        final String inputPath = Ut.isNil(args[1]) ? "init/oob": args[1];
        // 路径构造
        final String path = Ut.ioPath(inputPath, Environment.Development);
        DevKit.oobLoader(path, isOob);
    }
}