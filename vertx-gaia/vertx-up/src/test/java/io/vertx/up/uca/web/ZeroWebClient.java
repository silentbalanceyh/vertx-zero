package io.vertx.up.uca.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;

public class ZeroWebClient {

    public void testClient() throws Exception {
        final CloseableHttpClient client = HttpClients.createDefault();

        final File file = new File("128-1.jpg");
        System.out.println(file.length());
        final HttpPost post = new HttpPost("http://localhost:6201/api/attachment/upload/hotel");
        final FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
        final HttpEntity entity = builder.build();
        post.setEntity(entity);
        final HttpResponse data = client.execute(post);

    }
}
