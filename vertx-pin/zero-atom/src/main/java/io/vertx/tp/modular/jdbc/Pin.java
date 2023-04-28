package io.vertx.tp.modular.jdbc;

import io.horizon.specification.modeler.HDao;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._501PinNotFoundException;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

/**
 * 统一读取组件的接口，新版的数据访问层在 yml 文件中仅定义 Pin 插件就可以了，
 * 其他所有插件通过 Pin 来实现门面转化的动作，包括读取其他组件的应用都透过 Pin来完成，
 * Pin 中还可以检查数据库连接。
 */
public interface Pin {

    /**
     * 根据配置文件读取连接器
     */
    static Pin getInstance() {
        final Class<?> clazz = Ao.pluginPin();
        final Annal logger = Annal.get(Pin.class);
        Fn.outWeb(null == clazz, logger, _501PinNotFoundException.class, clazz, "implPin");
        return Ut.singleton(clazz);
    }

    /**
     * 读取发布器专用接口
     * 发布器执行时，必须知道是针对哪个Database进行发布
     */
    AoBuilder getBuilder(Database database);

    /**
     * 读取数据处理访问器专用接口
     * 用于Crud等各种复杂操作
     */
    HDao getDao(Database database);
}
