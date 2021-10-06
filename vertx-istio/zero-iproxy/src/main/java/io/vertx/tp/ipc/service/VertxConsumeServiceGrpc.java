package io.vertx.tp.ipc.service;

import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.vertx.tp.ipc.service.ConsumeServiceGrpc.getServiceDescriptor;


@javax.annotation.Generated(
    value = "by VertxGrpc generator",
    comments = "Source: zero.def.service.proto")
public final class VertxConsumeServiceGrpc {
    private static final int METHODID_OUTPUT_CALL = 0;

    private VertxConsumeServiceGrpc() {
    }

    public static ConsumeServiceVertxStub newVertxStub(io.grpc.Channel channel) {
        return new ConsumeServiceVertxStub(channel);
    }

    public static final class ConsumeServiceVertxStub extends io.grpc.stub.AbstractStub<ConsumeServiceVertxStub> {
        private final io.vertx.core.impl.ContextInternal ctx;
        private ConsumeServiceGrpc.ConsumeServiceStub delegateStub;

        private ConsumeServiceVertxStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = ConsumeServiceGrpc.newStub(channel);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        private ConsumeServiceVertxStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = ConsumeServiceGrpc.newStub(channel).build(channel, callOptions);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        @Override
        protected ConsumeServiceVertxStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ConsumeServiceVertxStub(channel, callOptions);
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public io.vertx.core.streams.ReadStream<io.vertx.tp.ipc.eon.StreamServerResponse> outputCall(io.vertx.core.Handler<io.vertx.core.streams.WriteStream<io.vertx.tp.ipc.eon.StreamServerRequest>> hdlr) {
            return io.vertx.grpc.stub.ClientCalls.manyToMany(ctx, hdlr, delegateStub::outputCall);
        }
    }

    public static abstract class ConsumeServiceVertxImplBase implements io.grpc.BindableService {
        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public ConsumeServiceVertxImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public void outputCall(io.vertx.core.streams.ReadStream<io.vertx.tp.ipc.eon.StreamServerRequest> request, io.vertx.core.streams.WriteStream<io.vertx.tp.ipc.eon.StreamServerResponse> response) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    ConsumeServiceGrpc.METHOD_OUTPUT_CALL,
                    asyncBidiStreamingCall(
                        new MethodHandlers<
                            io.vertx.tp.ipc.eon.StreamServerRequest,
                            io.vertx.tp.ipc.eon.StreamServerResponse>(
                            this, METHODID_OUTPUT_CALL, compression)))
                .build();
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final ConsumeServiceVertxImplBase serviceImpl;
        private final int methodId;
        private final String compression;

        MethodHandlers(ConsumeServiceVertxImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_OUTPUT_CALL:
                    return (io.grpc.stub.StreamObserver<Req>) io.vertx.grpc.stub.ServerCalls.manyToMany(
                        (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse>) responseObserver,
                        compression,
                        serviceImpl::outputCall);
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
