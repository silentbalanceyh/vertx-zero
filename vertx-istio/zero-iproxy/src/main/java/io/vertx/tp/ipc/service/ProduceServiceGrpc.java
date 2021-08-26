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
public final class ProduceServiceGrpc {

  private ProduceServiceGrpc() {}

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

  public static final String SERVICE_NAME = "io.vertx.tp.ipc.service.ProduceService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<io.vertx.tp.ipc.eon.StreamClientRequest,
      io.vertx.tp.ipc.eon.StreamClientResponse> METHOD_INPUT_CALL =
      io.grpc.MethodDescriptor.<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamClientResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "io.vertx.tp.ipc.service.ProduceService", "InputCall"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.StreamClientRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              io.vertx.tp.ipc.eon.StreamClientResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ProduceServiceStub newStub(io.grpc.Channel channel) {
    return new ProduceServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ProduceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ProduceServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ProduceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ProduceServiceFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static ProduceServiceVertxStub newVertxStub(io.grpc.Channel channel) {
    return new ProduceServiceVertxStub(channel);
  }

  /**
   */
  public static abstract class ProduceServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Client -&gt; Server
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientRequest> inputCall(
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_INPUT_CALL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_INPUT_CALL,
            asyncBidiStreamingCall(
              new MethodHandlers<
                io.vertx.tp.ipc.eon.StreamClientRequest,
                io.vertx.tp.ipc.eon.StreamClientResponse>(
                  this, METHODID_INPUT_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class ProduceServiceStub extends io.grpc.stub.AbstractStub<ProduceServiceStub> {
    private ProduceServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProduceServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProduceServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProduceServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client -&gt; Server
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientRequest> inputCall(
        io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_INPUT_CALL, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class ProduceServiceBlockingStub extends io.grpc.stub.AbstractStub<ProduceServiceBlockingStub> {
    private ProduceServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProduceServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProduceServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProduceServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class ProduceServiceFutureStub extends io.grpc.stub.AbstractStub<ProduceServiceFutureStub> {
    private ProduceServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProduceServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProduceServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProduceServiceFutureStub(channel, callOptions);
    }
  }

  /**
   */
  public static abstract class ProduceServiceVertxImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Client -&gt; Server
     * </pre>
     */
    public void inputCall(
        io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamClientResponse> exchange) {
      exchange.setReadObserver(asyncUnimplementedStreamingCall(METHOD_INPUT_CALL, exchange.writeObserver()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_INPUT_CALL,
            asyncBidiStreamingCall(
              new VertxMethodHandlers<
                io.vertx.tp.ipc.eon.StreamClientRequest,
                io.vertx.tp.ipc.eon.StreamClientResponse>(
                  this, METHODID_INPUT_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class ProduceServiceVertxStub extends io.grpc.stub.AbstractStub<ProduceServiceVertxStub> {
    private ProduceServiceVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProduceServiceVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProduceServiceVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProduceServiceVertxStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client -&gt; Server
     * </pre>
     */
    public void inputCall(io.vertx.core.Handler<
        io.vertx.grpc.GrpcBidiExchange<io.vertx.tp.ipc.eon.StreamClientResponse, io.vertx.tp.ipc.eon.StreamClientRequest>> handler) {
      final io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamClientResponse> readStream =
          io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamClientResponse>create();

      handler.handle(io.vertx.grpc.GrpcBidiExchange.create(readStream, asyncBidiStreamingCall(
          getChannel().newCall(METHOD_INPUT_CALL, getCallOptions()), readStream.readObserver())));
    }
  }

  private static final int METHODID_INPUT_CALL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ProduceServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ProduceServiceImplBase serviceImpl, int methodId) {
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
        case METHODID_INPUT_CALL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.inputCall(
              (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientResponse>) responseObserver);
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
    private final ProduceServiceVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(ProduceServiceVertxImplBase serviceImpl, int methodId) {
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
        case METHODID_INPUT_CALL:
          io.vertx.grpc.GrpcReadStream<io.vertx.tp.ipc.eon.StreamClientRequest> request0 = io.vertx.grpc.GrpcReadStream.<io.vertx.tp.ipc.eon.StreamClientRequest>create();
          serviceImpl.inputCall(
             io.vertx.grpc.GrpcBidiExchange.<io.vertx.tp.ipc.eon.StreamClientRequest, io.vertx.tp.ipc.eon.StreamClientResponse>create(
               request0,
               (io.grpc.stub.StreamObserver<io.vertx.tp.ipc.eon.StreamClientResponse>) responseObserver));
          return (io.grpc.stub.StreamObserver<Req>) request0.readObserver();
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class ProduceServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.vertx.tp.ipc.service.UpIpcService.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ProduceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ProduceServiceDescriptorSupplier())
              .addMethod(METHOD_INPUT_CALL)
              .build();
        }
      }
    }
    return result;
  }
}
