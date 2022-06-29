package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.secure.bridge.Bolt;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.matcher.RegexPath;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerAuthorize extends AbstractMixer {
    private static final Cc<String, Set<Aegis>> CC_WALLS = ZeroAnno.getWalls();
    private static final AtomicBoolean LOG_FOUND = new AtomicBoolean(Boolean.TRUE);
    private static final AtomicBoolean LOG_PROVIDER = new AtomicBoolean(Boolean.TRUE);

    private transient final Bolt bolt;

    public MixerAuthorize(final Vertx vertx) {
        super(vertx);
        this.bolt = Bolt.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T mount(final StompServerHandler handler, final StompServerOptions option) {
        // Stomp Path Find
        final String stomp = option.getWebsocketPath();
        final AtomicReference<Aegis> reference = new AtomicReference<>();
        CC_WALLS.store().data().forEach((path, aegisSet) -> {
            /*
             * Stomp:   /api/web-socket/stomp
             * Path:    /api/
             */
            final Pattern regexPath = RegexPath.createRegex(path);
            if (!aegisSet.isEmpty() && regexPath.matcher(stomp).matches()) {
                if (LOG_FOUND.getAndSet(Boolean.FALSE)) {
                    this.logger().info(Info.SECURE_FOUND, stomp, path, String.valueOf(aegisSet.size()));
                }
                reference.set(aegisSet.iterator().next());
            }
        });
        final Aegis config = reference.get();
        if (Objects.nonNull(config)) {
            final AuthenticationProvider provider = this.bolt.authenticateProvider(this.vertx, config);
            if (LOG_PROVIDER.getAndSet(Boolean.FALSE)) {
                this.logger().info(Info.SECURE_PROVIDER, provider.getClass());
            }
            handler.authProvider(provider);
        }
        return this.finished(config);
    }
}
