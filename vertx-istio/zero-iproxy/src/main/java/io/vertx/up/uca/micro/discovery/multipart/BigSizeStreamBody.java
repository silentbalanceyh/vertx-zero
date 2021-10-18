package io.vertx.up.uca.micro.discovery.multipart;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.InputStream;

public class BigSizeStreamBody extends InputStreamBody {
    private final long contentLength;

    public BigSizeStreamBody(final InputStream in,
                             final long contentLength,
                             final ContentType contentType) {
        super(in, contentType);
        this.contentLength = contentLength;
    }

    @Override
    public long getContentLength() {
        return this.contentLength;
    }
}
