package io.vertx.mod.ipc.service;

import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.vertx.mod.ipc.service.ProduceServiceGrpc.getServiceDescriptor;


@javax.annotation.Generated(
    value = "by VertxGrpc generator",
    comments = "Source: zero.def.service.proto")
public final class VertxProduceServiceGrpc {
    private static final int METHODID_INPUT_CALL = 0;

    private VertxProduceServiceGrpc() {
    }

    public static ProduceServiceVertxStub newVertxStub(io.grpc.Channel channel) {
        return new ProduceServiceVertxStub(channel);
    }

    public static final class ProduceServiceVertxStub extends io.grpc.stub.AbstractStub<ProduceServiceVertxStub> {
        private final io.vertx.core.impl.ContextInternal ctx;
        private ProduceServiceGrpc.ProduceServiceStub delegateStub;

        private ProduceServiceVertxStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = ProduceServiceGrpc.newStub(channel);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        private ProduceServiceVertxStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = ProduceServiceGrpc.newStub(channel).build(channel, callOptions);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        @Override
        protected ProduceServiceVertxStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ProduceServiceVertxStub(channel, callOptions);
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public io.vertx.core.streams.ReadStream<io.vertx.mod.ipc.eon.StreamClientResponse> inputCall(io.vertx.core.Handler<io.vertx.core.streams.WriteStream<io.vertx.mod.ipc.eon.StreamClientRequest>> hdlr) {
            return io.vertx.grpc.stub.ClientCalls.manyToMany(ctx, hdlr, delegateStub::inputCall);
        }
    }

    public static abstract class ProduceServiceVertxImplBase implements io.grpc.BindableService {
        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public ProduceServiceVertxImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public void inputCall(io.vertx.core.streams.ReadStream<io.vertx.mod.ipc.eon.StreamClientRequest> request, io.vertx.core.streams.WriteStream<io.vertx.mod.ipc.eon.StreamClientResponse> response) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    ProduceServiceGrpc.METHOD_INPUT_CALL,
                    asyncBidiStreamingCall(
                        new MethodHandlers<
                            io.vertx.mod.ipc.eon.StreamClientRequest,
                            io.vertx.mod.ipc.eon.StreamClientResponse>(
                            this, METHODID_INPUT_CALL, compression)))
                .build();
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final ProduceServiceVertxImplBase serviceImpl;
        private final int methodId;
        private final String compression;

        MethodHandlers(ProduceServiceVertxImplBase serviceImpl, int methodId, String compression) {
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
                case METHODID_INPUT_CALL:
                    return (io.grpc.stub.StreamObserver<Req>) io.vertx.grpc.stub.ServerCalls.manyToMany(
                        (io.grpc.stub.StreamObserver<io.vertx.mod.ipc.eon.StreamClientResponse>) responseObserver,
                        compression,
                        serviceImpl::inputCall);
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
