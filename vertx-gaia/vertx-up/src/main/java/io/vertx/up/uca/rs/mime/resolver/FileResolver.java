package io.vertx.up.uca.rs.mime.resolver;

import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.mime.Resolver;
import io.vertx.up.unity.Ux;

import java.util.Set;

public class FileResolver<T> implements Resolver<T> {

    private static final Annal LOGGER = Annal.get(FileResolver.class);

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income) {
        final Set<FileUpload> fileUploads = context.fileUploads();
        /*
         * Not needed to group `Set<FileUpload>`
         */
        final Class<?> argType = income.getArgType();
        final FileSystem fileSystem = context.vertx().fileSystem();
        final T result = Ux.toFile(fileUploads, argType, fileSystem::readFileBlocking);
        income.setValue(result);
        return income;
    }
}
