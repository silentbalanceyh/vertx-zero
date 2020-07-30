package io.vertx.tp.rbac.acl.region;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.acl.dwarf.DataDwarf;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.cv.em.RegionType;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

class DataOut {

    private static final Annal LOGGER = Annal.get(DataOut.class);

    /*
     * projection on result
     * RegionType.RECORD
     */
    @SuppressWarnings("all")
    static void dwarfRecord(final Envelop envelop, final JsonObject matrix) {
        final Acl acl = envelop.acl();
        final JsonArray projection = Sc.aclProjection(matrix.getJsonArray(Inquiry.KEY_PROJECTION), acl);
        dwarfUniform(envelop, projection, new HashSet<RegionType>() {
            {
                this.add(RegionType.RECORD);
            }
        }, (responseJson, type) -> DataDwarf.create(type).minimize(responseJson, matrix, acl));

        if (Objects.nonNull(acl)) {
            final JsonObject aclData = acl.aclData();
            if (Ut.notNil(aclData)) {
                envelop.attach("acl", aclData);
            }
        }
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
        }, (responseJson, type) -> DataDwarf.create(type).minimize(responseJson, matrix, envelop.acl()));
    }

    @SuppressWarnings("all")
    static void dwarfCollection(final Envelop envelop, final JsonObject matrix) {
        final JsonArray prjection = Sc.aclProjection(matrix.getJsonArray(Inquiry.KEY_PROJECTION), envelop.acl());
        dwarfUniform(envelop, prjection, new HashSet<RegionType>() {
            {
                this.add(RegionType.ARRAY);
                this.add(RegionType.PAGINATION);
            }
        }, (responseJson, type) -> DataDwarf.create(type).minimize(responseJson, matrix, envelop.acl()));
    }

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
                    Sc.infoAuth(LOGGER, AuthMsg.REGION_TYPE, type, responseJson.encode());
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
                        && Values.TWO == json.size()) {
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
