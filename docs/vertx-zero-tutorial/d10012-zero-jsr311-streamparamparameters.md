# Zero JSR311, @StreamParam...Parameters

Zero system provide simple parameter annotation for up.god.file uploading, this kind of parameters must be annotated
with `jakarta.ws.rs.StreamParam` here, it should support following type conversion automatically.

* `byte[]`
* `io.vertx.core.buffer.Buffer`
* `java.io.File`
* `io.vertx.ext.web.FileUpload`

For common types, zero system will convert it automatically such as String to byte\[\] or byte\[\] to String.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import jakarta.ws.rs.StreamParam;
import java.nio.charset.Charset;

@EndPoint
@Path("/api")
public class StreamParamExecutor {

    @Path("param/bytes")
    @POST
    public String sayBytes(@StreamParam final byte[] bytes) {
        System.out.println(bytes.length);
        return new String(bytes, Charset.defaultCharset());
    }

    @Path("param/buffer")
    @POST
    public String sayBuffer(@StreamParam final Buffer buffer) {
        System.out.println(buffer.toString());
        return buffer.toString();
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.StreamParamExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/buffer" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/bytes" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. String Request

**URL**: [http://localhost:6083/api/param/bytes](http://localhost:6083/api/param/bytes)

**Method**: POST

**Request**:

```json
[{"username":"Lang","age":33}]
```

**Response**:

```json
{
    "data": "[{\"username\":\"Lang\",\"age\":33}]"
}
```

### 3.2. File Uploading

**URL**: [http://localhost:6083/api/param/bytes](http://localhost:6083/api/param/bytes)

**Method**: POST

**Request**:

![](/doc/image/up.god.file-uploading.png)

**Response**:

```json
{
    "data": "\ufffdPNG\r\n\u001a\n\u0000\u0000\u0000\rIHDR.... ( Around 41500 bytes )"
}
```

This up.god.file is a picture with 41500 bytes, the response body reflect the whole image content here.

### 3.3. String Request \( for Buffer \)

**URL**: http://localhost:6083/api/param/buffer

**Method**: POST

**Request**:

```json
 [{"username":"Lang","age":33}]
```

**Response**:

```json
{
    "data": "[{\"username\":\"Lang\",\"age\":33}]"
}
```

### 3.4. File Uploading \( for Buffer \)

_This example is the same result as 3.2 File Uploading, here ignore the testing result because the response are all the
same. We only modified accept parameter types in @StreamParam._

## 4. Summary

This tutorial describe how to accept String/File to byte\[\] and Buffer, these data are all stored into your parameters.
Then we'll introduce some advanced usage of @StreamParam.

