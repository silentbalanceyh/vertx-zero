package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.commune.element.TypeField;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.UxJooq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxType {

    static TypeAtom atom(final IxMod active, final JsonArray columns) {
        final ConcurrentMap<String, String> headers = new ConcurrentHashMap<>();
        columns.stream().map(Ix::onColumn).filter(Objects::nonNull).forEach(kv -> {
            /* Calculated */
            headers.put(kv.getKey(), kv.getValue());
        });
        /*
         * First module for calculation
         */
        final TypeAtom atom = TypeAtom.create();
        final KModule module = active.module();
        final List<TypeField> fieldList = new ArrayList<>();

        final KModule connect = active.connect();
        if (Objects.nonNull(connect)) {
            fieldList.addAll(field(connect, active.envelop(), headers));
        }
        fieldList.addAll(field(module, active.envelop(), headers));

        fieldList.forEach(atom::add);
        return atom;
    }

    private static List<TypeField> field(final KModule module, final Envelop envelop,
                                         final ConcurrentMap<String, String> headerMap) {
        final UxJooq jooq = IxPin.jooq(module, envelop);
        final JqAnalyzer analyzer = jooq.analyzer();
        final ConcurrentMap<String, Class<?>> typeMap = analyzer.types();
        /*
         * Processing for TypeField list building
         */
        final List<TypeField> fieldList = new ArrayList<>();
        headerMap.forEach((field, alias) -> {
            final Class<?> type = typeMap.getOrDefault(field, String.class);
            fieldList.add(TypeField.create(field, alias, type));
        });
        return fieldList;
    }
}
