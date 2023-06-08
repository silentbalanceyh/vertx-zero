// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: zero.status.proto

package io.vertx.mod.ipc.eon;

/**
 * Protobuf type {@code io.vertx.mod.ipc.eon.IpcStatus}
 */
public final class IpcStatus extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:io.vertx.mod.ipc.eon.IpcStatus)
    IpcStatusOrBuilder {
    public static final int CODE_FIELD_NUMBER = 1;
    public static final int MESSAGE_FIELD_NUMBER = 2;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:io.vertx.mod.ipc.eon.IpcStatus)
    private static final io.vertx.mod.ipc.eon.IpcStatus DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<IpcStatus>
        PARSER = new com.google.protobuf.AbstractParser<IpcStatus>() {
        @Override
        public IpcStatus parsePartialFrom(
            final com.google.protobuf.CodedInputStream input,
            final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
            return new IpcStatus(input, extensionRegistry);
        }
    };

    static {
        DEFAULT_INSTANCE = new io.vertx.mod.ipc.eon.IpcStatus();
    }

    private int code_;
    private volatile java.lang.Object message_;
    private byte memoizedIsInitialized = -1;

    // Use IpcStatus.newBuilder() to construct.
    private IpcStatus(final com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }
    private IpcStatus() {
        this.code_ = 0;
        this.message_ = "";
    }

    private IpcStatus(
        final com.google.protobuf.CodedInputStream input,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        final int mutable_bitField0_ = 0;
        try {
            boolean done = false;
            while (!done) {
                final int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!input.skipField(tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 8: {

                        this.code_ = input.readInt32();
                        break;
                    }
                    case 18: {
                        final java.lang.String s = input.readStringRequireUtf8();

                        this.message_ = s;
                        break;
                    }
                }
            }
        } catch (final com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (final java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                e).setUnfinishedMessage(this);
        } finally {
            this.makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return io.vertx.mod.ipc.eon.UpStatus.internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor;
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final com.google.protobuf.ByteString data,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(final byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final byte[] data,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(final java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseWithIOException(PARSER, input);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final java.io.InputStream input,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseDelimitedFrom(final java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseDelimitedWithIOException(PARSER, input);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseDelimitedFrom(
        final java.io.InputStream input,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseWithIOException(PARSER, input);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus parseFrom(
        final com.google.protobuf.CodedInputStream input,
        final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(final io.vertx.mod.ipc.eon.IpcStatus prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static io.vertx.mod.ipc.eon.IpcStatus getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<IpcStatus> parser() {
        return PARSER;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
        return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return io.vertx.mod.ipc.eon.UpStatus.internal_static_io_vertx_tp_ipc_eon_IpcStatus_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                io.vertx.mod.ipc.eon.IpcStatus.class, io.vertx.mod.ipc.eon.IpcStatus.Builder.class);
    }

    /**
     * <pre>
     * Status Code
     * </pre>
     *
     * <code>int32 code = 1;</code>
     */
    @Override
    public int getCode() {
        return this.code_;
    }

    /**
     * <pre>
     * Status Content
     * </pre>
     *
     * <code>string message = 2;</code>
     */
    @Override
    public java.lang.String getMessage() {
        final java.lang.Object ref = this.message_;
        if (ref instanceof java.lang.String) {
            return (java.lang.String) ref;
        } else {
            final com.google.protobuf.ByteString bs =
                (com.google.protobuf.ByteString) ref;
            final java.lang.String s = bs.toStringUtf8();
            this.message_ = s;
            return s;
        }
    }

    /**
     * <pre>
     * Status Content
     * </pre>
     *
     * <code>string message = 2;</code>
     */
    @Override
    public com.google.protobuf.ByteString
    getMessageBytes() {
        final java.lang.Object ref = this.message_;
        if (ref instanceof java.lang.String) {
            final com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            this.message_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
    }

    @Override
    public final boolean isInitialized() {
        final byte isInitialized = this.memoizedIsInitialized;
        if (isInitialized == 1) {
            return true;
        }
        if (isInitialized == 0) {
            return false;
        }

        this.memoizedIsInitialized = 1;
        return true;
    }

    @Override
    public void writeTo(final com.google.protobuf.CodedOutputStream output)
        throws java.io.IOException {
        if (this.code_ != 0) {
            output.writeInt32(1, this.code_);
        }
        if (!this.getMessageBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 2, this.message_);
        }
    }

    @Override
    public int getSerializedSize() {
        int size = this.memoizedSize;
        if (size != -1) {
            return size;
        }

        size = 0;
        if (this.code_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(1, this.code_);
        }
        if (!this.getMessageBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, this.message_);
        }
        this.memoizedSize = size;
        return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof io.vertx.mod.ipc.eon.IpcStatus)) {
            return super.equals(obj);
        }
        final io.vertx.mod.ipc.eon.IpcStatus other = (io.vertx.mod.ipc.eon.IpcStatus) obj;

        boolean result = true;
        result = result && (this.getCode()
            == other.getCode());
        result = result && this.getMessage()
            .equals(other.getMessage());
        return result;
    }

    @java.lang.Override
    public int hashCode() {
        if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        hash = (37 * hash) + CODE_FIELD_NUMBER;
        hash = (53 * hash) + this.getCode();
        hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
        hash = (53 * hash) + this.getMessage().hashCode();
        hash = (29 * hash) + this.unknownFields.hashCode();
        this.memoizedHashCode = hash;
        return hash;
    }

    @Override
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @Override
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
            ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        final com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        final Builder builder = new Builder(parent);
        return builder;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<IpcStatus> getParserForType() {
        return PARSER;
    }

    @Override
    public io.vertx.mod.ipc.eon.IpcStatus getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code io.vertx.mod.ipc.eon.IpcStatus}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:io.vertx.mod.ipc.eon.IpcStatus)
        io.vertx.mod.ipc.eon.IpcStatusOrBuilder {
        private int code_;
        private java.lang.Object message_ = "";

        // Construct using io.vertx.mod.ipc.eon.IpcStatus.newBuilder()
        private Builder() {
            this.maybeForceBuilderInitialization();
        }

        private Builder(
            final com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return io.vertx.mod.ipc.eon.UpStatus.internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor;
        }

        @Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return io.vertx.mod.ipc.eon.UpStatus.internal_static_io_vertx_tp_ipc_eon_IpcStatus_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    io.vertx.mod.ipc.eon.IpcStatus.class, io.vertx.mod.ipc.eon.IpcStatus.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
            }
        }

        @Override
        public Builder clear() {
            super.clear();
            this.code_ = 0;

            this.message_ = "";

            return this;
        }

        @Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return io.vertx.mod.ipc.eon.UpStatus.internal_static_io_vertx_tp_ipc_eon_IpcStatus_descriptor;
        }

        @Override
        public io.vertx.mod.ipc.eon.IpcStatus getDefaultInstanceForType() {
            return io.vertx.mod.ipc.eon.IpcStatus.getDefaultInstance();
        }

        @Override
        public io.vertx.mod.ipc.eon.IpcStatus build() {
            final io.vertx.mod.ipc.eon.IpcStatus result = this.buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @Override
        public io.vertx.mod.ipc.eon.IpcStatus buildPartial() {
            final io.vertx.mod.ipc.eon.IpcStatus result = new io.vertx.mod.ipc.eon.IpcStatus(this);
            result.code_ = this.code_;
            result.message_ = this.message_;
            this.onBuilt();
            return result;
        }

        @Override
        public Builder clone() {
            return (Builder) super.clone();
        }

        @Override
        public Builder setField(
            final com.google.protobuf.Descriptors.FieldDescriptor field,
            final Object value) {
            return (Builder) super.setField(field, value);
        }

        @Override
        public Builder clearField(
            final com.google.protobuf.Descriptors.FieldDescriptor field) {
            return (Builder) super.clearField(field);
        }

        @Override
        public Builder clearOneof(
            final com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return (Builder) super.clearOneof(oneof);
        }

        @Override
        public Builder setRepeatedField(
            final com.google.protobuf.Descriptors.FieldDescriptor field,
            final int index, final Object value) {
            return (Builder) super.setRepeatedField(field, index, value);
        }

        @Override
        public Builder addRepeatedField(
            final com.google.protobuf.Descriptors.FieldDescriptor field,
            final Object value) {
            return (Builder) super.addRepeatedField(field, value);
        }

        @Override
        public Builder mergeFrom(final com.google.protobuf.Message other) {
            if (other instanceof io.vertx.mod.ipc.eon.IpcStatus) {
                return this.mergeFrom((io.vertx.mod.ipc.eon.IpcStatus) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(final io.vertx.mod.ipc.eon.IpcStatus other) {
            if (other == io.vertx.mod.ipc.eon.IpcStatus.getDefaultInstance()) {
                return this;
            }
            if (other.getCode() != 0) {
                this.setCode(other.getCode());
            }
            if (!other.getMessage().isEmpty()) {
                this.message_ = other.message_;
                this.onChanged();
            }
            this.onChanged();
            return this;
        }

        @Override
        public final boolean isInitialized() {
            return true;
        }

        @Override
        public Builder mergeFrom(
            final com.google.protobuf.CodedInputStream input,
            final com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
            io.vertx.mod.ipc.eon.IpcStatus parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (final com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (io.vertx.mod.ipc.eon.IpcStatus) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    this.mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        /**
         * <pre>
         * Status Code
         * </pre>
         *
         * <code>int32 code = 1;</code>
         */
        @Override
        public int getCode() {
            return this.code_;
        }

        /**
         * <pre>
         * Status Code
         * </pre>
         *
         * <code>int32 code = 1;</code>
         */
        public Builder setCode(final int value) {

            this.code_ = value;
            this.onChanged();
            return this;
        }

        /**
         * <pre>
         * Status Code
         * </pre>
         *
         * <code>int32 code = 1;</code>
         */
        public Builder clearCode() {

            this.code_ = 0;
            this.onChanged();
            return this;
        }

        /**
         * <pre>
         * Status Content
         * </pre>
         *
         * <code>string message = 2;</code>
         */
        @Override
        public java.lang.String getMessage() {
            final java.lang.Object ref = this.message_;
            if (!(ref instanceof java.lang.String)) {
                final com.google.protobuf.ByteString bs =
                    (com.google.protobuf.ByteString) ref;
                final java.lang.String s = bs.toStringUtf8();
                this.message_ = s;
                return s;
            } else {
                return (java.lang.String) ref;
            }
        }

        /**
         * <pre>
         * Status Content
         * </pre>
         *
         * <code>string message = 2;</code>
         */
        public Builder setMessage(
            final java.lang.String value) {
            if (value == null) {
                throw new NullPointerException();
            }

            this.message_ = value;
            this.onChanged();
            return this;
        }

        /**
         * <pre>
         * Status Content
         * </pre>
         *
         * <code>string message = 2;</code>
         */
        @Override
        public com.google.protobuf.ByteString
        getMessageBytes() {
            final java.lang.Object ref = this.message_;
            if (ref instanceof String) {
                final com.google.protobuf.ByteString b =
                    com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref);
                this.message_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <pre>
         * Status Content
         * </pre>
         *
         * <code>string message = 2;</code>
         */
        public Builder setMessageBytes(
            final com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);

            this.message_ = value;
            this.onChanged();
            return this;
        }

        /**
         * <pre>
         * Status Content
         * </pre>
         *
         * <code>string message = 2;</code>
         */
        public Builder clearMessage() {

            this.message_ = getDefaultInstance().getMessage();
            this.onChanged();
            return this;
        }

        @Override
        public final Builder setUnknownFields(
            final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }

        @Override
        public final Builder mergeUnknownFields(
            final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }


        // @@protoc_insertion_point(builder_scope:io.vertx.mod.ipc.eon.IpcStatus)
    }

}
