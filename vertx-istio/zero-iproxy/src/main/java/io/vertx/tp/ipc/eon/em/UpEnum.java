// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: zero.enum.proto

package io.vertx.tp.ipc.eon.em;

public final class UpEnum {
  private UpEnum() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017zero.enum.proto\022\026io.vertx.tp.ipc.eon.e" +
      "m*.\n\013Compression\022\010\n\004NONE\020\000\022\010\n\004GZIP\020\001\022\013\n\007" +
      "DEFLATE\020\002*\'\n\006Format\022\n\n\006BINARY\020\000\022\010\n\004JSON\020" +
      "\001\022\007\n\003XML\020\002*<\n\010Category\022\020\n\014COMPRESSABLE\020\000" +
      "\022\022\n\016UNCOMPRESSABLE\020\001\022\n\n\006RANDOM\020\002B\"\n\026io.v" +
      "ertx.tp.ipc.eon.emB\006UpEnumP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
