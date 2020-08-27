package io.vertx.tp.plugin.excel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.tp.plugin.excel.atom.ExTable;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SheetIngest {
    private transient final ExcelHelper helper;

    private SheetIngest(final ExcelHelper helper) {
        this.helper = helper;
    }

    static SheetIngest create(final ExcelHelper helper) {
        return new SheetIngest(helper);
    }

    /*
     * 1. Get Workbook reference
     * 2. Iterator for Sheet ( By Analyzer )
     */
    Set<ExTable> ingest(final InputStream in, final boolean isXlsx) {
        final Workbook workbook = this.helper.getWorkbook(in, isXlsx);
        return this.helper.getExTables(workbook);
    }

    Set<ExTable> ingest(final String filename) {
        final Workbook workbook = this.helper.getWorkbook(filename);
        return this.helper.getExTables(workbook);
    }

    void ingest(final String filename, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(Future.succeededFuture(this.ingest(filename)));
    }

    void ingest(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(Future.succeededFuture(this.ingest(in, isXlsx)));
    }

    <T> Handler<AsyncResult<Set<ExTable>>> ingestAsync(final String tableOnly,
                                                       final Handler<AsyncResult<Set<ExTable>>> handler) {
        return processed -> {
            if (processed.succeeded()) {
                /* Filtered valid table here */
                final Set<ExTable> execution = this.getFiltered(tableOnly, processed.result());
                handler.handle(Future.succeededFuture(execution));
            }
        };
    }

    private Set<ExTable> getFiltered(final String tableOnly, final Set<ExTable> processed) {
        return processed.stream()
                .filter(table -> tableOnly.equals(table.getName()))
                .collect(Collectors.toSet());
    }
}
