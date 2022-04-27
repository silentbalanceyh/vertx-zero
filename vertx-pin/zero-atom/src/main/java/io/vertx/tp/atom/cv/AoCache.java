package io.vertx.tp.atom.cv;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.modular.apply.AoDefault;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.tp.modular.phantom.AoModeler;
import io.vertx.tp.modular.phantom.AoPerformer;
import io.vertx.up.uca.cache.Cc;

/*
 * 池化处理
 */
public interface AoCache {
    // （设计图上存在）
    /* Model 池化 **/
    Cc<String, Model> CC_MODEL = Cc.open();

    /* OxPerformer资源池（内部） */
    Cc<String, AoPerformer> CC_PERFORMER = Cc.open();
    /* AoIo
     * 由于 AoIo 中存储了相关的数据信息，所以不可以开缓存
     * 如果开了缓存会出现几次操作的记录混用问题
     * */

    // 扩展管理

    /* AoConnection 池化管理 */
    Cc<String, AoConnection> CC_CONNECTION = Cc.open();

    /* AoBuilder 池化管理 */
    Cc<String, AoBuilder> CC_BUILDER = Cc.openThread();

    // （内部）
    /* OxModeler资源池 */
    Cc<String, AoModeler> CC_MODELER = Cc.openThread();

    Cc<String, AoDefault> CC_DEFAULT = Cc.open();
}
