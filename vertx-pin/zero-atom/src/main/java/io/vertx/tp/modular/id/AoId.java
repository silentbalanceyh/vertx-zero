package io.vertx.tp.modular.id;

import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.vertx.tp.atom.cv.em.IdMode;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.up.commune.Record;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 主键专用处理器
 */
public interface AoId {


    /*
     * 根据主键模式读取 Sole 接口
     */
    static AoId get(final IdMode mode) {
        return Pool.POOL_ID.get(mode);
    }

    static <T> String keyInfo(final ConcurrentMap<String, T> keyMap) {
        final StringBuilder keyInfo = new StringBuilder();
        keyMap.forEach((key, value) ->
            keyInfo.append(key).append("=").append(value).append(","));
        return keyInfo.toString();
    }

    static <T> String keyInfo(final Set<MJoin> joins) {
        final StringBuilder keyInfo = new StringBuilder();
        joins.forEach(item ->
            keyInfo.append(item.getEntityKey()).append("=").append(item.getEntity()).append(","));
        return keyInfo.toString();
    }

    /*
     * 读取主键信息时专用
     * 读取主键信息，这个接口是为 Record 中读取主键量身定制的
     * 直接从 Record 中读取主键信息
     */
    <ID> ID key(Record record, Model model);

    /*
     * 设置记录中的主键信息
     */
    <ID> void key(Record record, Model model, ID id);

    /*
     * 同步数据
     * 1. 将 Record 的数据写入到 keys 中（由于牵涉主键策略，所以需要使用 Sole）
     * 2. 将 Record 的数据写入到 matrix 中（牵涉主键策略，所以需要使用 Sole）
     */
    void connect(Record record,
                 ConcurrentMap<String, DataMatrix> keys,
                 ConcurrentMap<String, DataMatrix> matrix,
                 Set<MJoin> joins);
}
