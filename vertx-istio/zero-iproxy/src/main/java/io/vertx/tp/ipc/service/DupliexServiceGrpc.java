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
public final class DupliexServiceGrpc {

  private DupliexServiceGrpc() {}

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

  public static final String SERVICE_NAME = "io.vertx.tp.ipc.service.DupliexService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<io.vertx.tp.ipc.eon.StreamClientRequest,
      io.vertx.tp.ipc.eon.StreamServerResponse> METHOD_DUPLIEX_CALL =
      io.grpc.MethodDescriptor.<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamServerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "io.vertx.tp.ipc.service.DupliexService", "DupliexCall"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.StreamClientRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.StreamServerResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DupliexServiceStub newStub(io.grpc.Channel channel) {
    return new DupliexServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DupliexServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DupliexServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DupliexServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DupliexServiceFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static DupliexServiceVertxStub newVertxStub(io.grpc.Channel channel) {
    return new DupliexServiceVertxStub(channel);
  }

  /**
   */
  public static abstract class DupliexServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Full: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientRequest> dupliexCall(
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_DUPLIEX_CALL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_DUPLIEX_CALL,
            asyncBidiStreamingCall(
              new MethodHandlers<
                io.vertx.tp.ipc.eon.StreamClientRequest,
                io.vertx.tp.ipc.eon.StreamServerResponse>(
                  this, METHODID_DUPLIEX_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class DupliexServiceStub extends io.grpc.stub.AbstractStub<DupliexServiceStub> {
    private DupliexServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DupliexServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DupliexServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DupliexServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Full: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientRequest> dupliexCall(
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_DUPLIEX_CALL, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class DupliexServiceBlockingStub extends io.grpc.stub.AbstractStub<DupliexServiceBlockingStub> {
    private DupliexServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DupliexServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DupliexServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DupliexServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class DupliexServiceFutureStub extends io.grpc.stub.AbstractStub<DupliexServiceFutureStub> {
    private DupliexServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DupliexServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DupliexServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DupliexServiceFutureStub(channel, callOptions);
    }
  }

  /**
   */
  public static abstract class DupliexServiceVertxImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Full: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public void dupliexCall(
        io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamServerResponse> exchange) {
      exchange.setReadObserver(asyncUnimplementedStreamingCall(METHOD_DUPLIEX_CALL, exchange.writeObserver()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_DUPLIEX_CALL,
            asyncBidiStreamingCall(
              new VertxMethodHandlers<
                io.vertx.tp.ipc.eon.StreamClientRequest,
                io.vertx.tp.ipc.eon.StreamServerResponse>(
                  this, METHODID_DUPLIEX_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class DupliexServiceVertxStub extends io.grpc.stub.AbstractStub<DupliexServiceVertxStub> {
    private DupliexServiceVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DupliexServiceVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DupliexServiceVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DupliexServiceVertxStub(channel, callOptions);
    }

    /**
     * <pre>
     * Full: Client -&gt; Server -&gt; Client
     * </pre>
     */
    public void dupliexCall(io.vertx.core.Handler<
        io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamServerResponse, io.vertx.tp.ipc.eon.StreamClientRequest>> handler) {
      final io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamServerResponse> readStream =
          io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamServerResponse>create();

      handler.handle(io.vertx.grpc.GrpcBidiExchange.create(readStream, asyncBidiStreamingCall(
          getChannel().newCall(METHOD_DUPLIEX_CALL, getCallOptions()), readStream.readObserver())));
    }
  }

  private static final int METHODID_DUPLIEX_CALL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DupliexServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DupliexServiceImplBase serviceImpl, int methodId) {
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
        case METHODID_DUPLIEX_CALL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.dupliexCall(
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
    private final DupliexServiceVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(DupliexServiceVertxImplBase serviceImpl, int methodId) {
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
        case METHODID_DUPLIEX_CALL:
          io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamClientRequest> request0 = io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamClientRequest>create();
          serviceImpl.dupliexCall(
             io.vertx.grpc.GrpcBidiExchange.<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamServerResponse>create(
               request0,
               (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamServerResponse>) responseObserver));
          return (io.grpc.stub.StreamObserver<Req>) request0.readObserver();
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class DupliexServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.vertx.tp.ipc.service.UpIpcService.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DupliexServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DupliexServiceDescriptorSupplier())
              .addMethod(METHOD_DUPLIEX_CALL)
              .build();
        }
      }
    }
    return result;
  }
}
