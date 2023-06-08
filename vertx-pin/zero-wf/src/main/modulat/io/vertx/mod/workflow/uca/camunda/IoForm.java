package io.vertx.mod.workflow.uca.camunda;

import cn.vertxup.workflow.cv.WfCv;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoForm extends AbstractIo<JsonObject> {

    // 「IoRuntime」ProcessDefinition -> StartFormData ( FormData )
    @Override
    public Future<JsonObject> start(final ProcessDefinition definition) {
        Objects.requireNonNull(definition);
        final FormService formService = WfPin.camundaForm();
        final StartFormData startForm = formService.getStartFormData(definition.getId());
        Objects.requireNonNull(startForm);
        /*
         * This method will capture the form key such as `e.start.json` etc for
         * usage in form normalization, based on the key information the system
         * could read the configuration of form based on workflow definition.
         */
        /*
         * Default Value is as following:
         * camunda-forms:deployment:e.start.form
         *
         * Above value should be:
         * formKey  = camunda-forms:deployment:e.start.form
         * code     = e.start.form
         *
         * Form Type is:
         * Embedded or External Task Forms
         */
        final String formKey = startForm.getFormKey();
        Objects.requireNonNull(formKey);
        final String code = formKey.substring(formKey.lastIndexOf(VString.COLON) + 1);
        return Ux.future(this.formInput(code, definition));
    }

    @Override
    public Future<JsonObject> run(final Task task) {
        final ProcessDefinition definition = this.inProcess(task.getProcessDefinitionId());
        Objects.requireNonNull(definition);
        final String formKey = task.getFormKey();
        Objects.requireNonNull(formKey);
        final String code = formKey.substring(formKey.lastIndexOf(VString.COLON) + 1);
        return Ux.future(this.formInput(code, definition));
    }

    @Override
    public Future<JsonObject> end(final HistoricProcessInstance instance) {
        final ProcessDefinition definition = this.inProcess(instance.getProcessDefinitionId());
        return Ux.future(this.formInput(WfCv.CODE_HISTORY, definition));
    }

    /*
     * Form output here
     * {
     *     "form": {}
     * }
     */
    @Override
    public Future<JsonObject> out(final JsonObject workflow, final JsonObject formData) {
        if (Ut.isNotNil(formData)) {
            workflow.put(KName.FORM, formData);
        }
        return Ux.future(workflow);
    }

    /*
     * {
     *     "dynamic": "true | false",
     *     "code":    "form code of unique",
     *     "sigma":   "will be append out of current ioForm instance"
     * }
     */
    private JsonObject formInput(final String code, final ProcessDefinition definition) {
        final String definitionKey = definition.getKey();
        final String configFile = WfCv.FOLDER_ROOT + "/" + definitionKey + "/" + code + ".json";

        final JsonObject formParameters = new JsonObject();
        if (Ut.ioExist(configFile)) {
            // Static Form
            formParameters.put(KName.DYNAMIC, Boolean.FALSE);
            formParameters.put(KName.CODE, configFile);
        } else {
            // Dynamic Form
            formParameters.put(KName.DYNAMIC, Boolean.TRUE);
            formParameters.put(KName.CODE, code);
        }
        return formParameters;
    }
}
