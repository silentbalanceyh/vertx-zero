# Zero Framework

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.vertxup/vertx-zero/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/cn.vertxup/vertx-zero/)  [![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)  [![Build Status](https://travis-ci.org/silentbalanceyh/vertx-zero.svg?branch=master)](https://travis-ci.org/silentbalanceyh/vertx-zero)

Zero is a middleware framework based on [Vert.x](http://vertx.io) and it could help software engineers focus on business requirements instead of technical detail of Vert.x. The original idea of this framework came from [Spring Boot](https://spring.io/projects/spring-boot/) as that there is no approximative tools in Vert.x sphere at that time.

Zero has two metaphors, the original name is "Zero Up", "Up" means that I want to build a system that could be always running up online, "Zero" means no more workload for high production and you can build your own system efficiently with the default configuration only.

The latest Zero Micro Architecture ( [Aeon System](https://github.com/silentbalanceyh/vertx-zero-cloud) ) will be deployed to [K8S](https://kubernetes.io/) with [Istio](https://istio.io/) environment, it's a future focused method to build mature system with native cloud nature for more enterprise to execute critical valuable business efficiently. And I hope it could act as an important tool for more enterprise to complete digital transformation to satisfy the sustainable development, it could be high efficiently digital middleware in your side!

## 1. Features

**Critical For Beginner**: Zero Framework has been re-factor many times, the features are very complex now, you can refer following link for more details to know the power of Zero. 

* [功能支持表/Feature List](FEATURES.md) 

### 1.1. Structure

Zero Framework ( Latest Version ) contains five major projects as following:

|Name|Comment|
|---|---|
|vertx-gaia|**Zero Core** Runtime, it contains minimum zero environment and you can deploy your projects on zero.|
|vertx-ifx|**Infix Architecture**, Useful plug-ins that could be supported by Zero and you can choose as required.|
|vertx-import|Zero Usage dependency to perform development, it provides uniform entrance for your projects.|
|vertx-istio|**Aeon System**, The native cloud environment based on K8S with Istio. |
|vertx-pin|**Zero Extension** Modules, it provides common business features to satisfy many enterprise requirements such as [ODOO](https://www.odoo.com/). |

### 1.2. Usage Links

* **Examples**: In latest version, all zero original example demos have been moved to <https://github.com/silentbalanceyh/vertx-zero-example>.
* **Training Courses**: The official chinese training courses has been deployed to [Gitbook](https://www.gitbook.com/), the course link is [《Zero冥思录》](https://lang-yu.gitbook.io/zero/).
* **Vert.x**: If you want to study basic vert.x development skills, you can refer the tutorial: [《Vert.x逐陆记》](https://lang-yu.gitbook.io/vert-x/). 
* **English Docs**: You can refer origin documents on link: [Old Official Document](DOCUMENT.md). 

### 1.3. Related Open Source

* [Zero UI](http://www.vertxui.cn)：The front-end scaffold framework that are designed for zero based on [React](https://reactjs.org/) with [Ant-Design](https://ant-design.gitee.io/), [Ant-V](https://antv.vision/). 
* [Zero Ai](http://www.vertxai.cn): The auto script tools for zero full stack framework development.

## 2. Overlook

Here I provide a nother view to let you know Zero Framework. 

### 2.1. Modulat

**Zero Extension** has been designed and re-factor to modulat environment, you can configure the modules as required, here are some standard modules in Zero Extension.

![](/doc/_image/extension.png)

### 2.2. Topology

The whole **Zero Framework ( Aeon Environment )** is as following:

![](/doc/_image/arch.png)

## 3. Envrionment

* **Back-End**: You can download scaffold project from <https://github.com/silentbalanceyh/vertx-zero-scaffold> to initialize zero environment. 
* **Front-End**: You can use command `ai init -name` instead, refer [Front-End Initialize](http://www.vertxai.cn/document/doc-web/module-ai.html#.init). 

### 3.1. Configuration in pom.xml

If you want to use Zero framework, you can add following dependency into you `pom.xml` to use Zero:

**JDK 8**, vert.x 3.9.x

```xml
<parent>
    <artifactId>vertx-import</artifactId>
    <groupId>cn.vertxup</groupId>
    <version>0.6.2</version>
</parent>
```

**JDK 11+**, vert.x 4.x

```xml
<parent>
    <artifactId>vertx-import</artifactId>
    <groupId>cn.vertxup</groupId>
    <version>0.8.1</version>
</parent>
```

### 3.2. Start Up ( Core )

In your project, you can provide main entry only as following to run Zero \( Annotated with `@Up` \) .

```java
import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.Up;

@Up
public class Driver {

    public static void main(final String[] args) {
        VertxApplication.run(Driver.class);
    }
}
```

Once the Zero is up, you can see following logs in your console \( The default port is `6083` \):

```
[ ZERO ] ZeroHttpAgent Http Server has been started successfully. \
    Endpoint: http://0.0.0.0:6083/
```

### 3.3. Start Up ( Native Cloud )

When you want to enable **Aeon System**, you can switch the code as following:

```java
import io.vertx.aeon.AeonApplication;
import io.vertx.up.annotations.Up;

@Up
public class Driver {
    public static void main(final String[] args) {
        AeonApplication.run(Driver.class);
    }
}
```

## 4. Tips

### 4.1. Data Specification

In zero framework, we designed uniform data specification as following response data format for business usage:

```json
{
    "data":
}
```

After `0.5.2`, it could support freedom data format response as you wanted such as:

```shell
Hello World
1
...
```

If you want to switch to freedom mode, you can set the configuration in `vertx.yml` file:

```yaml
zero:
    freedom: true     # The default value of `freedom` is false.
```

### 4.2. Logging in Zero

You can use following function in your coding to get Logger component instead of `log4j` because we have re-designed the
detail implementation of logging system.

```java
// Zero Logger initialized, connect to vert.x logging system directly 
// but uniform managed by zero.

import io.vertx.up.log.Annal;

// Then in your class
public final class Statute {

    private static final Annal LOGGER = Annal.get(Statute.class);
    ......
}
```

### 4.3. Oracle Issue

From `0.8.0`, if you want to use Zero Extension of Dynamic Modeling, you need the project of <https://github.com/silentbalanceyh/vertx-zero/tree/master/vertx-pin/zero-vista>. The latest version is `0.8.0-SNAPSHOT`, you can modify the version and rebuild it. This feature is not needed in Core Framework.

> Above issue has been fixed in `0.8.1`.

## 5. Other Information

### 5.1. Cases List

> Because of Contract and Confidentiality Agreement, removed Sensitive information of customer include system name, customer name etc.

* **Deprecated**: Not Running Now
* **In Progress**: In Development or Upgraded Development
* **Running**: Running on Production Environment

| System Information                |Zero Version| System Status |
|-----------------------------------|---|---------------|
| TLK Video Mobile System           |0.4.6| Deprecated    |
| CMDB Platform of Bank             |Latest| Running       |
| ITSM/ITIL Platform of Bank        |Latest| Running |
| Integration of XBank              |Latest| Running       |
| Zero Training Demo                |Latest| Running       |
| Data Analyzing Exam               |Latest| Running       |
| Commercial Opportunity Management |Latest| Running |
| Hotel Management Platform         |Latest| In Progress   |
| ISO27000 Management Platform      |Latest| In Progress |
| IoT Control System                |Latest| In Progress |
| Medical Workflow Management       |Latest | In Progress |

### 5.2. WeChat Group

You can send request to me: `445191171` to contact with the author team of zero.

![](/doc/_image/chat.png)

## DESIGNED IN CHINA（中国工艺）



