package io.vertx.mod.ambient.uca.differ;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.refine.At;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * # Activity/Changes Operation
 *
 * The main record of log history ( Activity Record )
 *
 * - key                    ( System Generated )
 * - serial                 ( System Generated )            - 「Workflow」,「Atom」
 * - type                   ( The Record type )
 * - description            ( Description )                 - 「Workflow」,「Atom」
 *
 * - modelId                ( Modal identifier )
 * - modelKey               ( Model Related key )
 * - modelCategory          ( Model Category )
 *
 * - taskName               ( Workflow Provided )           - 「Workflow」,「Atom」
 * - taskSerial             ( Workflow Provided )           - 「Workflow」,「Atom」
 * - recordOld              ( Old Json )
 * - recordNew              ( New Json )
 *
 * - sigma                  ( Json Provided )
 * - active                 ( Json Provided )
 * - language               ( Json Provided )
 * - metadata               ( Empty )
 * - createdAt              ( Outer Now )                   - 「Workflow」,「Atom」
 * - createdBy              ( Outer Current )               - 「Workflow」,「Atom」
 * - updatedAt              ( Outer Now )
 * - updatedBy              ( Outer Current )
 *
 * The sub records of history ( ActivityChange Record )
 *
 * - key                    ( System Generated )
 * - activityId             ( System Generated )
 *
 * - type                   ( System Generated, ADD, UPDATE, DELETE )
 * - status                 ( CONFIRMED / PENDING / SYSTEM )
 *
 * - fieldName              ( Came from Atom )
 * - fieldAlias             ( Came from Atom )
 * - fieldType              ( Came from Atom )
 * - valueOld               ( Came from Data )
 * - valueNew               ( Came from Data )
 *
 * - sigma                  ( Json Provided )
 * - language               ( Json Provided )
 * - active                 ( Json Provided )
 * - metadata               ( Empty )
 *
 * - createdBy              ( Outer Current )
 * - createdAt              ( Outer Now )
 * - updatedBy              ( Outer Current )
 * - updatedAt              ( Outer Now )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SchismJ extends AbstractSchism {
    @Override
    public Future<JsonObject> diffAsync(
        final JsonObject recordO, final JsonObject recordN, final Supplier<Future<XActivity>> activityFn) {
        Objects.requireNonNull(activityFn);
        /*
         * The activityFn will create new XActivity record for current comparing
         * 1) The basic condition is Ok
         * 2) The required field will be filled in current method
         */
        return activityFn.get().compose(activity -> {
            /*
             *  Here the two fields are ready:
             *  - key
             *  - serial
             */
            activity.setRecordOld(recordO.encode());
            activity.setRecordNew(recordN.encode());

            final List<XActivityChange> changes = At.diffChange(recordO, recordN, this.atom);

            At.diffChange(changes, activity);
            return this.createActivity(activity, changes);
        });
    }
}
