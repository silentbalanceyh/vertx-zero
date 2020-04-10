package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._400PagerInvalidException;
import io.vertx.up.exception.web._500QueryMetaNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;

public class Pager implements Serializable {

    private static final Annal LOGGER = Annal.get(Pager.class);
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    /**
     * Start page: >= 1
     */
    private transient int page;
    /**
     * Page size
     */
    private transient int size;
    /**
     * From index: offset
     */
    private transient int start;
    /**
     * To index: limit
     */
    private transient int end;

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
     * @return valid Pager of new
     */
    public static Pager create(final Integer page, final Integer size) {
        return new Pager(page, size);
    }

    /**
     * Another mode to create Pager
     *
     * @param pageJson parsed pager
     * @return valid Pager
     */
    public static Pager create(final JsonObject pageJson) {
        return new Pager(pageJson);
    }

    @SuppressWarnings("all")
    private void ensure(final JsonObject pageJson) {
        // Pager building checking
        Fn.outWeb(null == pageJson, LOGGER,
                _500QueryMetaNullException.class, this.getClass());
        // Required
        Fn.outWeb(!pageJson.containsKey(PAGE), LOGGER,
                _400PagerInvalidException.class, this.getClass(), PAGE);
        Fn.outWeb(!pageJson.containsKey(SIZE), LOGGER,
                _400PagerInvalidException.class, this.getClass(), SIZE);
        // Types
        Inquiry.ensureType(pageJson, PAGE, Integer.class,
                Ut::isInteger, this.getClass());
        Inquiry.ensureType(pageJson, SIZE, Integer.class,
                Ut::isInteger, this.getClass());
    }

    private void init(final Integer page, final Integer size) {
        // Page/Size
        Fn.outWeb(1 > page, LOGGER,
                _400PagerInvalidException.class, this.getClass(), page);
        this.page = page;
        // Default Size is 10
        this.size = 0 < size ? size : 10;
        Fn.safeNull(() -> {
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
