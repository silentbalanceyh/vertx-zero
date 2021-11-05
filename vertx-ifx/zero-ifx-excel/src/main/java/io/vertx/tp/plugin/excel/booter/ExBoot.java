package io.vertx.tp.plugin.excel.booter;

import io.vertx.tp.plugin.excel.atom.ExConnect;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Data Booting for configuration
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ExBoot {
    /*
     *  table = configuration
     *  vertx-excel.yml splitting
     */
    ConcurrentMap<String, ExConnect> configure();

    List<String> oob();
}
