// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: zero.status.proto

package io.vertx.mod.ipc.eon;

public final class UpStatus {
    private UpStatus() {
    }

    public static void registerAllExtensions(
        com.google.protobuf.ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(
        com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
            (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internal_static_io_vertx_tp_ipc_eon_IpcStatus_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_io_vertx_tp_ipc_eon_RetryParams_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internal_static_io_vertx_tp_ipc_eon_RetryParams_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_io_vertx_tp_ipc_eon_RetryInfo_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internal_static_io_vertx_tp_ipc_eon_RetryInfo_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_io_vertx_tp_ipc_eon_ResponseParams_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internal_static_io_vertx_tp_ipc_eon_ResponseParams_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
        descriptor;

    static {
        java.lang.String[] descriptorData = {
            "\n\021zero.status.proto\022\023io.vertx.mod.ipc.eon" +
                "\"*\n\tIpcStatus\022\014\n\004code\030\001 \001(\005\022\017\n\007message\030\002" +
                " \001(\t\"$\n\013RetryParams\022\025\n\rmax_reconnect\030\001 \001" +
                "(\005\"/\n\tRetryInfo\022\016\n\006passed\030\001 \001(\010\022\022\n\nbacko" +
                "ff_ms\030\002 \003(\005\"3\n\016ResponseParams\022\014\n\004size\030\001 " +
                "\001(\005\022\023\n\013interval_us\030\002 \001(\005B!\n\023io.vertx.mod." +
                "ipc.eonB\010UpStatusP\001b\006proto3"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
            new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                public com.google.protobuf.ExtensionRegistry assignDescriptors(
                    com.google.protobuf.Descriptors.FileDescriptor root) {
                    descriptor = root;
                    return null;
                }
            };
        com.google.protobuf.Descriptors.FileDescriptor
            .internalBuildGeneratedFileFrom(descriptorData,
                new com.google.protobuf.Descriptors.FileDescriptor[]{
                }, assigner);
        internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor =
            getDescriptor().getMessageTypes().get(0);
        internal_static_io_vertx_tp_ipc_eon_IpcStatus_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor,
            new java.lang.String[]{"Code", "Message",});
        internal_static_io_vertx_tp_ipc_eon_RetryParams_descriptor =
            getDescriptor().getMessageTypes().get(1);
        internal_static_io_vertx_tp_ipc_eon_RetryParams_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_io_vertx_tp_ipc_eon_RetryParams_descriptor,
            new java.lang.String[]{"MaxReconnect",});
        internal_static_io_vertx_tp_ipc_eon_RetryInfo_descriptor =
            getDescriptor().getMessageTypes().get(2);
        internal_static_io_vertx_tp_ipc_eon_RetryInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_io_vertx_tp_ipc_eon_RetryInfo_descriptor,
            new java.lang.String[]{"Passed", "BackoffMs",});
        internal_static_io_vertx_tp_ipc_eon_ResponseParams_descriptor =
            getDescriptor().getMessageTypes().get(3);
        internal_static_io_vertx_tp_ipc_eon_ResponseParams_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_io_vertx_tp_ipc_eon_ResponseParams_descriptor,
            new java.lang.String[]{"Size", "IntervalUs",});
    }

    // @@protoc_insertion_point(outer_class_scope)
}
