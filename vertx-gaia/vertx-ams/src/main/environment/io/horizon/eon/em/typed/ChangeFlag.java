package io.horizon.eon.em.typed;

/*
 * Compared results for different usage
 * 1) Two `json` records ( JsonObject )
 * 2) Two `json array` records ( JsonArray )
 * 3) Two `list` records ( List<T> )
 */
public enum ChangeFlag {
    /*
     * It means that there occurs 'UPDATE' operation between
     */
    UPDATE,
    /*
     * It means that there occurs 'ADD' operation between
     */
    ADD,
    /*
     * It means that there occurs 'DELETE' operation between
     */
    DELETE,
    /*
     * Default value for nothing happen
     */
    NONE,
}
