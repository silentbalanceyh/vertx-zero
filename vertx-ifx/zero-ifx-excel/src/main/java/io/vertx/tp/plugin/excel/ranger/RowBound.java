package io.vertx.tp.plugin.excel.ranger;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;

public class RowBound implements ExBound {
    private final transient int start;
    private final transient int end;

    public RowBound(final Sheet sheet) {
        this(sheet.getFirstRowNum(), sheet.getLastRowNum());
    }

    public RowBound(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RowBound)) {
            return false;
        }
        final RowBound rowBound = (RowBound) o;
        return this.start == rowBound.start &&
            this.end == rowBound.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    @Override
    public String toString() {
        return "RowBound{" +
            "begin=" + this.start +
            ", end=" + this.end +
            '}';
    }

    @Override
    public int getStart() {
        return this.start;
    }

    @Override
    public int getEnd() {
        return this.end;
    }
}
