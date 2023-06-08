package io.mature.extension.quiz.develop;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DevDefault {

    /*
     * DIR Structure for Development Toolkit
     *
     * 1. Dynamic Modeling ( zero-atom )
     *
     * The following dir structure is for dynamic modeling, for ( Output ) part, it's generated by toolkit
     * automatically, it means that it's not needed to create it manually.
     *
     * - atom                           Dynamic Modeling Definition
     *   atom/cmdb                      For CMDB Platform
     *   atom/cmdb/reference            Modeling Reference Definition
     *   atom/cmdb/rule                 Unique Rule for Modeling Definition
     *   atom/cmdb/schema               Model Definition Schema Excel files
     *   atom/target                    ( Output Dir )
     *   atom/target/schema             ( Output Entity Files )
     *   atom/target/momdel             ( Output Model Files )
     *
     * Be careful of that this folder is not under `init/oob` because all the steps are preparing for data loading
     * before DevLoader.
     */
    static final Cc<String, DevModeller> CC_MODELLER = Cc.openThread();
    // Public
    static final String ROOT_INPUT = "atom/cmdb";
    static final String ROOT_OUTPUT = "src/main/resources/atom/target";
    // Private
    private static final String ROOT_MENU_ROLE = "init/permission/ui.menu/role/";
    private static final String ROOT_MENU = "init/permission/ui.menu/";
    private static final String JSON_EXTENSION = VString.DOT + VPath.SUFFIX.JSON;
    /*
     * 0. Root Folder
     *
     * The root folder is `init/oob` based on specification, all the files under this folder will
     * be loaded into database by Development Toolkit
     *
     * - init/oob
     */
    private static final String ROOT_OOB = "init/oob";

    // oob + suffix
    public static String pathOob() {
        return ROOT_OOB;
    }


    /*
     * 2. Cab Folder ( zero-ui )
     *
     * Cab folder is for UI Configuration connect to `UI_PAGE/UI_LAYOUT` table here, the specification are as following:
     *
     * - init/oob/cab/components:  UI Configuration For Page
     * - init/oob/cab/container:   UI Configuration For Layout ( Container )
     */
    public static String pathCab() {
        return ROOT_OOB + "/cab";
    }


    /*
     * 3. Cab Extension for Dynamic Model
     *
     * This folder is for CMDB only, in current version all the dynamic modeling files are stored under this folder
     * instead. the identifier is model identifier
     *
     * - init/oob/cmdb
     *   init/oob/cmdb/<identifier>/xxx.xlsx:  The major configuration excel files
     *   init/oob/cmdb/<identifier>/form:      The resource files that are related to UI_FORM
     *   init/oob/cmdb/<identifier>/ui:        The resource files that are related to UI_LIST
     */
    public static String pathCmdb() {
        return ROOT_OOB + "/cmdb";
    }

    public static String pathUi(final String identifier) {
        Objects.requireNonNull(identifier);
        return ROOT_OOB + "/cmdb/" + identifier + "/";
    }

    /*
     * 4. Data Dir
     *
     * This folder is only for basic data that will be loaded into database
     *
     * 1) Service Catalog Data
     * 2) Dictionary Data
     * 3) Location of WH ( PSI )
     * 4) System Menu ( Exclude OOB of Default Extension Menus )
     * 5) Resource Tree Data for ACL
     *
     * - init/oob/data
     */
    public static String pathData() {
        return ROOT_OOB + "/data";
    }

    /*
     * 5. Environment Dir
     * This folder is for environment data loading
     *
     * 1) Default Account
     * 2) Company/Employee/Dept/Team Information
     *
     * - init/oob/environment
     */
    public static String pathEnvironment() {
        return ROOT_OOB + "/environment";
    }

    /*
     * 6. Integration Dir ( zero-is )
     *
     * This folder occurs in tha latest version ( > 0.9 ), it stored integration default data
     *
     * 1) FTP
     * 2) RESTful
     * 3) SMS
     * 4) Email
     *
     * - init/oob/integration
     */
    public static String pathIntegration() {
        return ROOT_OOB + "/integration";
    }

    /*
     * 7. Modulat Dir ( zero-battery )
     *
     * This folder resources are all related to Zero Modulat, you can define new module in Development Toolkit
     * under this folder
     *
     * - init/oob/modulat
     */
    public static String pathModulat() {
        return ROOT_OOB + "/modulat";
    }

    /*
     * 8. Role Permission
     *
     * This folder contains following critical info:
     *
     * 1) Permission Re-initialization Script for Each role
     * 2) The Extension Permission of current app ( Stored in LANG.YU )
     *
     * You can modify the `run-perm.sh` script for new role added.
     */
    public static String pathRole(final String role) {
        Objects.requireNonNull(role);
        return ROOT_OOB + "/role/" + role + "/";
    }

    public static Set<String> roles() {
        final List<String> files = Ut.ioFiles(ROOT_MENU_ROLE);
        final Set<String> roleSet = new HashSet<>();
        files.forEach(file -> {
            final String role = file.replace(".json", VString.EMPTY);
            roleSet.add(role);
        });
        return roleSet;
    }

    /*
     * 9. Activity Rule ( zero-ambient )
     *
     * This folder is related for new development feature include all rules stored in `X_ACTIVITY_RULE`, in new workflow
     * engine, you can set different rule for each workflow's node.
     *
     * 1) Whether generate the new logs for current operation
     * 2) Bind a `Hooker` callback action in each rule ( Triggered or Not )
     * 3) Notify ( Email / SMS ) that's are related to operation
     * 4) AOP injection on workflow actions
     *
     * Start new script engine to parse condition ( JEXL ) for checking
     */
    public static String pathActivity(final String name) {
        if (Ut.isNil(name)) {
            return ROOT_OOB + "/workflow";
        } else {
            return ROOT_OOB + "/workflow/" + name;
        }
    }

    public static JsonArray pathMenu(final String role) {
        return Ut.ioJArray(ROOT_MENU_ROLE + role + JSON_EXTENSION);
    }

    public static String pathMenu(final String root, final String role) {
        return root + ROOT_MENU + role + JSON_EXTENSION;
    }

}