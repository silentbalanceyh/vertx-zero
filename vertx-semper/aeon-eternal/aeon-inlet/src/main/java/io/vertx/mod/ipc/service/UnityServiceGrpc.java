package io.vertx.mod.ipc.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 *
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.6.1)",
    comments = "Source: zero.def.service.proto")
public final class UnityServiceGrpc {

    private UnityServiceGrpc() {
    }

    public static final String SERVICE_NAME = "io.vertx.mod.ipc.service.UnityService";

    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final io.grpc.MethodDescriptor<io.vertx.mod.ipc.eon.IpcRequest,
        io.vertx.mod.ipc.eon.IpcResponse> METHOD_UNITY_CALL =
        io.grpc.MethodDescriptor.<io.vertx.mod.ipc.eon.IpcRequest, io.vertx.mod.ipc.eon.IpcResponse>newBuilder()
            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
            .setFullMethodName(generateFullMethodName(
                "io.vertx.mod.ipc.service.UnityService", "UnityCall"))
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                io.vertx.mod.ipc.eon.IpcRequest.getDefaultInstance()))
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                io.vertx.mod.ipc.eon.IpcResponse.getDefaultInstance()))
            .build();

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static UnityServiceStub newStub(io.grpc.Channel channel) {
        return new UnityServiceStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static UnityServiceBlockingStub newBlockingStub(
        io.grpc.Channel channel) {
        return new UnityServiceBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static UnityServiceFutureStub newFutureStub(
        io.grpc.Channel channel) {
        return new UnityServiceFutureStub(channel);
    }

    /**
     *
     */
    public static abstract class UnityServiceImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public void unityCall(io.vertx.mod.ipc.eon.IpcRequest request,
                              io.grpc.stub.StreamObserver<io.vertx.mod.ipc.eon.IpcResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_UNITY_CALL, responseObserver);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    METHOD_UNITY_CALL,
                    asyncUnaryCall(
                        new MethodHandlers<
                            io.vertx.mod.ipc.eon.IpcRequest,
                            io.vertx.mod.ipc.eon.IpcResponse>(
                            this, METHODID_UNITY_CALL)))
                .build();
        }
    }

    /**
     *
     */
    public static final class UnityServiceStub extends io.grpc.stub.AbstractStub<UnityServiceStub> {
        private UnityServiceStub(io.grpc.Channel channel) {
            super(channel);
        }

        private UnityServiceStub(io.grpc.Channel channel,
                                 io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected UnityServiceStub build(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            return new UnityServiceStub(channel, callOptions);
        }

        /**
         * <pre>
         * Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public void unityCall(io.vertx.mod.ipc.eon.IpcRequest request,
                              io.grpc.stub.StreamObserver<io.vertx.mod.ipc.eon.IpcResponse> responseObserver) {
            asyncUnaryCall(
                getChannel().newCall(METHOD_UNITY_CALL, getCallOptions()), request, responseObserver);
        }
    }

    /**
     *
     */
    public static final class UnityServiceBlockingStub extends io.grpc.stub.AbstractStub<UnityServiceBlockingStub> {
        private UnityServiceBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private UnityServiceBlockingStub(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected UnityServiceBlockingStub build(io.grpc.Channel channel,
                                                 io.grpc.CallOptions callOptions) {
            return new UnityServiceBlockingStub(channel, callOptions);
        }

        /**
         * <pre>
         * Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public io.vertx.mod.ipc.eon.IpcResponse unityCall(io.vertx.mod.ipc.eon.IpcRequest request) {
            return blockingUnaryCall(
                getChannel(), METHOD_UNITY_CALL, getCallOptions(), request);
        }
    }

    /**
     *
     */
    public static final class UnityServiceFutureStub extends io.grpc.stub.AbstractStub<UnityServiceFutureStub> {
        private UnityServiceFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private UnityServiceFutureStub(io.grpc.Channel channel,
                                       io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected UnityServiceFutureStub build(io.grpc.Channel channel,
                                               io.grpc.CallOptions callOptions) {
            return new UnityServiceFutureStub(channel, callOptions);
        }

        /**
         * <pre>
         * Direct: Client -&gt; Server -&gt; Client
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<io.vertx.mod.ipc.eon.IpcResponse> unityCall(
            io.vertx.mod.ipc.eon.IpcRequest request) {
            return futureUnaryCall(
                getChannel().newCall(METHOD_UNITY_CALL, getCallOptions()), request);
        }
    }

    private static final int METHODID_UNITY_CALL = 0;

    private static final class MethodHandlers<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final UnityServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(UnityServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_UNITY_CALL:
                    serviceImpl.unityCall((io.vertx.mod.ipc.eon.IpcRequest) request,
                        (io.grpc.stub.StreamObserver<io.vertx.mod.ipc.eon.IpcResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
            io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class UnityServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return io.vertx.mod.ipc.service.UpIpcService.getDescriptor();
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (UnityServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                        .setSchemaDescriptor(new UnityServiceDescriptorSupplier())
                        .addMethod(METHOD_UNITY_CALL)
                        .build();
                }
            }
        }
        return result;
    }
}
