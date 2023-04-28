package io.horizon.util;

/**
 * @author lang : 2023/4/28
 */
class TArray {
    static <T> T[] elementAdd(final T[] array, final T element) {
        final Class<?> type;
        if (array != null) {
            type = array.getClass().getComponentType();
        } else if (element != null) {
            type = element.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        @SuppressWarnings("unchecked") // type must be T
        final T[] newArray = (T[]) HInstance.instanceArray(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }
}
