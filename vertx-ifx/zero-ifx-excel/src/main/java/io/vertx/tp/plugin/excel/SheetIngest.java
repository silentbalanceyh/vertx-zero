package io.vertx.tp.plugin.excel;

import io.vertx.core.Future;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.commune.element.Shape;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
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
        return this.ingest(in, isXlsx, Shape.create());
    }

    Set<ExTable> ingest(final String filename) {
        return this.ingest(filename, Shape.create());
    }

    Set<ExTable> ingest(final InputStream in, final boolean isXlsx, final Shape shape) {
        final Workbook workbook = this.helper.getWorkbook(in, isXlsx);
        return this.helper.getExTables(workbook, shape);
    }

    Set<ExTable> ingest(final String filename, final Shape shape) {
        final Workbook workbook = this.helper.getWorkbook(filename);
        return this.helper.getExTables(workbook, shape);
    }

    private Set<ExTable> compress(final Set<ExTable> processed, final String... includes) {
        final Set<String> tables = new HashSet<>(Arrays.asList(includes));
        return processed.stream()
                .filter(table -> tables.contains(table.getName()))
                .collect(Collectors.toSet());
    }

    Future<Set<ExTable>> compressAsync(final Set<ExTable> processed, final String... includes) {
        return Future.succeededFuture(this.compress(processed, includes));
    }
}
