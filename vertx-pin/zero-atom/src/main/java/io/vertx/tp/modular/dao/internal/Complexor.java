package io.vertx.tp.modular.dao.internal;

/**
 * 工具类
 * 1. 带条件复杂更新
 * 2. 带条件复杂删除
 * 主要是带Criteria的查询，执行批量更新、删除专用
 */
public class Complexor extends AbstractUtil<Complexor> {

    private Complexor() {
    }

    public static Complexor create() {
        return new Complexor();
    }
}
