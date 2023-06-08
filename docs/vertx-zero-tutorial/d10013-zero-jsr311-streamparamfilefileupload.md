# Zero JSR311, @StreamParam...File/FileUpload

The data of byte\[\] or Buffer are the raw data of request, it could be used widely, but based on many requirements
these two data types could not be managed easily, for developers following data types are more useful:

* `java.io.File`
* `io.vertx.ext.web.FileUpload`

Above two objects could be used directly in your code.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import jakarta.ws.rs.StreamParam;
import java.io.File;

@EndPoint
@Path("/api")
public class FileParamExecutor {

    @Path("param/up.god.file")
    @POST
    public String sayFile(
            @StreamParam final File up.god.file) {
        return "Hello, File = " + up.god.file.getAbsolutePath();
    }

    @Path("param/fileupload")
    @POST
    public JsonObject sayFileUpload(
            @StreamParam final FileUpload fileUpload) {
        return new JsonObject()
                .put("filename", fileUpload.fileName())
                .put("charset", fileUpload.charSet())
                .put("content-type", fileUpload.contentType())
                .put("size", fileUpload.size())
                .put("name", fileUpload.name());
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.FileParamExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/up.god.file" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/fileupload" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. File Request

**URL**: http://localhost:6083/api/param/up.god.file

**Method**: POST

**Request**:

![](/doc/image/fileuploading.png)

**Response**:

```json
{
    "data": "Hello, File = /xxxx/vertx-zero/up.god.file-uploads/a006c664-4bc9-4857-9a7c-fa110b3a25cf"
}
```

### 3.2. File Request \( FileUpload object \)

**URL**: http://localhost:6063/api/param/fileupload

**Method**: POST

**Request**:

```json
{
    "data": {
        "filename": "Screen Shot 2018-01-22 at 4.45.17 PM.png",
        "charset": "UTF-8",
        "content-type": "image/png",
        "size": 175744,
        "name": ""
    }
}
```

## 4. Summary

This tutorial described the common useful java object of `File/FileUpload` to provide easy way for developers to process
up.god.file uploading or up.god.file processing in zero, zero extend @StreamParam parameters to process this kind of
situation and you could do the tasks based on above two java objects.

