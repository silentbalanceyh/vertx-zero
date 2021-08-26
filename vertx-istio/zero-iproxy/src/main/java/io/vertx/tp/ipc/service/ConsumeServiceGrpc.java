package io.vertx.tp.ipc.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 *
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.6.1)",
    comments = "Source: zero.def.service.proto")
public final class ConsumeServiceGrpc {

    public static final String SERVICE_NAME = "io.vertx.tp.ipc.service.ConsumeService";
    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final io.grpc.MethodDescriptor<io.vertx.tp.ipc.eon.StreamServerRequest,
        io.vertx.tp.ipc.eon.StreamServerResponse> METHOD_OUTPUT_CALL =
        io.grpc.MethodDescriptor.<io.vertx.tp.ipc.eon.StreamServerRequest, io.vertx.tp.ipc.eon.StreamServerResponse>newBuilder()
            .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
            .setFullMethodName(generateFullMethodName(
                "io.vertx.tp.ipc.service.ConsumeService", "OutputCall"))
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                io.vertx.tp.ipc.eon.StreamServerRequest.getDefaultInstance()))
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                io.vertx.tp.ipc.eon.StreamServerResponse.getDefaultInstance()))
            .build();
    private static final int METHODID_OUTPUT_CALL = 0;
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private ConsumeServiceGrpc() {
    }

    private static <T> io.grpc.stub.StreamObserver<T> toObserver(final io.vertx.core.Handler<io.vertx.core.AsyncResult<T>> handler) {
        return new io.grpc.stub.StreamObserver<T>() {
            private volatile boolean resolved = false;

            @Override
            public void onNext(T value) {
                if (!resolved) {
                    resolved = true;
                    handler.handle(io.vertx.core.Future.succeededFuture(value));
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!resolved) {
                    resolved = true;
                    handler.handle(io.vertx.core.Future.failedFuture(t));
                }
            }

            @Override
            public void onCompleted() {
                if (!resolved) {
                    resolved = true;
                    handler.handle(io.vertx.core.Future.succeededFuture());
                }
            }
        };
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static ConsumeServiceStub newStub(io.grpc.Channel channel) {
        return new ConsumeServiceStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static ConsumeServiceBlockingStub newBlockingStub(
        io.grpc.Channel channel) {
        return new ConsumeServiceBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static ConsumeServiceFutureStub newFutureStub(
        io.grpc.Channel channel) {
        return new ConsumeServiceFutureStub(channel);
    }

    /**
     * Creates a new vertx stub that supports all call types for the service
     */
    public static ConsumeServiceVertxStub newVertxStub(io.grpc.Channel channel) {
        return new ConsumeServiceVertxStub(channel);
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (ConsumeServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                        .setSchemaDescriptor(new ConsumeServiceDescriptorSupplier())
                        .addMethod(METHOD_OUTPUT_CALL)
                        .build();
                }
            }
        }
        return result;
    }

    /**
     *
     */
    public static abstract class ConsumeServiceImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * Server -&gt; Client
         * </pre>
         */
        public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerRequest> outputCall(
            io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse> responseObserver) {
            return asyncUnimplementedStreamingCall(METHOD_OUTPUT_CALL, responseObserver);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    METHOD_OUTPUT_CALL,
                    asyncBidiStreamingCall(
                        new MethodHandlers<
                            io.vertx.tp.ipc.eon.StreamServerRequest,
                            io.vertx.tp.ipc.eon.StreamServerResponse>(
                            this, METHODID_OUTPUT_CALL)))
                .build();
        }
    }

    /**
     *
     */
    public static final class ConsumeServiceStub extends io.grpc.stub.AbstractStub<ConsumeServiceStub> {
        private ConsumeServiceStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ConsumeServiceStub(io.grpc.Channel channel,
                                   io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ConsumeServiceStub build(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            return new ConsumeServiceStub(channel, callOptions);
        }

        /**
         * <pre>
         * Server -&gt; Client
         * </pre>
         */
        public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerRequest> outputCall(
            io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse> responseObserver) {
            return asyncBidiStreamingCall(
                getChannel().newCall(METHOD_OUTPUT_CALL, getCallOptions()), responseObserver);
        }
    }

    /**
     *
     */
    public static final class ConsumeServiceBlockingStub extends io.grpc.stub.AbstractStub<ConsumeServiceBlockingStub> {
        private ConsumeServiceBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ConsumeServiceBlockingStub(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ConsumeServiceBlockingStub build(io.grpc.Channel channel,
                                                   io.grpc.CallOptions callOptions) {
            return new ConsumeServiceBlockingStub(channel, callOptions);
        }
    }

    /**
     *
     */
    public static final class ConsumeServiceFutureStub extends io.grpc.stub.AbstractStub<ConsumeServiceFutureStub> {
        private ConsumeServiceFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ConsumeServiceFutureStub(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ConsumeServiceFutureStub build(io.grpc.Channel channel,
                                                 io.grpc.CallOptions callOptions) {
            return new ConsumeServiceFutureStub(channel, callOptions);
        }
    }

    /**
     *
     */
    public static abstract class ConsumeServiceVertxImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * Server -&gt; Client
         * </pre>
         */
        public void outputCall(
            io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamServerRequest, io.vertx.tp.ipc.eon.StreamServerResponse> exchange) {
            exchange.setReadObserver(asyncUnimplementedStreamingCall(METHOD_OUTPUT_CALL, exchange.writeObserver()));
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    METHOD_OUTPUT_CALL,
                    asyncBidiStreamingCall(
                        new VertxMethodHandlers<
                            io.vertx.tp.ipc.eon.StreamServerRequest,
                            io.vertx.tp.ipc.eon.StreamServerResponse>(
                            this, METHODID_OUTPUT_CALL)))
                .build();
        }
    }

    /**
     *
     */
    public static final class ConsumeServiceVertxStub extends io.grpc.stub.AbstractStub<ConsumeServiceVertxStub> {
        private ConsumeServiceVertxStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ConsumeServiceVertxStub(io.grpc.Channel channel,
                                        io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ConsumeServiceVertxStub build(io.grpc.Channel channel,
                                                io.grpc.CallOptions callOptions) {
            return new ConsumeServiceVertxStub(channel, callOptions);
        }

        /**
         * <pre>
         * Server -&gt; Client
         * </pre>
         */
        public void outputCall(io.vertx.core.Handler<
            io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamServerResponse, io.vertx.tp.ipc.eon.StreamServerRequest>> handler) {
            final io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamServerResponse> readStream =
                io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamServerResponse>create();

            handler.handle(io.vertx.grpc.GrpcBidiExchange.create(readStream, asyncBidiStreamingCall(
                getChannel().newCall(METHOD_OUTPUT_CALL, getCallOptions()), readStream.readObserver())));
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final ConsumeServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(ConsumeServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
            io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_OUTPUT_CALL:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.outputCall(
                        (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class VertxMethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final ConsumeServiceVertxImplBase serviceImpl;
        private final int methodId;

        VertxMethodHandlers(ConsumeServiceVertxImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
            io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_OUTPUT_CALL:
                    io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamServerRequest> request0 = io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamServerRequest>create();
                    serviceImpl.outputCall(
                        io.vertx.grpc.GrpcBidiExchange.<io.vertx.tp.ipc.eon.StreamServerRequest, io.vertx.tp.ipc.eon.StreamServerResponse>create(
                            request0,
                            (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse>) responseObserver));
                    return (io.grpc.stub.StreamObserver<Req>) request0.readObserver();
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class ConsumeServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return io.vertx.tp.ipc.service.UpIpcService.getDescriptor();
        }
    }
}
