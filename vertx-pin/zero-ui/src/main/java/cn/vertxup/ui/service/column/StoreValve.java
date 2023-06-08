package cn.vertxup.ui.service.column;

import cn.vertxup.ui.domain.tables.daos.UiColumnDao;
import cn.vertxup.ui.domain.tables.pojos.UiColumn;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Comparator;
import java.util.stream.Collectors;

import static io.vertx.mod.ui.refine.Ui.LOG;

class StoreValve implements UiValve {

    @Override
    public Future<JsonArray> fetchColumn(final Vis vis, final String identifier, final String sigma) {
        /*
         * Default global controlId is
         * 1) The format is VIEW-identifier
         * 2) sigma could distinguish multi applications
         */
        final String controlId = vis.view() + "-" + identifier;
        final JsonObject filters = new JsonObject();
        filters.put(VString.EMPTY, Boolean.TRUE);
        filters.put("controlId", controlId);
        filters.put(KName.SIGMA, sigma);
        LOG.Ui.info(this.getClass(), "The condition for column fetching: {0}", filters.encode());
        return Ux.Jooq.on(UiColumnDao.class)
            .<UiColumn>fetchAsync(filters)
            .compose(list -> Ux.future(list.stream()
                /*
                 * Position Sorting
                 */
                .sorted(Comparator.comparing(UiColumn::getPosition))
                .collect(Collectors.toList())))
            .compose(list -> Ux.future(list.stream().map(this::procColumn).collect(Collectors.toList())))
            .compose(jsonList -> Ux.future(new JsonArray(jsonList)));
    }

    private JsonObject procColumn(final UiColumn column) {
        final JsonObject columnJson = new JsonObject();
        columnJson.put("title", column.getTitle());
        columnJson.put("dataIndex", column.getDataIndex());
        /*
         * sorter
         * className
         * fixed
         * width
         */
        Fn.monad(column::getSorter, (sorter) -> columnJson.put("sorter", sorter));
        Fn.monad(column::getFixed, (fixed) -> {
            if (fixed) {
                columnJson.put("fixed", "left");
            } else {
                columnJson.put("fixed", "right");
            }
        });
        Fn.monad(column::getClassName, (className) -> columnJson.put("className", className));
        Fn.monad(column::getWidth, (width) -> columnJson.put("width", width));
        /*
         * If render
         */
        Fn.monad(column::getRender, (render) -> {
            columnJson.put("$render", render);
            if ("DATE".equals(render)) {
                assert null != column.getFormat() : " $format should not be null when DATE";
                columnJson.put("$format", column.getFormat());
            } else if ("DATUM".equals(render)) {
                // columnJson.put("$datum")
                assert null != column.getDatum() : " $datum should not be null when DATUM";
                columnJson.put("$datum", column.getDatum());
            }
        });
        Fn.monad(column::getFilterType, (filterType) -> {
            columnJson.put("$filter.type", filterType);
            columnJson.put("$filter.config", column.getFilterConfig());
            Ut.valueToJObject(columnJson, "$filter.config");
        });
        /*
         * Zero Config
         */
        Fn.monad(column::getEmpty, (empty) -> columnJson.put("$empty", empty));
        Fn.monad(column::getMapping, (mapping) -> {
            columnJson.put("$mapping", mapping);
            Ut.valueToJObject(columnJson, "$mapping");
        });
        Fn.monad(column::getConfig, (config) -> {
            columnJson.put("$config", config);
            Ut.valueToJObject(columnJson, "$config");
        });
        return columnJson;
    }
}
