package io.horizon.eon;

import io.horizon.util.HUt;

interface VSpecExtension {
    String ACTION = "action";
    String AUTHORITY = "authority";

    String CAB = "cab";
    String MODULAT = "modulat";

    String PLUGIN = "plugin";

    String WORKFLOW = "workflow";

    interface workflow {
        // workflow/{code}
        static String of(final String code) {
            return HUt.fromMessage(WORKFLOW + "/{}", code);
        }
    }

    interface plugin {
        String SQL = "sql";

        // plugin/{module}
        static String of(final String mod) {
            return HUt.fromMessage(PLUGIN + "/{}", mod);
        }

        interface mod {
            // plugin/{module}/configuration.json
            static String configuration_json(final String mod) {
                return HUt.fromMessage(plugin.of(mod) + "/configuration.json");
            }

            /**
             * <pre><code>
             *     - /plugin/mod/configuration.json  （模块内）主配置
             *     - /plugin/mod/oob                 （模块内）模块基础配置
             *     - /plugin/mod/oob/cab             （安全）管理员角色数据
             *     - /plugin/mod/oob/data            （数据）OOB基础数据
             *     - /plugin/mod/oob/menu            （菜单）模块菜单配置
             *     - /plugin/mod/oob/modulat         （模块）模块化专用配置
             *     - /plugin/mod/oob/module/crud     （扩展）zero-crud 模块专用配置
             *     - /plugin/mod/oob/module/ui       （扩展）zero-ui 模块专用配置
             *     - /plugin/mod/oob/role/           （安全）管理员角色权限数据
             *     - /plugin/mod/oob/initialize.json （文件）可使用 aj jmod 处理的初始化配置文件
             *     - /plugin/mod/oob/initialize.yml  （文件）Jooq Dao专用配置
             *     - /plugin/mod/oob/module.json     （文件）开启 module/ 目录中的扩展配置功能
             * </code></pre>
             */
            // plugin/{module}/oob/
            interface oob {
                // plugin/{module}/oob/
                static String of(final String mod) {
                    return HUt.fromMessage(PLUGIN + "/{}/oob", mod);
                }

                // plugin/{module}/oob/initialize.json
                static String initialize_json(final String mod) {
                    return HUt.fromMessage(oob.of(mod) + "/initialize.json");
                }

                // plugin/{module}/oob/initialize.yml
                static String initialize_yml(final String mod) {
                    return HUt.fromMessage(oob.of(mod) + "/initialize.yml");
                }

                // plugin/{module}/oob/module.json
                static String module_json(final String mod) {
                    return HUt.fromMessage(oob.of(mod) + "/module.json");
                }
            }
        }


        /**
         * <pre><code>
         *     - /plugin/sql/mod/                  （SQL）模块内表结构信息
         *     - /plugin/sql/mod.properties        Liquibase专用配置
         *     - /plugin/sql/mod.yml               Liquibase专用配置
         * </code></pre>
         */
        interface sql {

            // plugin/sql/{module}.properties
            static String liquibase_properties(final String mod) {
                return HUt.fromMessage(PLUGIN + "/{}/{}.properties", SQL, mod);
            }

            // plugin/sql/{module}.yml
            static String liquibase_yml(final String mod) {
                return HUt.fromMessage(PLUGIN + "/{}/{}.yml", SQL, mod);
            }

            interface mod {
                // plugin/sql/{module}
                static String of(final String mod) {
                    return HUt.fromMessage(PLUGIN + "/{}/{}", SQL, mod);
                }
            }
        }
    }

    interface modulat {
        // modulat/{module}
        static String of(final String mod) {
            return HUt.fromMessage(MODULAT + "/{}", mod);
        }
    }

    interface cab {
        // cab/directory
        String DIRECTORY = "directory";         // 文档管理目录专用配置

        // cab/{language}/{uri}
        static String of(final String language, final String uri) {
            return HUt.fromMessage(CAB + "/{}/{}", language, uri);
        }
    }

    interface authority {
        // authority/{code}
        static String of(final String code) {
            return HUt.fromMessage(AUTHORITY + "/{}", code);
        }
    }

    interface action {
        // action/{code}
        default String of(final String code) {
            return HUt.fromMessage(ACTION + "/{}", code);
        }
    }
}
