package io.horizon.specification.storage;

import io.horizon.runtime.cache.CStore;
import io.horizon.uca.fs.LocalFs;
import io.vertx.core.Future;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「抽象文件系统接口」File Store
 * <hr/>
 * 可搭载不同的文件读取内容，实现完整的文件系统模式，用于存储结构搭建，该接口负责：
 * <pre><code>
 *     1. 文件操作集
 *     2. 目录操作集
 * </code></pre>
 * 直接使用抽象文件接口服务，将目录和文件合并到一个 HSF 中实现存储方案，历史原因，不赘述
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HFS extends HStore {
    static HFS common() {
        return CStore.CCT_FS.pick(LocalFs::new);
    }

    /* 创建目录：    mkdir -p            */
    boolean mkdir(String dirName);

    default boolean mkdir(final Set<String> dirName) {
        dirName.forEach(this::mkdir);
        return true;
    }

    default Future<Boolean> mkdirAsync(final Set<String> dirName) {
        return Future.succeededFuture(this.mkdir(dirName));
    }

    default Future<Boolean> mkdirAsync(final String dirName) {
        return Future.succeededFuture(this.mkdir(dirName));
    }

    /* 删除所有：    rm -rf              */
    boolean rm(String filename);

    default Future<Boolean> rmAsync(final String filename) {
        return Future.succeededFuture(this.rm(filename));
    }

    default boolean rm(final Set<String> filenameSet) {
        filenameSet.forEach(this::rm);
        return true;
    }

    default Future<Boolean> rmAsync(final Set<String> filenameSet) {
        return Future.succeededFuture(this.rm(filenameSet));
    }

    /* 重命名：      mv from to          */
    boolean rename(String nameFrom, String nameTo);

    default Future<Boolean> renameAsync(final String nameFrom, final String nameTo) {
        return Future.succeededFuture(this.rename(nameFrom, nameTo));
    }

    default boolean rename(final ConcurrentMap<String, String> renameMap) {
        renameMap.forEach(this::rename);
        return true;
    }

    default Future<Boolean> renameAsync(final ConcurrentMap<String, String> renameMap) {
        return Future.succeededFuture(this.rename(renameMap));
    }

    /* 拷贝： cp */
    boolean cp(String nameFrom, String nameTo);

    default Future<Boolean> cpAsync(final String nameFrom, final String nameTo) {
        return Future.succeededFuture(this.cp(nameFrom, nameTo));
    }
}
