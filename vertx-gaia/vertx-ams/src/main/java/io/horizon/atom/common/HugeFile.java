package io.horizon.atom.common;

import io.horizon.exception.internal.EmptyIoException;
import io.horizon.fn.HFn;
import io.horizon.util.HUt;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/*
 * Big File reader for > 10GB
 */
public class HugeFile {
    private final StringBuilder prefix = new StringBuilder();
    private final long processLength = 0;
    private MappedByteBuffer[] mappedByteBuffers;
    private FileInputStream inputStream;
    private FileChannel fileChannel;
    private int bufferCount;
    private int byteBufferSize;
    private int bufferCountIndex = 0;
    private byte[] byteBuffer;
    private long fileSize;

    public HugeFile(final String filename) {
        this(filename, 65536);
    }

    public HugeFile(final String fileName, final int byteBufferSize) {
        HFn.jvmAt(() -> {
            final File file = HUt.ioFile(fileName);
            if (Objects.isNull(file)) {
                throw new EmptyIoException(this.getClass(), fileName);
            } else {
                this.inputStream = new FileInputStream(file);
                this.fileChannel = this.inputStream.getChannel();
                this.fileSize = this.fileChannel.size();
                this.bufferCount = (int) Math.ceil((double) this.fileSize / (double) Integer.MAX_VALUE);
                this.mappedByteBuffers = new MappedByteBuffer[this.bufferCount];
                this.byteBufferSize = byteBufferSize;

                long preLength = 0;
                long regionSize = Integer.MAX_VALUE;
                for (int i = 0; i < this.bufferCount; i++) {
                    if (this.fileSize - preLength < Integer.MAX_VALUE) {
                        regionSize = this.fileSize - preLength;
                    }
                    this.mappedByteBuffers[i] = this.fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
                    preLength += regionSize;
                }
            }
        });
    }

    public synchronized int read() {
        if (this.bufferCountIndex >= this.bufferCount) {
            return -1;
        }

        final int limit = this.mappedByteBuffers[this.bufferCountIndex].limit();
        final int position = this.mappedByteBuffers[this.bufferCountIndex].position();

        int realSize = this.byteBufferSize;
        if (limit - position < this.byteBufferSize) {
            realSize = limit - position;
        }
        this.byteBuffer = new byte[realSize];
        this.mappedByteBuffers[this.bufferCountIndex].get(this.byteBuffer);

        if (realSize < this.byteBufferSize && this.bufferCountIndex < this.bufferCount) {
            this.bufferCountIndex++;
        }
        return realSize;
    }

    public void close() {
        HFn.jvmAt(() -> {
            this.fileChannel.close();
            this.inputStream.close();
            for (final MappedByteBuffer byteBuffer : this.mappedByteBuffers) {
                byteBuffer.clear();
            }
            this.byteBuffer = null;
        });
    }

    public synchronized byte[] getCurrentBytes() {
        return this.byteBuffer;
    }
}
