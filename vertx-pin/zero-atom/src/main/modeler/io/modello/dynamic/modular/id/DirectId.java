package io.modello.dynamic.modular.id;

import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.modello.specification.HRecord;
import io.vertx.mod.atom.error._417PrimaryKeyResultException;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Model ->
 *     Entity -> getKey
 */
class DirectId extends AbstractId {

    @Override
    @SuppressWarnings("unchecked")
    public <ID> ID key(final HRecord record,
                       final Model model) {
        // 检查定义
        this.ensure(model);
        // 获取唯一主键值
        final ConcurrentMap<String, Object> keyMap = Ao.joinKeys(model, record);
        // 读取唯一记录中的值
        return (ID) keyMap.keySet().stream()
            .map(keyMap::get).findFirst()
            .orElse(null);
    }

    @Override
    public <ID> void key(final HRecord record,
                         final Model model,
                         final ID id) {
        // 检查定义
        this.ensure(model);
        // 查找唯一的主键
        final String keyField = Ao.joinKey(model);
        if (Ut.isNotNil(keyField)) {
            record.set(keyField, Ao.toKey(id));
        }
    }

    private void ensure(final Model model) {
        final Set<MJoin> joinSet = model.dbJoins();
        /*
         * 一对一的模式，不执行 Join，keyMap的尺寸必须等于 1
         */
        Fn.outWeb(1 != joinSet.size(), _417PrimaryKeyResultException.class, this.getClass(),
            /* ARG1：出现该异常的目标类名 */ this.getClass().getName(),
            /* ARG2：当前实体的主键信息 */ AoId.keyInfo(joinSet));
    }
}
