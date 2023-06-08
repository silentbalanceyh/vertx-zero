package io.vertx.mod.rbac.acl.rapid;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.rapid.addon.DwarfAddOn;
import io.vertx.mod.rbac.cv.em.RegionType;
import io.vertx.mod.rbac.error._500DwarfInstanceNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.util.Ut;

/*
 * Dwarf
 */
public interface Dwarf {

    static Dwarf create(final RegionType type) {
        if (RegionType.RECORD == type) {
            return Pool.CC_DWARF.pick(DwarfRecord::new, type);
        } else if (RegionType.PAGINATION == type) {
            return Pool.CC_DWARF.pick(DwarfPagination::new, type);
            //return Fn.po?l(Pool.DWARF_POOL, type, PaginationDwarf::new);
        } else if (RegionType.ARRAY == type) {
            return Pool.CC_DWARF.pick(DwarfArray::new, type);
            //return Fn.po?l(Pool.DWARF_POOL, type, ArrayDwarf::new);
        } else {
            /*
             * Exception for unsupported type of Dwarf
             */
            Fn.out(true, _500DwarfInstanceNullException.class, Dwarf.class, type);
            return null;
        }
    }

    static Dwarf create() {
        return create(DwarfAddOn.class);
    }

    static Dwarf create(final Class<?> clazz) {
        return Pool.CC_ADDON.pick(() -> Ut.instance(clazz), clazz.getName());
    }

    void minimize(JsonObject dataReference, JsonObject matrix, Acl acl);
}
