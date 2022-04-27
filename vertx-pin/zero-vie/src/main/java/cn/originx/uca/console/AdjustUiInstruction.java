package cn.originx.uca.console;

import cn.originx.refine.Ox;
import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import cn.originx.uca.ui.FieldStatus;
import cn.vertxup.ui.domain.tables.daos.UiColumnDao;
import cn.vertxup.ui.domain.tables.daos.UiFieldDao;
import cn.vertxup.ui.domain.tables.daos.UiFormDao;
import cn.vertxup.ui.domain.tables.daos.UiListDao;
import cn.vertxup.ui.domain.tables.pojos.UiColumn;
import cn.vertxup.ui.domain.tables.pojos.UiField;
import cn.vertxup.ui.domain.tables.pojos.UiForm;
import cn.vertxup.ui.domain.tables.pojos.UiList;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Log;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdjustUiInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        /*
         * 读取标识符
         */
        return this.runEach(appName, null, this::executeAsync).compose(done -> {
            final JsonArray normalized = new JsonArray();
            done.forEach(normalized::addAll);
            /*
             * 生成报表
             */
            this.printReport(normalized);
            return Ux.future(TermStatus.SUCCESS);
        });
    }

    private void printHeader(final StringBuilder content, final String identifier) {
        final JsonObject config = Ut.valueJObject(this.atom.getConfig().getJsonObject("header"));

        /* Format Table */
        content.append(this.atom.getConfig().getString("identifier")).append(" : ");
        content.append(Log.color(identifier, Log.COLOR_CYAN, true)).append("\n");
        content.append("--------------------------------------------------");
        content.append("----------------------------------------------------------\n");
        content.append(String.format("%-40s", " " + Log.color(config.getString("control"), Log.COLOR_BLANK, true)));
        content.append(String.format("%-26s", Log.color(config.getString("status"), Log.COLOR_BLANK, true)));
        content.append(String.format("%-32s", Log.color(config.getString("expected"), Log.COLOR_BLANK, true)));
        content.append(String.format("%-32s", Log.color(config.getString("actual"), Log.COLOR_BLANK, true))).append("\n");
        content.append("--------------------------------------------------");
        content.append("----------------------------------------------------------\n");
    }

    private void printRow(final StringBuilder content, final JsonObject item) {
        final String status = item.getString(KName.STATUS);
        content.append(String.format("%-32s", item.getString("control")));
        if ("INVALID".equals(status)) {
            content.append(String.format("%-28s", Log.color(item.getString("status"), Log.COLOR_RED, true)));
        } else {
            content.append(String.format("%-28s", Log.color(item.getString("status"), Log.COLOR_YELLOW, true)));
        }
        content.append(String.format("%-24s", item.getString("attribute")));
        content.append(String.format("%-32s", item.getString("uiField")));
        content.append("\n");
    }

    private void printReport(final JsonArray normalized) {
        /*
         * 先过滤，只包含 INVALID, REMAIN，滤掉 MATCH
         * 1. 条件1：过滤掉 MATCH 的，匹配的不呈现
         * 2. 条件2：$button 字段滤掉
         * 3. 条件3：根据配置过滤 ignores
         * 4. 条件4：表单命名规则中是否指定 suffix
         */
        final JsonArray processed = new JsonArray();
        Ut.itJArray(normalized)
            .filter(item -> !FieldStatus.MATCH.name().equals(item.getString(KName.STATUS)))
            .filter(item -> !"$button".equals(item.getString("uiField")))
            .filter(item -> !"connector".equals(item.getString("uiField")))
            .filter(item -> !this.ignores().contains(item.getString("attribute")))
            .filter(item -> {
                final String control = item.getString("control");
                final String type = item.getString("type");
                if ("FORM".equals(type)) {
                    /*
                     * 表单类
                     */
                    if (control.endsWith("save")) {
                        /*
                         * save 类 -> REMAIN / INVALID 都输出
                         */
                        return true;
                    } else {
                        /*
                         * filter, view, batch 只输出 REMAIN
                         */
                        return "REMAIN".equals(item.getString("status"));
                    }
                } else {
                    /*
                     * 列表类，只输出 REMAIN
                     */
                    return "REMAIN".equals(item.getString("status"));
                }
            }).forEach(processed::add);
        /* Format Table */
        final ConcurrentMap<String, JsonArray> formatted = Ut.elementGroup(processed, KName.IDENTIFIER);
        /* 排序 identifier */
        final Set<String> treeSet = new TreeSet<>(formatted.keySet());
        final StringBuilder content = new StringBuilder();

        /* 成功 */
        final AtomicBoolean noError = new AtomicBoolean(Boolean.TRUE);
        treeSet.forEach(identifier -> {
            /*
             * Row print here
             */
            final JsonArray dataTable = formatted.get(identifier);
            if (!dataTable.isEmpty()) {
                noError.set(Boolean.FALSE);
                this.printHeader(content, identifier);
                Ut.itJArray(dataTable).forEach(item -> this.printRow(content, item));
            }
        });
        if (noError.get()) {
            Sl.output("所有的表单和列表配置都正确！Form / List，--> {0}",
                Log.color("Successfully", Log.COLOR_GREEN, true));
        }
        System.out.println(content.toString());
    }

    private Future<JsonArray> executeAsync(final String identifier) {
        /*
         * 生成记录报表
         */
        return Ok.app().compose(app -> {
            final DataAtom atom = Ao.toAtom(app.getName(), identifier);
            /*
             * Form -> 表单数据处理
             * List -> 列配置数据处理
             */
            final Refer formRefer = new Refer();
            final Refer listRefer = new Refer();
            return this.uiForm(identifier, app.getSigma())
                .compose(formRefer::future)
                .compose(nil -> this.uiList(identifier, app.getSigma()))
                .compose(listRefer::future)
                .compose(nil -> Ux.future(Ox.compareUi(atom, formRefer.get(), listRefer.get())));
        });
    }

    private Future<JsonArray> uiList(final String identifier, final String sigma) {
        final JsonObject condition = this.uiCond(identifier, sigma);
        return Ux.Jooq.on(UiListDao.class).<UiList>fetchAndAsync(condition)
            .compose(lists -> Ux.thenCombineT(lists, this::uiListField))
            .compose(Ux::futureA);
    }

    private Future<JsonObject> uiListField(final UiList list) {
        final JsonObject condition = new JsonObject();
        final String controlId = "DEFAULT-" + list.getIdentifier();
        condition.put(KName.SIGMA, list.getSigma());
        condition.put("controlId", controlId);
        final JsonObject listJson = Ux.toJson(list);
        return Ux.Jooq.on(UiColumnDao.class).<UiColumn>fetchAndAsync(condition)
            .compose(fields -> {
                listJson.put(KName.Ui.COLUMNS, Ux.toJson(fields));
                return Ux.future(listJson);
            });
    }

    private Future<JsonArray> uiForm(final String identifier, final String sigma) {
        final JsonObject condition = this.uiCond(identifier, sigma);
        return Ux.Jooq.on(UiFormDao.class).<UiForm>fetchAndAsync(condition)
            .compose(forms -> Ux.thenCombineT(forms, this::uiFormField))
            .compose(Ux::futureA);
    }

    private Future<JsonObject> uiFormField(final UiForm form) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, form.getSigma());
        condition.put("controlId", form.getKey());
        final JsonObject formJson = Ux.toJson(form);
        return Ux.Jooq.on(UiFieldDao.class).<UiField>fetchAndAsync(condition)
            .compose(fields -> {
                formJson.put(KName.Modeling.FIELDS, Ux.toJson(fields));
                return Ux.future(formJson);
            });
    }

    private JsonObject uiCond(final String identifier, final String sigma) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.IDENTIFIER, identifier);
        condition.put(KName.SIGMA, sigma);
        return condition;
    }
}
