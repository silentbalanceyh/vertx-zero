package io.vertx.tp.ke.refine;

import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeUser {

    private static final Set<String> FIELDS = new HashSet<>() {
        {
            this.add(KName.SIGMA);
            this.add(KName.LANGUAGE);
            this.add(KName.ACTIVE);
            this.add(KName.CREATED_AT);
            this.add(KName.CREATED_BY);
            this.add(KName.UPDATED_AT);
            this.add(KName.UPDATED_BY);
        }
    };

    static <T, I> void audit(final I output, final String outPojo, final T input, final String inPojo, final boolean isUpdated) {
        // If contains pojo, must be deserialized for auditor information
        // OutMap Calculation
        final ConcurrentMap<String, String> outMap = buildMap(outPojo, isUpdated);
        final ConcurrentMap<String, String> inMap = buildMap(inPojo, isUpdated);
        /* Mapping */
        final LocalDateTime now = LocalDateTime.now();
        outMap.forEach((key, out) -> {
            final String in = inMap.get(key);
            if (KName.CREATED_AT.equals(in) || KName.UPDATED_AT.equals(in)) {
                // LocalDataTime
                Ut.field(output, out, now);
            } else {
                // Copy Data
                Ut.field(output, out, Ut.field(input, in));
            }
        });
    }

    private static ConcurrentMap<String, String> buildMap(final String filename, final boolean isUpdated) {
        final ConcurrentMap<String, String> vector = new ConcurrentHashMap<>();
        if (Ut.isNil(filename)) {
            FIELDS.forEach(each -> vector.put(each, each));
        } else {
            final Mojo outMojo = Mirror.create(KeUser.class).mount(filename).mojo();
            outMojo.getIn().forEach((in, out) -> {
                if (FIELDS.contains(in)) {
                    vector.put(in, out);
                }
            });
        }
        if (isUpdated) {
            vector.remove(KName.CREATED_BY);
            vector.remove(KName.CREATED_AT);
        }
        return vector;
    }
}
