package io.vertx.tp.fm.refine;

import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.tp.ke.atom.KSpec;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FmBook {

    static List<FBook> umBook(final KSpec spec, final List<FBook> books) {
        Objects.requireNonNull(books);
        // Read the major book
        final FBook found = books.stream().filter(FBook::getMajor).findFirst().orElse(null);
        if (Objects.isNull(found)) {
            // The major book does not exist
            Fm.Log.warnBook(FmBook.class, "Book major could not be found, check workflow! ");
            return new ArrayList<>();
        } else {
            // Sub Book Building
            final List<FBook> subBooks = new ArrayList<>();
            int start;
            if (1 < books.size()) {
                start = books.size();
            } else {
                // Main book has been created once, there is major book in `books`
                start = 1;
                subBooks.add(found);
            }
            // Calculate the left books
            final Set<String> existing = books.stream()
                .filter(book -> !book.getMajor())
                .map(FBook::getModelKey).collect(Collectors.toSet());
            final Set<String> created = Ut.diff(spec.getQrKeys(), existing);
            // Set sub book serial number start point
            for (final String modelKey : created) {
                final String serial = found.getSerial() + "-" + start;
                final FBook book = umBook(found, serial);
                book.setModelKey(modelKey);
                // Append
                subBooks.add(book);
                start++;
            }
            return subBooks;
        }
    }

    private static FBook umBook(final FBook book, final String serial) {
        final FBook created = new FBook();
        Ke.umCreated(created, book);
        /*
         * type
         * modelId
         * orderId
         */
        created.setType(book.getType());
        created.setModelId(book.getModelId());
        created.setOrderId(book.getOrderId());
        created.setAmount(BigDecimal.ZERO);
        created.setMajor(Boolean.FALSE);
        created.setParentId(book.getKey());
        // serial, code
        created.setSerial(serial);
        created.setCode(serial);
        created.setStatus(FmCv.Status.PENDING);
        return created;
    }

    static FBook umBook(final KSpec spec) {
        final FBook created = new FBook();
        Ke.umCreated(created, spec);
        /*
         * type
         * modelId
         * modelKey - null
         * orderId - reference
         */
        created.setType(spec.getType());
        created.setModelId(spec.getIdentifier());
        created.setOrderId(spec.getReference());
        /*
         * code, serial
         */
        created.setSerial(spec.getSerial());
        created.setCode(spec.getCode());
        created.setAmount(BigDecimal.ZERO);
        created.setMajor(Boolean.TRUE);
        created.setStatus(FmCv.Status.PENDING);
        return created;
    }

    static JsonObject qrBook(final KSpec spec) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.TYPE, spec.getType());
        condition.put(KName.MODEL_ID, spec.getIdentifier());
        if (spec.multiple()) {
            condition.put(KName.MODEL_KEY + ",i", Ut.toJArray(spec.getQrKeys()));
        } else {
            condition.put(KName.MODEL_KEY, spec.getModelKey());
        }
        condition.put("orderId", spec.getReference());
        return condition;
    }
}
