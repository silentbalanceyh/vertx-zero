/*
 * Standard Rbac Api in zero system, if you want to use it, you must import as following:
 *
 *      <dependency>
            <groupId>cn.vertxup</groupId>
            <artifactId>zero-rbac</artifactId>
            <version>${project.version}</version>
        </dependency>
 *
 * Here are some limitation to use this module
 * 1) Your booter ( main ) must be under `cn.vertxup` package, it could trigger the class scan
 * 2) You couldn't design Security Restful API that has been used in current module scan:
 * Here are API position that has been taken:
 *
 * POST   /oauth/login          Login
 * POST   /oauth/authorize      Authorization Code
 * POST   /oauth/token          Token Exchange
 *
 * Here are some plugin:
 * 1) Apeak Infusion
 * column:
 *   component: xxx.xxx.ColService ( Must implement ColStub )
 * 2) Auditor Infusion
 * auditor:
 *   component: xxx.xxx.AuditorPin ( Must implement Auditor )
 */
package cn.vertxup;