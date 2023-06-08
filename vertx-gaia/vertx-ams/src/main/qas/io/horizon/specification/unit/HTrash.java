package io.horizon.specification.unit;

/**
 * 「回收站」Trash
 * <hr/>
 * 回收站组件专用应用，用于处理本地分支开发的回收站存储删除部分的内容，方便处理过程中可回滚
 * 由于本地库中不包含详细的远程可同步版本信息，所以此处依赖回收站用于备份和还原
 *
 * @author lang : 2023-05-20
 */
public interface HTrash {
    /**
     * 回收站存储路径
     *
     * @return {@link String}
     */
    String path();
}
