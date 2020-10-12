package cn.vertxup.ui.service.column;

import cn.vertxup.ui.domain.tables.daos.UiColumnDao;
import cn.vertxup.ui.domain.tables.pojos.UiColumn;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;

import java.util.Comparator;
import java.util.stream.Collectors;

class StoreValve implements UiValve {
    @Override
    public Future<JsonArray> fetchColumn(final String view, final String identifier, final String sigma) {
        /*
         * Default global controlId is
         * 1) The format is VIEW-identifier
         * 2) sigma could distinguish multi applications
         */
        final String controlId = view + "-" + identifier;
        final JsonObject filters = new JsonObject();
        filters.put("", Boolean.TRUE);
        filters.put("controlId", controlId);
        filters.put("sigma", sigma);
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
        Ke.runBoolean(column::getSorter, (sorter) -> columnJson.put("sorter", sorter));
        Ke.runBoolean(column::getFixed, (fixed) -> {
            if (fixed) {
                columnJson.put("fixed", "left");
            } else {
                columnJson.put("fixed", "right");
            }
        });
        Ke.runString(column::getClassName, (className) -> columnJson.put("className", className));
        Ke.runInteger(column::getWidth, (width) -> columnJson.put("width", width));
        /*
         * If render
         */
        Ke.runString(column::getRender, (render) -> {
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
        Ke.runString(column::getFilterType, (filterType) -> {
            columnJson.put("$filter.type", filterType);
            columnJson.put("$filter.config", column.getFilterConfig());
            Ke.mount(columnJson, "$filter.config");
        });
        /*
         * Zero Config
         */
        Ke.runString(column::getEmpty, (empty) -> columnJson.put("$empty", empty));
        Ke.runString(column::getMapping, (mapping) -> {
            columnJson.put("$mapping", mapping);
            Ke.mount(columnJson, "$mapping");
        });
        Ke.runString(column::getConfig, (config) -> {
            columnJson.put("$config", config);
            Ke.mount(columnJson, "$config");
        });
        return columnJson;
    }
}
