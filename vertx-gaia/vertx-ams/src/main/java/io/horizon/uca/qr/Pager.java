package io.horizon.uca.qr;

import io.horizon.exception.web._400QPagerIndexException;
import io.horizon.exception.web._400QPagerInvalidException;
import io.horizon.exception.web._500QQueryMetaNullException;
import io.horizon.fn.HFn;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.syntax.Ir;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

public class Pager implements Serializable {

    private static final Annal LOGGER = Annal.get(Pager.class);
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    /**
     * Start page: >= 1
     */
    private int page;
    /**
     * Page size
     */
    private int size;
    /**
     * From index: offset
     */
    private int start;
    /**
     * To index: limit
     */
    private int end;

    private Pager(final Integer page, final Integer size) {
        this.init(page, size);
    }

    private Pager(final JsonObject pageJson) {
        this.ensure(pageJson);
        this.init(pageJson.getInteger(PAGE), pageJson.getInteger(SIZE));
    }

    /**
     * Create pager by page, size
     *
     * @param page page index + 1
     * @param size page size
     *
     * @return valid Pager of new
     */
    public static Pager create(final Integer page, final Integer size) {
        return new Pager(page, size);
    }

    /**
     * Another mode to create Pager
     *
     * @param pageJson parsed pager
     *
     * @return valid Pager
     */
    public static Pager create(final JsonObject pageJson) {
        return new Pager(pageJson);
    }

    @SuppressWarnings("all")
    private void ensure(final JsonObject pageJson) {
        // Pager building checking
        HFn.outWeb(null == pageJson, LOGGER,
            _500QQueryMetaNullException.class, this.getClass());
        // Required
        HFn.outWeb(!pageJson.containsKey(PAGE), LOGGER,
            _400QPagerInvalidException.class, this.getClass(), PAGE);
        HFn.outWeb(!pageJson.containsKey(SIZE), LOGGER,
            _400QPagerInvalidException.class, this.getClass(), SIZE);
        // Types
        Ir.ensureType(pageJson, PAGE, Integer.class,
            HUt::isInteger, this.getClass());
        Ir.ensureType(pageJson, SIZE, Integer.class,
            HUt::isInteger, this.getClass());
    }

    private void init(final Integer page, final Integer size) {
        // Page/Size
        HFn.outWeb(1 > page, LOGGER,
            _400QPagerIndexException.class, this.getClass(), page);
        this.page = page;
        // Default Size is 10
        this.size = 0 < size ? size : 10;
        HFn.runAt(() -> {
            // Caculate
            this.start = (this.page - 1) * this.size;
            this.end = this.page * this.size;
        }, this.page, this.size);
    }

    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.put(PAGE, this.page);
        data.put(SIZE, this.size);
        return data;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "Pager{" +
            "page=" + this.page +
            ", size=" + this.size +
            ", start=" + this.start +
            ", end=" + this.end +
            '}';
    }
}
