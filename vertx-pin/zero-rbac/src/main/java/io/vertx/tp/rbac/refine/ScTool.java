package io.vertx.tp.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Locale;

class ScTool {
    private static final Annal LOGGER = Annal.get(ScTool.class);
    private static final ScConfig CONFIG = ScPin.getConfig();

    static String generateCode() {
        final Integer codeLength = CONFIG.getCodeLength();
        final String authCode = Ut.randomString(codeLength);
        ScLog.infoAuth(LOGGER, "Generated Authorization Code: {0}", authCode);
        return authCode;
    }

    static String generatePwd() {
        return CONFIG.getPasswordInit();
    }

    static String generateProfileKey(final SResource resource) {
        /*
         * Get Role/Group/Tree modes
         */
        final String modeRole = resource.getModeRole();
        final String modeGroup = resource.getModeGroup();
        if (Ut.isNil(modeGroup)) {
            /*
             * User Mode
             *
             * USER_UNION
             * USER_INTERSECT
             * USER_EAGER
             * USER_LAZY
             */
            return "USER_" + modeRole.toUpperCase(Locale.getDefault());
        } else {
            final String modeTree = resource.getModeTree();
            final String group = modeGroup.toUpperCase(Locale.getDefault()) +
                    "_" + modeRole.toUpperCase(Locale.getDefault());
            if (Ut.isNil(modeTree)) {
                /*
                 * Group Mode
                 * HORIZON_XXX
                 * CRITICAL_XXX
                 * OVERLOOK_XXX
                 */
                return group;
            } else {
                /*
                 * Inherit / Child / Parent/ Extend
                 * EXTEND_XXX
                 * PARENT_XXX
                 * CHILD_XXX
                 * INHERIT_XXX
                 */
                return modeTree.toUpperCase(Locale.getDefault()) +
                        "_" + group;
            }
        }
    }
}
