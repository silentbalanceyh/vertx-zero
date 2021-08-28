package io.vertx.tp.plugin.excel;

import io.vertx.tp.plugin.excel.atom.ExConnect;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, Workbook> WORKBOOKS
        = new ConcurrentHashMap<>();

    ConcurrentMap<Integer, Workbook> WORKBOOKS_STREAM
        = new ConcurrentHashMap<>();

    ConcurrentMap<String, ExcelHelper> HELPERS
        = new ConcurrentHashMap<>();

    ConcurrentMap<String, ExConnect> CONNECTS
        = new ConcurrentHashMap<>();
}
