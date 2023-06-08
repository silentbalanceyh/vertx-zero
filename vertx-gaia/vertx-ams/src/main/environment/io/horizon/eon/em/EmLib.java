package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmLib {
    private EmLib() {
    }

    /**
     * @author lang : 2023-05-20
     */
    public enum EditorType {
        DESIGNER_WORKFLOW,   // 流程设计器
        DESIGNER_REPORT,     // 报表设计器
        DESIGNER_FORM,       // 表单设计器

        MODELER_COMMON,      // 建模设计器
        MODELER_GRAPH,       // 图建模设计器
        MODELER_HARDWARE,    // 硬件建模器

        EDITOR_SCRIPT,       // 脚本编辑器
        EDITOR_RULE,         // 规则编辑器
        EDITOR_SERVICE,      // 接口编辑器
        EDITOR_TASK,         // 任务编辑器
    }

    /**
     * @author lang : 2023-05-20
     */
    public enum LibraryType {
        INTERNAL,   // 内部系统库
        EXTERNAL,   // 外部系统库
        INFIX,      // 内部插件
        PLUGIN,     // 外部插件
    }
}
