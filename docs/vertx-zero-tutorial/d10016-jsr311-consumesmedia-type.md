# JSR311, @Consumes...Media Type

Zero system support content negotiation in restful web service application as web framework, here we
used `javax.ws.rs.Consumes` annotation to focus on http header `Content-Type`, it means that you must provide this
header value in your request.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.media;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.legacy.VValue;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@EndPoint
@Path("/api")
public class ContentActor {

    @POST
    @Path("media/json")
    @Consumes(MediaType.APPLICATION_JSON)
    public String sayJson(final JsonObject data) {
        return data.encode();
    }

    @POST
    @Path("media/xml")
    @Consumes(MediaType.APPLICATION_XML)
    public String sayXml(final byte[] data) {
        final String xml = new String(data, Values.CHARSET);
        System.out.println(xml);
        return xml;
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.media.ContentActor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/media/json" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/media/xml" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Error Request

**URL**: [http://localhost:6083/api/media/json](http://localhost:6083/api/media/json)

**Method** : POST

**Headers** :

```
Content-Type: application/xml
```

**Response** : \( _Status Code = 415_ \)

```json
{
    "code": -60006,
    "message": "[ERR-60006] (MediaAtom) Web Exception occus: (415) - Server could not accept the mime \"application/xml\", expected should be one of application/json."
}
```

### 3.2. Json Request

**URL**: [http://localhost:6083/api/media/json](http://localhost:6083/api/media/json)

**Method** : POST

**Headers **:

```
Content-Type: application/json
```

**Request**:

```json
{
    "username":"request"
}
```

**Response** :

```json
{
    "data": "{\"username\":\"request\"}"
}
```

### 3.3. Xml Request

_Xml resolver of media type is still in development, in current situation there is no business requirement that related
to application/xml media type, so this kind of resolution will be put in future plan._

# 4. Summary

Although in current version zero system does not support some complex media type, but for future plan following media
type will be put into zero and it depend on some business requirements, please check following supported list in current
version of zero to know.

* [ ] application/xml
* [ ] application/atom+xml
* [ ] application/xhtml+xml
* [ ] application/svg+xml
* [x] application/json
* [x] application/x-www-form-urlencoded
* [x] multipart/form-data
* [ ] application/octet-stream
* [x] text/plain
* [ ] text/xml
* [ ] text/html
* [ ] text/event-stream
* [ ] application/json-patch+json



