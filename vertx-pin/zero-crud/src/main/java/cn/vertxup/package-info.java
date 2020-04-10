/*
 * Standard Crud Api in zero system, if you want to use it, you must import as following:
 *
        <dependency>
            <groupId>cn.vertxup</groupId>
            <artifactId>zero-crud</artifactId>
            <version>${zero.version}</version>
        </dependency>
 *
 * Here are some limitation to use this module
 * 1) Your booter ( main ) must be under `cn.vertxup` package, it could trigger the class scan
 * 2) You couldn't design Restful API that has been used in current module such as /api/{module}
 * Here are API position that has been taken:
 *
 * < actor = moduleName >
 *
 * POST     /api/{actor}                    Single Insert
 * GET      /api/{actor}/{key}              Single Get ( By Id )
 * DELETE   /api/{actor}/{key}              Single Delete ( By Id )
 * PUT      /api/{actor}/{key}              Single Put ( By Id )
 * DELETE   /api/batch/{actor}/delete       Batch Delete
 * PUT      /api/batch/{actor}/update       Batch Update
 * POST     /api/{actor}/export             Export Operation
 * POST     /api/{actor}/import             Import Operation
 * POST     /api/{actor}/search             Search ( Search Engine )
 * GET      /api/columns/full/{actor}       Apeak Full
 * GET      /api/columns/my/{actor}         Apeak of My
 * POST     /api/columns/my/{actor}         Apeak Saving of My
 *
 */
package cn.vertxup;