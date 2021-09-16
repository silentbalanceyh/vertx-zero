package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.error._409ModuleConflictException;
import io.vertx.tp.error._409MultiModuleException;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ExcelPre implements Pre {
    private transient final ExcelClient client;

    ExcelPre(final ExcelClient client) {
        this.client = client;
    }

    @Override
    public Future<JsonArray> inJAAsync(final JsonObject data, final IxMod in) {
        final String filename = data.getString(KName.FILE_NAME);
        /* File Checking */
        final File file = new File(filename);
        if (!file.exists() || Objects.isNull(this.client)) {
            return Ux.futureA();
        }

        /* Read file into data table */
        final Kv<String, Set<ExTable>> content = this.readFile(file);
        if (!content.valid()) {
            return Ux.futureA();
        }

        /* Data Processing */
        final KModule module = in.module();
        final String expected = module.getTable();
        final String actual = content.getKey();
        Fn.out(!expected.equals(actual), _409ModuleConflictException.class, this.getClass(), actual, expected);

        /* Tenant Information */
        return this.client.extractAsync(content.getValue());
    }

    private Kv<String, Set<ExTable>> readFile(final File file) {
        final ConcurrentMap<String, Set<ExTable>> tableMap = new ConcurrentHashMap<>();
        final Kv<String, Set<ExTable>> kv = Kv.create();
        try {
            final InputStream stream = new FileInputStream(file);
            final Set<ExTable> tables = this.client.ingest(stream, true);
            /*
             * Filtered the tables that equal module in table
             */
            tables.stream()
                .filter(Objects::nonNull)
                .filter(item -> Objects.nonNull(item.getName()))
                .forEach(item -> {
                    if (!tableMap.containsKey(item.getName())) {
                        tableMap.put(item.getName(), new HashSet<>());
                    }
                    tableMap.get(item.getName()).add(item);
                });
            Fn.out(1 != tableMap.size(), _409MultiModuleException.class, this.getClass(), tableMap.size());
            final String tableName = tableMap.keySet().iterator().next();
            kv.set(tableName, tableMap.get(tableName));
            return kv;
        } catch (final IOException ex) {
            ex.printStackTrace();
            throw new _500InternalServerException(this.getClass(), ex.getMessage());
        }
    }
}
