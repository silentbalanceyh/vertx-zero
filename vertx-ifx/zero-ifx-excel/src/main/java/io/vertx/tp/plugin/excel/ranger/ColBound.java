package io.vertx.tp.plugin.excel.ranger;

import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

public class ColBound implements ExBound {
    private transient int start;
    private transient int end;

    public ColBound(final Row row) {
        this(row.getFirstCellNum(), row.getLastCellNum());
    }

    public ColBound(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColBound)) {
            return false;
        }
        final ColBound colBound = (ColBound) o;
        return this.start == colBound.start &&
                this.end == colBound.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    @Override
    public String toString() {
        return "ColBound{" +
                "begin=" + this.start +
                ", end=" + this.end +
                '}';
    }

    @Override
    public int getStart() {
        return this.start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    @Override
    public int getEnd() {
        return this.end;
    }

    public void setEnd(final int end) {
        this.end = end;
    }
}
