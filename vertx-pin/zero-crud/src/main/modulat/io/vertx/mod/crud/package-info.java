/**
 * This plugin-in is different from others because it could be mount by zero system.
 * There defined some API ( Agent/Worker ) in this module as uniform CRUD Restful Api here such as
 * common usage.
 * For example, here are a module named role, you can define class name in the file
 * up/module/, this plugin will analyze all the definition file and mount the api to your system.
 * Here are some points:
 * 1) The event but name is different from "EVENT://", it's named with other prefix to distinguish from standard.
 * 2) Here this module has high priority to define RESTful API, but also it could help you to avoid some duplicated
 * coding works.
 * 3) If you want to do some performance turning, you can extend the interface here.
 */
package io.vertx.mod.crud;