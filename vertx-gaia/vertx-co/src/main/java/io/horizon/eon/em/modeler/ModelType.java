package io.horizon.eon.em.modeler;

public enum ModelType {
    DIRECT,       // 直接模型（和数据表1对1处理，默认为DIRECT）
    VIEW,         // 视图模型（后期可以和数据库中的视图绑定）
    JOINED,       // 连接模型，和视图模型类似，但不绑定数据库中视图，直接做连接（自然连接）
    READONLY,     // 只读模型
}
