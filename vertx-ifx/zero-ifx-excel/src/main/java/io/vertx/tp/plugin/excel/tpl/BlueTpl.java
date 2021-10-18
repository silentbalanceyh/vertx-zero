package io.vertx.tp.plugin.excel.tpl;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.tp.plugin.excel.ExTpl;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.Values;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * 风格插件，直接为 sheet 增加 Style 相关信息
 */
public class BlueTpl implements ExTpl {
    private transient BlueDye dye;

    @Override
    @Fluent
    public ExTpl bind(final Workbook workbook) {
        this.dye = BlueDye.get(workbook);
        return this;
    }

    @Override
    public void applyStyle(final Sheet sheet, final TypeAtom TypeAtom) {
        /*
         * 读取可见区域
         */
        if (Objects.nonNull(sheet)) {
            /*
             * 处理第一行
             */
            final Row first = sheet.getRow(Values.IDX);
            this.applyFirst(first);
            final int dataStart;
            if (TypeAtom.isComplex()) {
                /*
                 * 处理 Title 行
                 */
                final Row cnHeader = sheet.getRow(Values.ONE);
                final Row enHeader = sheet.getRow(Values.THREE);
                this.applyHeader(cnHeader, enHeader);
                /*
                 * 处理第二 Title 行
                 */
                final Row cnHeader1 = sheet.getRow(Values.TWO);
                final Row enHeader1 = sheet.getRow(Values.FOUR);
                this.applyHeader(cnHeader1, enHeader1);
                dataStart = 5;
            } else {
                /*
                 * 处理 Title 行
                 */
                final Row cnHeader = sheet.getRow(Values.ONE);
                final Row enHeader = sheet.getRow(Values.TWO);
                this.applyHeader(cnHeader, enHeader);
                dataStart = 3;
            }
            /*
             * 处理数据行
             */
            final int num = sheet.getPhysicalNumberOfRows();
            for (int idx = dataStart; idx < num; idx++) {
                final Row data = sheet.getRow(idx);
                this.applyData(data, TypeAtom);
            }
        }
    }

    private void applyFirst(final Row row) {
        // {Table}
        final Cell table = row.getCell(Values.IDX);
        this.dye.onTable(table);
        // identifier
        final Cell identifier = row.getCell(Values.ONE);
        this.dye.onModel(identifier);
        // Empty
        final Cell empty = row.getCell(Values.TWO);
        this.dye.onEmpty(empty);
    }

    private void applyHeader(final Row cnHeader, final Row enHeader) {
        // Header for CN text
        final int cells = cnHeader.getPhysicalNumberOfCells();
        for (int idx = 0; idx < cells; idx++) {
            final Cell cell = cnHeader.getCell(idx);
            this.dye.onCnHeader(cell);
        }
        // Header for En text
        final int enCells = enHeader.getPhysicalNumberOfCells();
        for (int idx = 0; idx < enCells; idx++) {
            final Cell cell = enHeader.getCell(idx);
            this.dye.onEnHeader(cell);
        }
    }

    private void applyData(final Row dataRow, final TypeAtom TypeAtom) {
        final int enCells = dataRow.getPhysicalNumberOfCells();
        for (int idx = 0; idx < enCells; idx++) {
            final Cell cell = dataRow.getCell(idx);
            final Class<?> type = TypeAtom.type(idx);
            this.dye.onData(cell, type);
        }
    }
}
