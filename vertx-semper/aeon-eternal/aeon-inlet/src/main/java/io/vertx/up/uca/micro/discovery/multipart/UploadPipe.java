package io.vertx.up.uca.micro.discovery.multipart;

import io.vertx.core.Handler;
import io.vertx.core.http.*;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.discovery.InOut;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class UploadPipe implements Pipe<HttpResponse> {

    private static final Annal LOGGER = Annal.get(UploadPipe.class);

    private final transient RoutingContext context;
    private final transient ServiceReference reference;
    private final transient RequestOptions options;
    // Secondary reference
    private final transient HttpServerRequest request;
    private final transient HttpServerResponse response;

    private UploadPipe(final RoutingContext context,
                       final ServiceReference reference,
                       final RequestOptions options) {
        this.context = context;
        /*
         * Extract request / response from context
         */
        this.request = context.request();
        this.response = context.response();
        this.reference = reference;
        this.options = options;
    }

    public static UploadPipe create(final RoutingContext context,
                                    final ServiceReference reference,
                                    final RequestOptions options) {
        return new UploadPipe(context, reference, options);
    }


    @Override
    public void doRequest(final Handler<HttpResponse> handler) {
        /*
         * Only support post method in uploading HttpPipe
         */
        if (HttpMethod.POST == this.request.method() &&
            this.request.isExpectMultipart()) {
            /*
             * https://github.com/vert-x/vertx-examples/blob/master/src/raw/java/upload/UploadClient.java
             * Preparing for uploading
             */
            final FileUpload fileUpload = this.getFile();
            if (null == fileUpload) {
                /*
                 * It means that the system met error when multipart request happened.
                 */
                InOut.sync500Error(this.getClass(), this.context, new RuntimeException("Upload file missing..."));
            } else {
                /*
                 * Execute uploading processing
                 */
                this.executeUpload(fileUpload, handler);
            }
        } else {
            InOut.sync405Error(this.getClass(), this.context);
        }
    }

    private void executeUpload(final FileUpload fileUpload,
                               final Handler<HttpResponse> handler) {
        try {
            final String filename = fileUpload.uploadedFileName();
            /*
             * async file reading
             */
            final File file = new File(filename);
            if (file.exists()) {
                /*
                 * Because of issue:
                 * https://github.com/vert-x3/vertx-web/issues/1137
                 * Here find another solution
                 */
                final CloseableHttpClient client = HttpClients.createDefault();

                final String uri = this.remoteUri();
                final HttpPost post = new HttpPost(uri);
                /*
                 * Http Headers
                 */
                this.processHeaders(post);
                /*
                 * Files uploaded here
                 */
                this.processMultipart(post, fileUpload, file);
                /*
                 * Execute request here
                 */
                for (final Header header : post.getAllHeaders()) {
                    LOGGER.info("Normalized header: key = {0}, value = {1}.", header.getName(), header.getValue());
                }
                final HttpResponse data = client.execute(post);
                /*
                 * Http Response to HttpClientResponse
                 */
                handler.handle(data);
            }
        } catch (final Exception ex) {
            // TODO: Exception for debug
            ex.printStackTrace();
            InOut.sync500Error(this.getClass(), this.context, ex);
        }
    }

    private String remoteUri() {
        final String uri = InOut.normalizeUri(this.context);
        final StringBuilder absoluteUri = new StringBuilder();
        absoluteUri.append("http://").append(this.options.getHost());
        absoluteUri.append(":").append(this.options.getPort());
        absoluteUri.append(uri);
        final String remoteUri = absoluteUri.toString();
        LOGGER.info("Multipart redirect uri: {0}", remoteUri);
        return remoteUri;
    }

    private void processMultipart(final HttpPost request, final FileUpload fileUpload, final File file) {
        final ContentType contentType = ContentType.create(fileUpload.contentType());
        LOGGER.info("Read file from file system: {0}", file.getAbsolutePath());
        final FileBody fileBody = new FileBody(file, contentType);

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /*
         * Custom content disposition
         * Because Api Gateway will send the request to micro service
         * It means that the content-disposition must be calculated by api gateway internally
         * here.
         * Now it's working to redirect request to micro service.
         * Related submitted issue:
         * https://github.com/vert-x3/vertx-web/issues/1137
         */
        final String disposition = "form-data; name=\"file\"; "
            + "filename=\"" + fileUpload.fileName() + "";
        final FormBodyPart bodyPart = FormBodyPartBuilder.create()
            .setName("file")
            .addField("Content-Type", fileUpload.contentType())
            .addField("Content-Transfer-Encoding", fileUpload.contentTransferEncoding())
            .addField("Content-Disposition", disposition)
            .setBody(fileBody)
            .build();
        builder.addPart(bodyPart);
        final HttpEntity entity = builder.build();
        request.setEntity(entity);
    }

    private void processHeaders(final HttpPost request) {
        /*
         * Extract Server Request headers
         */
        this.request.headers().forEach(entry ->
            LOGGER.info("Client header: {0} = {1}", entry.getKey(), entry.getValue()));
        /*
         * User Agent Switch, Fix value
         */
        this.processHeader(request, HttpHeaders.AUTHORIZATION);  // Security
        this.processHeader(request, HttpHeaders.CACHE_CONTROL);  // Cache
        this.processHeader(request, HttpHeaders.ACCEPT);         // Accept
        this.processHeader(request, HttpHeaders.CONNECTION);     // Connection
        this.processHeader(request, HttpHeaders.ACCEPT_ENCODING);// Accept-Encoding
    }

    private void processHeader(final HttpPost request, final CharSequence key) {
        if (this.request.headers().contains(key)) {
            // Security header
            request.setHeader(key.toString(),
                this.request.headers().get(key));
        }
    }

    private FileUpload getFile() {
        final Set<FileUpload> fileUploads = new HashSet<>(this.context.fileUploads());
        if (fileUploads.isEmpty()) {
            return null;
        } else {
            return fileUploads.iterator().next();
        }
    }
}
