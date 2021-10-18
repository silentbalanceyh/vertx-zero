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
public final class ConsumeServiceGrpc {

  private ConsumeServiceGrpc() {}

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

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
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

  private static final int METHODID_OUTPUT_CALL = 0;

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

  private static final class ConsumeServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.vertx.tp.ipc.service.UpIpcService.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

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
}
