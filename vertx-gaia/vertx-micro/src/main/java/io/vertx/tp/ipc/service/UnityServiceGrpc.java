package io.vertx.tp.ipc.service;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.6.1)",
    comments = "Source: zero.def.service.proto")
public final class UnityServiceGrpc {

  private UnityServiceGrpc() {}

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

  public static final String SERVICE_NAME = "io.vertx.tp.ipc.service.UnityService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<io.vertx.tp.ipc.eon.IpcRequest,
      io.vertx.tp.ipc.eon.IpcResponse> METHOD_UNITY_CALL =
      io.grpc.MethodDescriptor.<io.vertx.tp.ipc.eon.IpcRequest, io.vertx.tp.ipc.eon.IpcResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "io.vertx.tp.ipc.service.UnityService", "UnityCall"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.IpcRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.IpcResponse.getDefaultInstance()))
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
   * Creates a new vertx stub that supports all call types for the service
   */
  public static UnityServiceVertxStub newVertxStub(io.grpc.Channel channel) {
    return new UnityServiceVertxStub(channel);
  }

  /**
   */
  public static abstract class UnityServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Direct: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public void unityCall(io.vertx.tp.ipc.eon.IpcRequest request,
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.IpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_UNITY_CALL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_UNITY_CALL,
            asyncUnaryCall(
              new MethodHandlers<
                io.vertx.tp.ipc.eon.IpcRequest,
                io.vertx.tp.ipc.eon.IpcResponse>(
                  this, METHODID_UNITY_CALL)))
          .build();
    }
  }

  /**
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
    public void unityCall(io.vertx.tp.ipc.eon.IpcRequest request,
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.IpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_UNITY_CALL, getCallOptions()), request, responseObserver);
    }
  }

  /**
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
    public io.vertx.tp.ipc.eon.IpcResponse unityCall(io.vertx.tp.ipc.eon.IpcRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_UNITY_CALL, getCallOptions(), request);
    }
  }

  /**
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
    public com.google.common.util.concurrent.ListenableFuture<io.vertx.tp.ipc.eon.IpcResponse> unityCall(
        io.vertx.tp.ipc.eon.IpcRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_UNITY_CALL, getCallOptions()), request);
    }
  }

  /**
   */
  public static abstract class UnityServiceVertxImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Direct: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public void unityCall(io.vertx.tp.ipc.eon.IpcRequest request,
        io.vertx.core.Future<io.vertx.tp.ipc.eon.IpcResponse> response) {
      asyncUnimplementedUnaryCall(METHOD_UNITY_CALL, UnityServiceGrpc.toObserver(response.completer()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_UNITY_CALL,
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.vertx.tp.ipc.eon.IpcRequest,
                io.vertx.tp.ipc.eon.IpcResponse>(
                  this, METHODID_UNITY_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class UnityServiceVertxStub extends io.grpc.stub.AbstractStub<UnityServiceVertxStub> {
    private UnityServiceVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UnityServiceVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UnityServiceVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UnityServiceVertxStub(channel, callOptions);
    }

    /**
     * <pre>
     * Direct: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public void unityCall(io.vertx.tp.ipc.eon.IpcRequest request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.tp.ipc.eon.IpcResponse>> response) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_UNITY_CALL, getCallOptions()), request, UnityServiceGrpc.toObserver(response));
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
          serviceImpl.unityCall((io.vertx.tp.ipc.eon.IpcRequest) request,
              (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.IpcResponse>) responseObserver);
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

  private static final class VertxMethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UnityServiceVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(UnityServiceVertxImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UNITY_CALL:
          serviceImpl.unityCall((io.vertx.tp.ipc.eon.IpcRequest) request,
              (io.vertx.core.Future<io.vertx.tp.ipc.eon.IpcResponse>) io.vertx.core.Future.<io.vertx.tp.ipc.eon.IpcResponse>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.IpcResponse>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
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
      return io.vertx.tp.ipc.service.UpIpcService.getDescriptor();
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
