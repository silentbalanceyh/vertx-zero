package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiControlDao;
import cn.vertxup.ui.domain.tables.daos.UiVisitorDao;
import cn.vertxup.ui.domain.tables.pojos.UiControl;
import cn.vertxup.ui.domain.tables.pojos.UiVisitor;
import io.horizon.spi.ui.UiHunter;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.em.ControlType;
import io.vertx.up.atom.typed.UData;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.mod.ui.refine.Ui.LOG;

public class ControlService implements ControlStub {

    private static final Annal LOGGER = Annal.get(ControlService.class);

    @Override
    public Future<JsonArray> fetchControls(final String pageId) {
        return Ux.Jooq.on(UiControlDao.class)
            .<UiControl>fetchAsync("pageId", pageId)
            .compose(Ux::futureA)
            .compose(list -> {
                /*
                 * Convert JsonArray field of Control
                 */
                final JsonArray result = new JsonArray();
                list.stream().filter(Objects::nonNull)
                    .map(item -> (JsonObject) item)
                    .map(item -> Ut.valueToJObject(item,
                        KName.Ui.CONTAINER_CONFIG,
                        KName.Ui.COMPONENT_CONFIG,
                        KName.Ui.ASSIST,
                        KName.Ui.GRID
                    ))
                    .forEach(result::add);
                return Ux.future(result);
            });
    }

    @Override
    public Future<JsonObject> fetchById(final String control) {
        return Ux.Jooq.on(UiControlDao.class)
            .<UiControl>fetchByIdAsync(control)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject(
                KName.Ui.CONTAINER_CONFIG,
                KName.Ui.COMPONENT_CONFIG,
                KName.Ui.ASSIST,
                KName.Ui.GRID
            ));
    }

    @Override
    public Future<JsonObject> fetchControl(final ControlType controlType, final JsonObject params) {
        /*
         * The first step to fetch UI_VISITOR record.
         * {
         *      "type": "组件类型：LIST | FORM",
         *      "sigma": "统一标识符",
         *      "page": "页面ID，对应 UI_PAGE 中的记录",
         *      "path": "三部分组成，前端自动计算的 view / position，配置中的 __ALIAS__ -> alias",
         *      "identifier": "建模管理中模型专用标识符 identifier"
         * }
         */
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.TYPE, controlType.name());
        Ut.valueCopy(criteria, params,
            KName.SIGMA,
            KName.IDENTIFIER,
            KName.Ui.PAGE,
            KName.App.PATH
        );
        LOG.Ui.info(LOGGER, "Control ( type = {0} ) with parameters = `{1}`", controlType, criteria.encode());
        return Ux.Jooq.on(UiVisitorDao.class).<UiVisitor>fetchOneAsync(criteria).compose(visitor -> {
            if (Objects.isNull(visitor)) {
                /* 「空」无任何记录，直接返回空结果 */
                return Ux.futureJ();
            }
            if (Ut.isNil(visitor.getControlId())) {
                /* 不包含 controlId，如果不包含，则执行第二级操作 */
                final String component = visitor.getRunComponent();
                if (Ut.isNil(component)) {
                    /* 「空」组件未配置，返回空结果 */
                    return Ux.futureJ();
                }
                final Class<?> clazz = Ut.clazz(component, null);
                /*
                 * 两个规范
                 * 1. 「空」clazz 为空，跳出
                 * 2. 「实现」clazz 必须实现了 UiHunter.class 接口
                 */
                if (Objects.isNull(clazz) || !Ut.isImplement(clazz, UiHunter.class)) {
                    /* 「空」组件规范不合法，返回空结果 */
                    return Ux.futureJ();
                }
                /* 再次检索，构造 UData */
                final UiHunter hunter = Ut.instance(clazz);
                final UData data = UData.createJ(params);
                return hunter.seek(data, visitor).compose(controlId -> {
                    final JsonObject response = Ux.toJson(visitor);
                    response.put(KName.Ui.CONTROL_ID, controlId);
                    return Ux.future(response);
                });
            } else {
                /* 包含 controlId，直接执行返回，最终返回数据必须包含 controlId */
                return Ux.futureJ(visitor);
            }
        });
    }
}
