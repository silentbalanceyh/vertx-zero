package io.vertx.up.uca.rs.mime;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.mime.parse.EpsilonIncome;
import io.vertx.up.uca.rs.mime.parse.Income;
import io.vertx.up.util.Ut;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MediaAnalyzer implements Analyzer {

    private static final Annal LOGGER = Annal.get(MediaAnalyzer.class);

    private static final ConcurrentMap<String, Income<List<Epsilon<Object>>>> POOL_EPSILON = new ConcurrentHashMap<>();

    @Override
    public Object[] in(final RoutingContext context,
                       final Event event)
        throws WebException {
        /* Consume mime type matching **/
        final MediaType requestMedia = this.getMedia(context);
        MediaAtom.accept(event, requestMedia);

        /* Extract definition from method **/
        final Income<List<Epsilon<Object>>> income = Fn.poolThread(POOL_EPSILON, EpsilonIncome::new);
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
        return Fn.getSemi(Ut.isNil(header), LOGGER,
            () -> MediaType.WILDCARD_TYPE,
            () -> MediaType.valueOf(header));
    }

}
