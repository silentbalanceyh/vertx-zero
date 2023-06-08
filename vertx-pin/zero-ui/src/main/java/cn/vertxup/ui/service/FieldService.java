package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiFieldDao;
import cn.vertxup.ui.domain.tables.pojos.UiField;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.em.RowType;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static io.vertx.mod.ui.refine.Ui.LOG;

public class FieldService implements FieldStub {
    private static final Annal LOGGER = Annal.get(FieldService.class);

    @Override
    public Future<JsonArray> fetchUi(final String formId) {
        return Ux.Jooq.on(UiFieldDao.class)
            .<UiField>fetchAsync(KName.Ui.CONTROL_ID, formId)
            .compose(ui -> {
                if (Objects.isNull(ui) || ui.isEmpty()) {
                    LOG.Ui.warn(LOGGER, " Field not configured.");
                    return Ux.future(new JsonArray());
                } else {
                    final JsonArray uiJson = Ut.serializeJson(ui);
                    return this.attachConfig(uiJson);
                }
            });
    }

    @Override
    public Future<JsonArray> updateA(final String controlId, final JsonArray data) {
        final ConcurrentMap<Object, Boolean> seen = new ConcurrentHashMap<>();
        // 1. mountIn fields, convert those into object from string
        final List<UiField> fields = Ut.itJArray(data)
            // filter(deduplicate) by name
            .filter(item -> (!item.containsKey(KName.NAME)) ||
                (Ut.isNotNil(item.getString(KName.NAME)) && null == seen.putIfAbsent(item.getString(KName.NAME), Boolean.TRUE)))
            .map(item -> Ut.valueToString(item,
                FieldStub.OPTION_JSX,
                FieldStub.OPTION_CONFIG,
                FieldStub.OPTION_ITEM,
                FieldStub.RULES,
                KName.METADATA
            ))
            .map(field -> field.put(KName.Ui.CONTROL_ID, Optional.ofNullable(field.getString(KName.Ui.CONTROL_ID)).orElse(controlId)))
            .map(field -> Ux.fromJson(field, UiField.class))
            .collect(Collectors.toList());
        // 2. delete old ones and insert new ones
        return this.deleteByControlId(controlId)
            .compose(result -> Ux.Jooq.on(UiFieldDao.class)
                .insertAsync(fields)
                .compose(Ux::futureA)
                // 3. mountOut
                .compose(Fn.ofJArray(
                    FieldStub.OPTION_JSX,
                    FieldStub.OPTION_CONFIG,
                    FieldStub.OPTION_ITEM,
                    FieldStub.RULES,
                    KName.METADATA
                )));
    }

    @Override
    public Future<Boolean> deleteByControlId(final String controlId) {
        return Ux.Jooq.on(UiFieldDao.class)
            .deleteByAsync(new JsonObject().put(KName.Ui.CONTROL_ID, controlId));
    }

    private Future<JsonArray> attachConfig(final JsonArray fieldJson) {
        /*
         * metadata mode for parsing processor
         */
        final JsonArray ui = new JsonArray();
        /*
         * Calculate row
         */
        final int rowIndex = Ut.itJArray(fieldJson)
            .map(each -> each.getInteger("yPoint"))
            .max(Comparator.naturalOrder())
            .orElse(0);
        for (int idx = 0; idx <= rowIndex; idx++) {
            final Integer current = idx;
            final List<JsonObject> row = Ut.itJArray(fieldJson)
                .filter(item -> current.equals(item.getInteger("yPoint")))
                .sorted(Comparator.comparing(item -> item.getInteger("xPoint")))
                .toList();
            /*
             * Calculate columns
             */
            final JsonArray rowArr = new JsonArray();
            row.forEach(cell -> {
                /*
                 * Title row is special here
                 */
                final RowType rowType = Ut.toEnum(() -> cell.getString("rowType"),
                    RowType.class, RowType.FIELD);
                final JsonObject dataCell = new JsonObject();
                if (RowType.TITLE == rowType) {
                    dataCell.put("title", cell.getValue("label"));
                    dataCell.put("field", cell.getValue(KName.KEY));    // Specific field for title.
                } else if (RowType.CONTAINER == rowType) {
                    dataCell.put("complex", Boolean.TRUE);
                    // Container type will be mapped to name field here
                    dataCell.put(KName.NAME, cell.getValue("container"));
                    // optionJsx -> config
                    Ut.valueToJObject(cell, FieldStub.OPTION_JSX);
                    if (Objects.nonNull(cell.getValue(FieldStub.OPTION_JSX))) {
                        dataCell.put(KName.Ui.CONFIG, cell.getValue(FieldStub.OPTION_JSX));
                    }
                } else {
                    Ut.valueToJObject(cell,
                        FieldStub.OPTION_JSX,
                        FieldStub.OPTION_CONFIG,
                        FieldStub.OPTION_ITEM,
                        "rules"
                    );
                    final String render = Objects.isNull(cell.getString("render")) ? "" :
                        cell.getString("render");
                    final String label = Objects.isNull(cell.getString("label")) ? "" :
                        cell.getString("label");
                    final String metadata = cell.getString("name")
                        + "," + label + "," + cell.getInteger("span")
                        + ",," + render;

                    dataCell.put("metadata", metadata);
                    /*
                     * hidden
                     */
                    final Boolean hidden = cell.getBoolean("hidden");
                    if (hidden) {
                        dataCell.put("hidden", Boolean.TRUE);
                    }
                    /*
                     * Rules
                     */
                    final JsonArray rules = cell.getJsonArray("rules");
                    if (Objects.nonNull(rules) && !rules.isEmpty()) {
                        dataCell.put("optionConfig.rules", rules);
                    }
                    /*
                     * Three core configuration
                     */
                    if (Objects.nonNull(cell.getValue(FieldStub.OPTION_JSX))) {
                        dataCell.put(FieldStub.OPTION_JSX, cell.getValue(FieldStub.OPTION_JSX));
                    }
                    if (Objects.nonNull(cell.getValue(FieldStub.OPTION_CONFIG))) {
                        dataCell.put(FieldStub.OPTION_CONFIG, cell.getValue(FieldStub.OPTION_CONFIG));
                    }
                    if (Objects.nonNull(cell.getValue(FieldStub.OPTION_ITEM))) {
                        dataCell.put(FieldStub.OPTION_ITEM, cell.getValue(FieldStub.OPTION_ITEM));
                    }

                    /*
                     * moment
                     * 1) When `Edit/Add` status
                     * 2) When `View` status
                     * In this kind of situation, the config `optionJsx` must contains `config.format` here.
                     */
                    final JsonObject optionJsx = cell.getJsonObject(FieldStub.OPTION_JSX);

                    if (Ut.isNotNil(optionJsx)) {
                        final JsonObject config = optionJsx.getJsonObject("config");
                        if (Ut.isNotNil(config) && config.containsKey("format")) {
                            /*
                             * Date here for moment = true
                             * Here are some difference between two components
                             * 1) For `Date`, the format should be string
                             * 2) For `TableEditor`, the format should be object
                             * The table editor is added new here
                             */
                            final Object format = config.getValue("format");
                            if (String.class == format.getClass()) {
                                dataCell.put("moment", true);
                            }
                        }
                    }
                }
                rowArr.add(dataCell);
            });
            ui.add(rowArr);
        }
        return Ux.future(ui);
    }
}
