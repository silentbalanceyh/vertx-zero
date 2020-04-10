package io.vertx.up.uca.rs.mime.resolver;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.uca.rs.mime.Resolver;

import java.io.File;
import java.util.Set;

@SuppressWarnings("unchecked")
public class FileResolver<T> implements Resolver<T> {

    private static final Annal LOGGER = Annal.get(FileResolver.class);

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income) {
        context.request().headers().forEach((item) -> LOGGER.debug("Headers: {0} = {1}",
                item.getKey(), item.getValue()));
        final Set<FileUpload> fileUploads = context.fileUploads();
        LOGGER.info("Upload files: size = {0}", fileUploads.size());
        if (Values.ONE == fileUploads.size()) {
            final FileUpload fileUpload = fileUploads.iterator().next();
            // Returned directly reference for FileUpload
            if (income.getArgType().isAssignableFrom(FileUpload.class)) {
                income.setValue((T) fileUpload);
            } else if (income.getArgType() == File.class) {
                // File object construction
                final Object ret = ZeroSerializer.getValue(income.getArgType(), fileUpload.uploadedFileName());
                income.setValue((T) ret);
            } else if (income.getArgType().isArray()) {
                // byte[]
                if (byte.class == income.getArgType().getComponentType() ||
                        Byte.class == income.getArgType().getComponentType()) {
                    final FileSystem fileSystem = context.vertx().fileSystem();
                    final Buffer buffer = fileSystem.readFileBlocking(fileUpload.uploadedFileName());
                    income.setValue((T) buffer.getBytes());
                }
            } else if (Buffer.class.isAssignableFrom(income.getArgType())) {
                // Buffer
                final FileSystem fileSystem = context.vertx().fileSystem();
                final Buffer buffer = fileSystem.readFileBlocking(fileUpload.uploadedFileName());
                income.setValue((T) buffer);
            }
        } else {
            // Multi Files only support Set<FileUpload>
            income.setValue((T) fileUploads);
        }
        return income;
    }
}
