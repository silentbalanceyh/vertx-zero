package cn.originx.migration.restore;

import cn.originx.migration.AbstractStep;
import cn.originx.refine.Ox;
import cn.vertxup.ambient.domain.tables.daos.XNumberDao;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.eon.bridge.Values;
import io.horizon.eon.em.Environment;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdjustNumber extends AbstractStep {
    private static final String ADJUST = "adjust";

    public AdjustNumber(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        final String folder = this.ioRoot(config);
        final String file = folder + "report/numbers/data.json";
        Ox.Log.infoShell(this.getClass(), "读取修正文件：{0}", file);
        /*
         * 修正文件专用数据
         */
        final JsonArray numberData = Ut.ioJArray(file);
        this.report(numberData);
        /*
         * 根据最终数据更新 XNumber
         */
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(numberData)
            .filter(item -> Objects.nonNull(item.getValue(ADJUST)))
            .filter(item -> Values.RANGE < item.getInteger(ADJUST))
            .map(this::saveNumber).forEach(futures::add);
        return Fn.combineA(futures).compose(processed -> {
            Ox.Log.infoShell(this.getClass(), "修正序号完成！");
            return Ux.future(config);
        });
    }

    private Future<JsonObject> saveNumber(final JsonObject item) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.SIGMA, this.app.getSigma());
        filters.put(KName.IDENTIFIER, item.getString(KName.IDENTIFIER));
        filters.put(Strings.EMPTY, Boolean.TRUE);
        return Ux.Jooq.on(XNumberDao.class).<XNumber>fetchOneAsync(filters).compose(number -> {
            final int adjust = item.getInteger(ADJUST);
            number.setCurrent((long) adjust);
            return Ux.Jooq.on(XNumberDao.class).updateAsync(number)
                .compose(Ux::futureJ);
        }).otherwise(error -> {
            if (Objects.nonNull(error)) {
                error.printStackTrace();
            }
            return new JsonObject();
        });
    }

    private void report(final JsonArray numberData) {
        if (Ut.notNil(numberData)) {
            final StringBuilder content = new StringBuilder();
            final String width = "\t\t\t\t\t\t";
            content.append(Ut.fromAdjust("模型标识", 28)).append(width);
            content.append(Ut.fromAdjust("当前值", 15)).append(width);
            content.append(Ut.fromAdjust("错误值", 12)).append(width);
            content.append(Ut.fromAdjust("修正值", 12)).append("\n");
            Ut.itJArray(numberData).forEach(item -> {
                final Integer adjust = item.getInteger(ADJUST);
                if (Objects.nonNull(adjust) && Values.RANGE != adjust) {
                    content.append(Ut.fromAdjust(item.getString(KName.IDENTIFIER), 32)).append(width);
                    content.append(Ut.fromAdjust(item.getInteger(KName.CODE), 15, ' ')).append(width);
                    content.append(Ut.fromAdjust(item.getInteger("current"), 15, ' ')).append(width);
                    content.append(Ut.fromAdjust(item.getInteger(ADJUST), 15, ' ')).append("\n");
                }
            });
            Ox.Log.infoShell(this.getClass(), "完整报表：\n{0}", content.toString());
        }
    }
}
