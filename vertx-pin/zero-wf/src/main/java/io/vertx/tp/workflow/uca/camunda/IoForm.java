package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoForm extends AbstractIo<FormData, ProcessDefinition> {

    // ProcessDefinition -> StartFormData ( FormData )
    @Override
    public Future<FormData> start(final String definitionId) {
        final ProcessDefinition definition = this.pDefinition(definitionId);
        Objects.requireNonNull(definition);
        final FormService formService = WfPin.camundaForm();
        final StartFormData startForm = formService.getStartFormData(definition.getId());
        Objects.requireNonNull(startForm);
        /*
         * This method will capture the form key such as `e.start.json` etc for
         * usage in form normalization, based on the key information the system
         * could read the configuration of form based on workflow definition.
         */
        return Ux.future(startForm);
    }

    /*
     * Form output here
     * {
     *      "code": "Process Definition Key",
     *      "formKey": "The extract form key here, such as 'camunda-forms:deployment:xxx'",
     *      "definitionId": "Process Definition Id",
     *      "definitionKey": "Process Definition Key",
     * }
     */
    @Override
    public Future<JsonObject> out(final JsonObject workflow, final FormData formData) {
        // Extract code from formKey
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
        final String formKey = formData.getFormKey();
        Objects.requireNonNull(formKey);
        final String code = formKey.substring(formKey.lastIndexOf(Strings.COLON) + 1);
        // Build Form ConfigRunner parameters
        final JsonObject response = new JsonObject();
        response.put(KName.CODE, code);
        response.put(KName.Flow.FORM_KEY, formKey);

        if (formData instanceof StartFormData) {
            /*
             * {
             *      "code": "???",
             *      "formKey": "???",
             *      "definitionId": "???",
             *      "definitionKey": "???",
             * }
             */
            final ProcessDefinition definition = ((StartFormData) formData).getProcessDefinition();
            response.put(KName.Flow.DEFINITION_KEY, definition.getKey());
            response.put(KName.Flow.DEFINITION_ID, definition.getId());
        }
        return Ux.future(response);
    }
}
