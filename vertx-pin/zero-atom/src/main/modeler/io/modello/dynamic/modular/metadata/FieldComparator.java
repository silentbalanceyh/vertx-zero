package io.modello.dynamic.modular.metadata;

import cn.vertxup.atom.domain.tables.pojos.MField;

import java.util.Comparator;

public class FieldComparator implements Comparator<MField> {

    /**
     * 排序不能返回0
     **/
    @Override
    public int compare(final MField fieldOne, final MField fieldTwo) {
        final String colOne = fieldOne.getColumnName();
        final String colTwo = fieldTwo.getColumnName();
        if (colOne == null || colTwo == null) {
            return -1;
        }
        if (colOne.length() > colTwo.length()) {
            return 1;
        }
        if (colOne.length() < colTwo.length()) {
            return -1;
        }
        if (colOne.compareTo(colTwo) > 0) {
            return 1;
        }
        if (colOne.compareTo(colTwo) < 0) {
            return -1;
        }
        if (colOne.compareTo(colTwo) == 0) {
            return 0;
        }
        return 0;
    }
}
