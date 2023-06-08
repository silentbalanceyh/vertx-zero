package io.vertx.mod.rbac.acl.region;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.rapid.Dwarf;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.mod.rbac.cv.em.RegionType;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static io.vertx.mod.rbac.refine.Sc.LOG;

class DataOut {

    private static final Annal LOGGER = Annal.get(DataOut.class);

    /*
     * projection on result
     * RegionType.RECORD
     */
    @SuppressWarnings("all")
    static void dwarfRecord(final Envelop envelop, final JsonObject matrix) {
        final Acl acl = envelop.acl();
        final JsonArray projection = Sc.aclOn(matrix.getJsonArray(Ir.KEY_PROJECTION), acl);
        dwarfUniform(envelop, projection, new HashSet<RegionType>() {
            {
                this.add(RegionType.RECORD);
            }
        }, (responseJson, type) -> Dwarf.create(type).minimize(responseJson, matrix, acl));
    }

    /*
     * rows on result
     * RegionType.PAGINATION
     * RegionType.ARRAY
     */
    @SuppressWarnings("all")
    static void dwarfRows(final Envelop envelop, final JsonObject matrix) {
        final JsonObject rows = matrix.getJsonObject("rows");
        dwarfUniform(envelop, rows, new HashSet<RegionType>() {
            {
                this.add(RegionType.ARRAY);
                this.add(RegionType.PAGINATION);
            }
        }, (responseJson, type) -> Dwarf.create(type).minimize(responseJson, matrix, envelop.acl()));
    }

    @SuppressWarnings("all")
    static void dwarfCollection(final Envelop envelop, final JsonObject matrix) {
        final JsonArray prjection = Sc.aclOn(matrix.getJsonArray(Ir.KEY_PROJECTION), envelop.acl());
        dwarfUniform(envelop, prjection, new HashSet<RegionType>() {
            {
                this.add(RegionType.ARRAY);
                this.add(RegionType.PAGINATION);
            }
        }, (responseJson, type) -> Dwarf.create(type).minimize(responseJson, matrix, envelop.acl()));
    }

    static void dwarfAddon(final Envelop envelop, final JsonObject matrix) {
        final JsonObject responseJson = envelop.outJson();
        // Qr
        Dwarf.create().minimize(responseJson, matrix, envelop.acl());
    }

    // --------------------------- Analyze Region ----------------------------
    /*
     * Uniform called by static method for different workflow of region type
     */
    private static <T> void dwarfUniform(final Envelop envelop,
                                         final T hitted,
                                         final Set<RegionType> expected,
                                         final BiConsumer<JsonObject, RegionType> consumer) {
        if (Objects.nonNull(hitted)) {
            Supplier<Boolean> isEmpty = null;
            if (hitted instanceof JsonObject) {
                isEmpty = ((JsonObject) hitted)::isEmpty;
            } else if (hitted instanceof JsonArray) {
                isEmpty = ((JsonArray) hitted)::isEmpty;
            }
            /*
             * Whether supplier is available here for predicate
             */
            if (Objects.nonNull(isEmpty) && !isEmpty.get()) {
                final JsonObject responseJson = envelop.outJson();
                if (Objects.nonNull(responseJson)) {
                    /*
                     * Analyze result for type here.
                     */
                    final RegionType type = analyzeRegion(responseJson);
                    LOG.Auth.info(LOGGER, AuthMsg.REGION_TYPE, type, responseJson.encode());
                    if (expected.contains(type)) {
                        consumer.accept(responseJson, type);
                    }
                }
            }
        }
    }

    /*
     * There are three data format that could be enabled for region.
     * 1. Json Object:
     * {
     *     "data": {}
     * }
     * 2. Pagination
     * {
     *     "data": {
     *         "list": [],
     *         "count": xxx
     *     }
     * }
     * 3. Json Array:
     * {
     *     "data": []
     * }
     */
    private static RegionType analyzeRegion(final JsonObject reference) {
        /* Extract Data Object */
        final Object value = reference.getValue("data");
        if (Objects.nonNull(value)) {
            if (Ut.isJArray(value)) {
                /* value = JsonArray */
                return RegionType.ARRAY;
            } else if (Ut.isJObject(value)) {
                /* Distinguish between Pagination / Object */
                final JsonObject json = (JsonObject) value;
                if (json.containsKey("list") && json.containsKey("count")
                    && VValue.TWO == json.size()) {
                    return RegionType.PAGINATION;
                } else {
                    return RegionType.RECORD;
                }
            } else {
                /* value = Other, Region Disabled */
                return RegionType.FORBIDDEN;
            }
        } else {
            /* value = null , Region Disabled */
            return RegionType.FORBIDDEN;
        }
    }
}
