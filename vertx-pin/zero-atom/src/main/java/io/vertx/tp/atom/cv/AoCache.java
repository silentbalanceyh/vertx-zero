package io.vertx.tp.atom.cv;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jooq.JQEngine;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.tp.modular.phantom.AoModeler;
import io.vertx.tp.modular.phantom.AoPerformer;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.experiment.mixture.HDao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * 池化处理
 */
public interface AoCache {
    // （设计图上存在）
    /* Model 池化 **/
    ConcurrentMap<String, Model> POOL_MODELS = new ConcurrentHashMap<>();
    /* Switcher */
    ConcurrentMap<Integer, Switcher> POOL_SWITCHER = new ConcurrentHashMap<>();
    /* OxPerformer资源池（内部） */
    ConcurrentMap<String, AoPerformer> POOL_PERFORMERS = new ConcurrentHashMap<>();
    /* AoIo
     * 由于 AoIo 中存储了相关的数据信息，所以不可以开缓存
     * 如果开了缓存会出现几次操作的记录混用问题
     * */

    // 扩展管理
    /* AoConnection 池化管理 */
    ConcurrentMap<String, AoConnection> POOL_CONNECTION = new ConcurrentHashMap<>();
    /* AoBuilder 池化管理 */
    ConcurrentMap<String, AoBuilder> POOL_T_BUILDER = new ConcurrentHashMap<>();
    /* AoDao 池化管理 */
    ConcurrentMap<String, HDao> POOL_T_DAO = new ConcurrentHashMap<>();
    /* JqEngine 池化 */
    ConcurrentMap<String, JQEngine> POOL_ENGINES = new ConcurrentHashMap<>();

    // （内部）
    /* OxModeler资源池 */
    ConcurrentMap<String, AoModeler> POOL_MODELER = new ConcurrentHashMap<>();
}
