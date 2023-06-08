package io.vertx.mod.ipc.service;

import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.vertx.mod.ipc.service.UnityServiceGrpc.getServiceDescriptor;


@javax.annotation.Generated(
    value = "by VertxGrpc generator",
    comments = "Source: zero.def.service.proto")
public final class VertxUnityServiceGrpc {
    private static final int METHODID_UNITY_CALL = 0;

    private VertxUnityServiceGrpc() {
    }

    public static UnityServiceVertxStub newVertxStub(io.grpc.Channel channel) {
        return new UnityServiceVertxStub(channel);
    }

    public static final class UnityServiceVertxStub extends io.grpc.stub.AbstractStub<UnityServiceVertxStub> {
        private final io.vertx.core.impl.ContextInternal ctx;
        private UnityServiceGrpc.UnityServiceStub delegateStub;

        private UnityServiceVertxStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = UnityServiceGrpc.newStub(channel);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        private UnityServiceVertxStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = UnityServiceGrpc.newStub(channel).build(channel, callOptions);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        @Override
        protected UnityServiceVertxStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new UnityServiceVertxStub(channel, callOptions);
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public io.vertx.core.Future<io.vertx.mod.ipc.eon.IpcResponse> unityCall(io.vertx.mod.ipc.eon.IpcRequest request) {
            return io.vertx.grpc.stub.ClientCalls.oneToOne(ctx, request, delegateStub::unityCall);
        }

    }

    public static abstract class UnityServiceVertxImplBase implements io.grpc.BindableService {
        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public UnityServiceVertxImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        /**
         * <pre>
         *  Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public io.vertx.core.Future<io.vertx.mod.ipc.eon.IpcResponse> unityCall(io.vertx.mod.ipc.eon.IpcRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    UnityServiceGrpc.METHOD_UNITY_CALL,
                    asyncUnaryCall(
                        new MethodHandlers<
                            io.vertx.mod.ipc.eon.IpcRequest,
                            io.vertx.mod.ipc.eon.IpcResponse>(
                            this, METHODID_UNITY_CALL, compression)))
                .build();
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final UnityServiceVertxImplBase serviceImpl;
        private final int methodId;
        private final String compression;

        MethodHandlers(UnityServiceVertxImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_UNITY_CALL:
                    io.vertx.grpc.stub.ServerCalls.oneToOne(
                        (io.vertx.mod.ipc.eon.IpcRequest) request,
                        (io.grpc.stub.StreamObserver<io.vertx.mod.ipc.eon.IpcResponse>) responseObserver,
                        compression,
                        serviceImpl::unityCall);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
