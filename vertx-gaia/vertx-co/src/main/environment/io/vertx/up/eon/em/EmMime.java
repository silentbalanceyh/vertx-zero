package io.vertx.up.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmMime {
    private EmMime() {
    }

    public enum Flow {
        RESOLVER,
        TYPED,
        STANDARD
    }

    public enum Type {
        /**
         * Text
         **/
        TEXT,
        /**
         * Multipart
         **/
        MULTIPART,
        /**
         * EmApp
         **/
        APPLICATION,
        /**
         * Message
         **/
        MESSAGE,
        /**
         * Image
         **/
        IMAGE,
        /**
         * Audio
         **/
        AUDIO,
        /**
         * Video
         **/
        VIDEO
    }
}
