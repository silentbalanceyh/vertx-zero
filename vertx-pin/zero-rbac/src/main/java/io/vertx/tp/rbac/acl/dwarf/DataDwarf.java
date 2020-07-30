package io.vertx.tp.rbac.acl.dwarf;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500DwarfInstanceNullException;
import io.vertx.tp.rbac.cv.em.RegionType;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.fn.Fn;

/*
 * Dwarf
 */
public interface DataDwarf {

    static DataDwarf create(final RegionType type) {
        if (RegionType.RECORD == type) {
            return Fn.pool(Pool.DWARF_POOL, type, RecordDwarf::new);
        } else if (RegionType.PAGINATION == type) {
            return Fn.pool(Pool.DWARF_POOL, type, PaginationDwarf::new);
        } else if (RegionType.ARRAY == type) {
            return Fn.pool(Pool.DWARF_POOL, type, ArrayDwarf::new);
        } else {
            /*
             * Exception for unsupported type of Dwarf
             */
            Fn.out(true, _500DwarfInstanceNullException.class, DataDwarf.class, type);
            return null;
        }
    }

    void minimize(JsonObject dataReference, JsonObject matrix, Acl acl);
}
