package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiPageDao;
import cn.vertxup.ui.domain.tables.pojos.UiPage;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class PageService implements PageStub {
    @Inject
    private transient TplStub tplStub;
    @Inject
    private transient ControlStub controlStub;

    @Override
    public Future<JsonObject> fetchAmp(final String sigma,
                                       final JsonObject params) {
        final JsonObject filters = params.copy();
        filters.put(KName.SIGMA, sigma);
        filters.put("", Boolean.TRUE);
        return Ux.Jooq.on(UiPageDao.class)
            .<UiPage>fetchOneAsync(filters)
            .compose(page -> {
                if (Objects.nonNull(page)) {
                    /*
                     * Page Existing in current system
                     */
                    if (Ut.notNil(page.getLayoutId())) {
                        /*
                         * Continue to extract layout Data here
                         */
                        return this.fetchLayout(page);
                    } else {
                        return Ux.futureJ(page);
                    }
                } else {
                    /*
                     * No configuration related to current page
                     */
                    return Ux.future(new JsonObject());
                }
            })
            .compose(pageJson -> {
                /*
                 * Extract pageId
                 */
                final String pageId = pageJson.getString(KName.KEY);
                return this.controlStub.fetchControls(pageId)
                    /*
                     * Fetch Controls of current page
                     * This will be filled into $control variable
                     */
                    .compose(controls -> {
                        /*
                         * Grouped by key, this could be used in front tier directly
                         */
                        final JsonObject converted = new JsonObject();
                        controls.stream()
                            .filter(Objects::nonNull)
                            .map(item -> (JsonObject) item)
                            .filter(item -> Objects.nonNull(item.getString(KName.KEY)))
                            .forEach(item -> converted.put(item.getString(KName.KEY), item.copy()));
                        pageJson.put(KName.Ui.CONTROLS, converted);
                        return Ux.future(pageJson);
                    });
            });
    }

    /*
     * Fetch layout by page.
     */
    private Future<JsonObject> fetchLayout(final UiPage page) {
        return this.tplStub.fetchLayout(page.getLayoutId())
            .compose(layout -> {
                final JsonObject pageJson = Ux.toJson(page);
                pageJson.put("layout", layout);
                return Ux.future(pageJson)
                    /*
                     * Configuration converted to Json
                     */
                    .compose(Ke.mount(KName.Ui.CONTAINER_CONFIG))
                    .compose(Ke.mount(KName.Ui.ASSIST))
                    /*
                     * Another method to convert JsonArray
                     */
                    .compose(Ke.mountArray(KName.Ui.GRID));
            });
    }
}
