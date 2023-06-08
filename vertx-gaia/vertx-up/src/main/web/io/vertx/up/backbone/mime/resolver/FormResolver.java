package io.vertx.up.backbone.mime.resolver;

import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;
import io.vertx.up.unity.Ux;

import java.util.HashSet;
import java.util.Set;

public class FormResolver<T> implements Resolver<T> {

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income) {
        final Set<FileUpload> fileUploads = new HashSet<>(context.fileUploads());
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
