package io.vertx.up.backbone.mime;

import io.horizon.exception.WebException;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.mime.parse.EpsilonIncome;
import io.vertx.up.backbone.mime.parse.Income;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

public class MediaAnalyzer implements Analyzer {

    private static final Annal LOGGER = Annal.get(MediaAnalyzer.class);
    private static final Cc<String, Income<List<Epsilon<Object>>>> CC_EPSILON = Cc.openThread();

    @Override
    public Object[] in(final RoutingContext context,
                       final Event event)
        throws WebException {
        /* Consume mime type matching **/
        final MediaType requestMedia = this.getMedia(context);
        MediaAtom.accept(event, requestMedia);

        /* Extract definition from method **/
        final Income<List<Epsilon<Object>>> income = CC_EPSILON.pick(EpsilonIncome::new); // Fn.po?lThread(POOL_EPSILON, EpsilonIncome::new);
        final List<Epsilon<Object>> epsilons = income.in(context, event);

        /* Extract value list **/
        return epsilons.stream()
            .map(Epsilon::getValue).toArray();
    }

    @Override
    public Envelop out(final Envelop envelop,
                       final Event event) throws WebException {
        // TODO: Replier
        return null;
    }

    private MediaType getMedia(final RoutingContext context) {
        final String header = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
        return Fn.runOr(Ut.isNil(header), LOGGER,
            () -> MediaType.WILDCARD_TYPE,
            () -> MediaType.valueOf(header));
    }

}
