package io.vertx.core.eventbus;

import io.horizon.eon.VValue;
import io.vertx.core.buffer.Buffer;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

/**
 * Codec to transfer envelop
 */
public final class EnvelopCodec implements MessageCodec<Envelop, Envelop> {

    @Override
    public void encodeToWire(final Buffer buffer,
                             final Envelop message) {
        buffer.appendBytes(Ut.toBytes(message));
    }

    @Override
    public Envelop decodeFromWire(final int i,
                                  final Buffer buffer) {
        return Ut.fromBuffer(i, buffer);
    }

    @Override
    public Envelop transform(final Envelop message) {
        return message;
    }

    @Override
    public String name() {
        return this.getClass().getName();
    }

    @Override
    public byte systemCodecID() {
        return VValue.CODECS;
    }
}
