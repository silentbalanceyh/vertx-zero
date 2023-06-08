package io.vertx.mod.atom.cv;

import io.horizon.uca.cache.Cc;
import io.modello.dynamic.modular.apply.AoDefault;
import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AoBuilder;
import io.modello.dynamic.modular.phantom.AoModeler;
import io.vertx.mod.atom.modeling.Model;

/*
 * 池化处理
 */
public interface AoCache {
    // （设计图上存在）
    /* Model 池化 **/
    Cc<String, Model> CC_MODEL = Cc.open();
    /* AoConnection 池化管理 */
    Cc<String, AoConnection> CC_CONNECTION = Cc.open();

    /* AoBuilder 池化管理 */
    Cc<String, AoBuilder> CC_BUILDER = Cc.openThread();

    // （内部）
    /* OxModeler资源池 */
    Cc<String, AoModeler> CC_MODELER = Cc.openThread();

    Cc<String, AoDefault> CC_DEFAULT = Cc.open();
}
