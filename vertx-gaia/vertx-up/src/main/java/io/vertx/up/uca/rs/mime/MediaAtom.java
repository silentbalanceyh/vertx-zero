package io.vertx.up.uca.rs.mime;

import io.vertx.up.atom.agent.Event;
import io.horizon.exception.WebException;
import io.vertx.up.exception.web._415MediaNotSupportException;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

final class MediaAtom {

    private static final Annal LOGGER = Annal.get(MediaAtom.class);

    static void accept(final Event event,
                       final MediaType type) throws WebException {
        final Set<MediaType> medias = event.getConsumes();
        if (!medias.contains(MediaType.WILDCARD_TYPE)) {
            /* 1. Start to parsing expected type **/
            boolean match = medias.stream()
                .anyMatch(media ->
                    MediaType.MEDIA_TYPE_WILDCARD.equals(media.getType()) ||
                        media.getType().equalsIgnoreCase(type.getType()));
            /* 2. Type checking **/
            Fn.outUp(!match, LOGGER,
                _415MediaNotSupportException.class,
                MediaAtom.class, type, medias);
            /* 3. Start to parsing expected sub type **/
            match = medias.stream()
                .anyMatch(media ->
                    MediaType.MEDIA_TYPE_WILDCARD.equals(media.getSubtype()) ||
                        media.getSubtype().equalsIgnoreCase(type.getSubtype())
                );
            /* 4. Subtype checking **/
            Fn.outUp(!match, LOGGER,
                _415MediaNotSupportException.class,
                MediaAtom.class, type, medias);
        }
    }
}
