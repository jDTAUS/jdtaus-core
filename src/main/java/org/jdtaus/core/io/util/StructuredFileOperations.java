/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdtaus.core.io.util;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.event.EventListenerList;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyException;
import org.jdtaus.core.text.Message;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.StructuredFile;
import org.jdtaus.core.io.StructuredFileListener;
import org.jdtaus.core.lang.spi.MemoryManager;
import org.jdtaus.core.monitor.spi.Task;
import org.jdtaus.core.monitor.spi.TaskMonitor;

/**
 * {@code StructuredFile} implementation based on {@code FileOperations}.
 * <p>This implementation performs read-ahead caching for the
 * {@code readBlock()} methods. Changes done via the {@code writeBlock()}
 * methods will only be cached if the blocks beeing changed were read-ahead into
 * the cache via the {@code readBlock()} methods before. All other writes will
 * directly be performed with calls to {@code FileOperations} methods. The
 * {@code flush()} method must be used to write out cached changes to the
 * underlying {@code FileOperations} implementation.<p/>
 * <p><b>Note:</b><br>
 * This implementation is not thread-safe. Concurrent changes to the underlying
 * {@code FileOperations} implementation are not supported.</p>
 *
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class StructuredFileOperations implements StructuredFile
{
    //--Fields------------------------------------------------------------------

    /** Cache for {@code readAhead} blocks. */
    private byte[] cache;

    /** Index of the block starting at {@code cache[0]}. */
    private long cacheIndex;

    /** Number of blocks read-ahead into cache. */
    private int cacheMaxIndex;

    /** Information about changes in memory which need to be persisted. */
    private boolean[] dirtyCache;

    /** Pre-allocated temporary buffer. */
    private byte[] minimumBuffer;

    /** List for {@code StructuredFileListener}s. */
    private final EventListenerList fileListeners = new EventListenerList();

    //------------------------------------------------------------------Fields--
    //--Implementation----------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(StructuredFileOperations.class.getName());

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    protected void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("monitoringThreshold");
        this._monitoringThreshold = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("minBufferedBlocks");
        this._minBufferedBlocks = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("readAhead");
        this._readAhead = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("blockSize");
        this._blockSize = ((java.lang.Integer) p.getValue()).intValue();

    }

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>TaskMonitor</code> implementation. */
    private transient TaskMonitor _dependency1;

    /**
     * Gets the configured <code>TaskMonitor</code> implementation.
     *
     * @return the configured <code>TaskMonitor</code> implementation.
     */
    private TaskMonitor getTaskMonitor()
    {
        TaskMonitor ret = null;
        if(this._dependency1 != null)
        {
            ret = this._dependency1;
        }
        else
        {
            ret = (TaskMonitor) ContainerFactory.getContainer().
                getDependency(StructuredFileOperations.class,
                "TaskMonitor");

            if(ModelFactory.getModel().getModules().
                getImplementation(StructuredFileOperations.class.getName()).
                getDependencies().getDependency("TaskMonitor").
                isBound())
            {
                this._dependency1 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>MemoryManager</code> implementation. */
    private transient MemoryManager _dependency0;

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        MemoryManager ret = null;
        if(this._dependency0 != null)
        {
            ret = this._dependency0;
        }
        else
        {
            ret = (MemoryManager) ContainerFactory.getContainer().
                getDependency(StructuredFileOperations.class,
                "MemoryManager");

            if(ModelFactory.getModel().getModules().
                getImplementation(StructuredFileOperations.class.getName()).
                getDependencies().getDependency("MemoryManager").
                isBound())
            {
                this._dependency0 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code monitoringThreshold}.
     * @serial
     */
    private int _monitoringThreshold;

    /**
     * Gets the value of property <code>monitoringThreshold</code>.
     *
     * @return the value of property <code>monitoringThreshold</code>.
     */
    public int getMonitoringThreshold()
    {
        return this._monitoringThreshold;
    }

    /**
     * Property {@code minBufferedBlocks}.
     * @serial
     */
    private int _minBufferedBlocks;

    /**
     * Gets the value of property <code>minBufferedBlocks</code>.
     *
     * @return the value of property <code>minBufferedBlocks</code>.
     */
    protected int getMinBufferedBlocks()
    {
        return this._minBufferedBlocks;
    }

    /**
     * Property {@code readAhead}.
     * @serial
     */
    private int _readAhead;

    /**
     * Gets the value of property <code>readAhead</code>.
     *
     * @return the value of property <code>readAhead</code>.
     */
    protected int getReadAhead()
    {
        return this._readAhead;
    }

    /**
     * Property {@code blockSize}.
     * @serial
     */
    private int _blockSize;

    /**
     * Gets the value of property <code>blockSize</code>.
     *
     * @return the value of property <code>blockSize</code>.
     */
    public int getBlockSize()
    {
        return this._blockSize;
    }


    //--------------------------------------------------------------Properties--
    //--StructuredFile----------------------------------------------------------

    public long getBlockCount() throws IOException
    {
        return this.getFileOperations().getLength() / this.getBlockSize();
    }

    public void deleteBlocks(final long index,
        final long count) throws IOException
    {

        final long blockCount = this.getBlockCount();

        // Preconditions.
        if(index < 0L || index > blockCount - count)
        {
            throw new ArrayIndexOutOfBoundsException((int) index);
        }
        if(count <= 0 || count > blockCount - index)
        {
            throw new ArrayIndexOutOfBoundsException((int) count);
        }

        this.deleteBlocksImpl(index, count, blockCount);
    }

    private void deleteBlocksImpl(final long index, final long count,
        final long blockCount) throws IOException
    {
        final long block = index + count;
        final Task task = new Task();
        long toMoveByte = (blockCount - block) * this.getBlockSize();
        long readPos = block * this.getBlockSize();
        long writePos = index * this.getBlockSize();
        long progress = 0L;

        // Flush the cache.
        if(!(this.cacheIndex < 0) && this.cacheIndex >= index)
        {
            this.flush();
            this.cacheIndex = -1L;
        }

        // No blocks are following the ones to remove.
        if(toMoveByte == 0L)
        {
            this.getFileOperations().setLength(this.getFileOperations().
                getLength() - count * this.getBlockSize());

            this.fireBlocksDeleted(index, count);
            return;
        }

        final byte[] buf = this.newTemporaryBuffer(toMoveByte >
            Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) toMoveByte);


        task.setIndeterminate(false);
        task.setCancelable(false);
        task.setMinimum(0);
        task.setMaximum(toMoveByte > Integer.MAX_VALUE ?
            Integer.MAX_VALUE : (int) toMoveByte);

        task.setProgress((int) progress);
        task.setDescription(new DeleteBlocksMessage());

        final boolean monitoring = toMoveByte > this.getMonitoringThreshold();
        if(monitoring)
        {
            this.getTaskMonitor().monitor(task);
        }

        try
        {
            // Move following blocks to the position of the first block to
            // remove.
            while(toMoveByte > 0L)
            {
                this.getFileOperations().setFilePointer(readPos);
                final int len = toMoveByte <= buf.length ?
                    (int) toMoveByte : buf.length;

                int read = 0;
                int total = 0;
                do
                {
                    read = this.getFileOperations().
                        read(buf, total, len - total);

                    if(read == -1)
                    {
                        throw new EOFException();
                    }
                    else
                    {
                        total += read;
                    }

                } while(total < len);

                // Move the block count blocks to the beginning.
                this.getFileOperations().setFilePointer(writePos);
                this.getFileOperations().write(buf, 0, len);

                readPos += len;
                writePos += len;
                toMoveByte -= len;
                progress = progress + len > Integer.MAX_VALUE ?
                    Integer.MAX_VALUE : progress + len;

                task.setProgress((int) progress);
            }

            // Truncate the file.
            this.getFileOperations().setLength(this.getFileOperations().
                getLength() - count * this.getBlockSize());

            this.fireBlocksDeleted(index, count);
        }
        finally
        {
            if(monitoring)
            {
                this.getTaskMonitor().finish(task);
            }
        }
    }

    public void insertBlocks(final long index,
        final long count) throws IOException
    {

        final long blockCount = this.getBlockCount();

        // Preconditions.
        if(index < 0L || index > blockCount)
        {
            throw new ArrayIndexOutOfBoundsException((int) index);
        }
        if(count <= 0L || count > Long.MAX_VALUE - blockCount)
        {
            throw new ArrayIndexOutOfBoundsException((int) count);
        }

        this.insertBlocksImpl(index, count, blockCount);
    }

    private void insertBlocksImpl(final long index, final long count,
        final long blockCount) throws IOException
    {
        final Task task = new Task();
        long toMoveByte = (blockCount - index) * this.getBlockSize();
        long readPos = blockCount * this.getBlockSize();
        long writePos = readPos + count * this.getBlockSize();
        long progress = 0L;

        // Flush the cache.
        if(!(this.cacheIndex < 0) && this.cacheIndex >= index)
        {
            this.flush();
            this.cacheIndex = -1L;
        }

        // Increase the length of the file.
        this.getFileOperations().setLength(this.getFileOperations().
            getLength() + this.getBlockSize() * count);

        // New blocks are inserted at the end of the file.
        if(toMoveByte <= 0L)
        {
            this.fireBlocksInserted(index, count);
            return;
        }

        final byte[] buf  = this.newTemporaryBuffer(
            toMoveByte > Integer.MAX_VALUE ?
                Integer.MAX_VALUE : (int) toMoveByte);

        task.setIndeterminate(false);
        task.setCancelable(false);
        task.setMinimum(0);
        task.setMaximum(toMoveByte > Integer.MAX_VALUE ?
            Integer.MAX_VALUE : (int) toMoveByte);

        task.setProgress((int) progress);
        task.setDescription(new InsertBlocksMessage());

        final boolean monitoring = toMoveByte > this.getMonitoringThreshold();
        if(monitoring)
        {
            this.getTaskMonitor().monitor(task);
        }

        try
        {
            // Move all blocks from index inclusive count blocks to the end of
            // the file.
            while(toMoveByte > 0L)
            {
                final int moveLen = buf.length >= toMoveByte ?
                    (int) toMoveByte : buf.length;

                readPos -= moveLen;
                writePos -= moveLen;
                this.getFileOperations().setFilePointer(readPos);
                int read = 0;
                int total = 0;

                do
                {
                    read = this.getFileOperations().
                        read(buf, total, moveLen - total);

                    if(read == -1)
                    {
                        throw new EOFException();
                    }
                    else
                    {
                        total += read;
                    }

                } while(total < moveLen);

                // Move the block count blocks to the end.
                this.getFileOperations().setFilePointer(writePos);
                this.getFileOperations().write(buf, 0, moveLen);

                toMoveByte -= moveLen;
                progress = progress + moveLen > Integer.MAX_VALUE ?
                    Integer.MAX_VALUE : progress + moveLen;

                task.setProgress((int) progress);
            }

            this.fireBlocksInserted(index, count);
        }
        finally
        {
            if(monitoring)
            {
                this.getTaskMonitor().finish(task);
            }
        }
    }

    public void readBlock(final long block, final int off,
        final byte[] buf) throws IOException
    {

        this.readBlock(block, off, buf, 0, buf.length);
    }

    public void readBlock(final long block, final int off, final byte[] buf,
        final int index, final int length) throws IOException
    {

        this.assertValidArguments(block, off, buf, index, length);
        // Fill the cache.
        this.readAhead(block, this.getBlockCount());
        // Copy from cache.
        System.arraycopy(this.cache,
            ((int) ((block - this.cacheIndex) * this.getBlockSize())) + off,
            buf, index, length);

    }

    public void writeBlock(final long block, final int off,
        final byte[] buf) throws IOException
    {

        this.writeBlock(block, off, buf, 0, buf.length);
    }


    public void writeBlock(final long block, final int off, final byte[] buf,
        final int index, final int length) throws IOException
    {

        this.assertValidArguments(block, off, buf, index, length);

        if(!(this.cacheIndex < 0L) && block >= this.cacheIndex &&
            block <= this.cacheIndex + this.cacheMaxIndex)
        {

            System.arraycopy(buf, index, this.cache,
                (int) (block - this.cacheIndex) * this.getBlockSize() + off,
                length);

            this.dirtyCache[(int) (block - this.cacheIndex)] = true;
        }
        else
        {
            this.getFileOperations().
                setFilePointer(block * this.getBlockSize() + off);

            this.getFileOperations().write(buf, index, length);
        }
    }

    public void addStructuredFileListener(
        final StructuredFileListener listener)
    {

        this.fileListeners.add(StructuredFileListener.class, listener);
    }

    public void removeStructuredFileListener(
        final StructuredFileListener listener)
    {

        this.fileListeners.remove(StructuredFileListener.class, listener);
    }

    public StructuredFileListener[] getStructuredFileListeners()
    {
        return (StructuredFileListener[]) this.fileListeners.getListeners(
            StructuredFileListener.class);

    }

    //----------------------------------------------------------StructuredFile--
    //--StructuredFileOperations------------------------------------------------

    /** Message: {@code Removing blocks.} */
    private static final class DeleteBlocksMessage extends Message
    {
        private static final Object[] NO_ARGS = {};
        public Object[] getFormatArguments(final Locale locale)
        {
            return NO_ARGS;
        }
        public String getText(final Locale locale)
        {
            return StructuredFileOperationsBundle.
                getDeleteBlocksTaskText(locale);

        }
    }

    /** Message: {@code Inserting blocks.} */
    private static final class InsertBlocksMessage extends Message
    {
        private static final Object[] NO_ARGS = {};
        public Object[] getFormatArguments(final Locale locale)
        {
            return NO_ARGS;
        }
        public String getText(final Locale locale)
        {
            return StructuredFileOperationsBundle.
                getInsertBlocksTaskText(locale);

        }
    }

    /** {@code FileOperations} backing the instance. */
    private FileOperations fileOperations;

    /**
     * Creates a new {@code StructuredFileOperations} instance.
     *
     * @param blockSize Number of bytes per block.
     * @param fileOperations {@code FileOperations} implementation to operate
     * on.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws IllegalArgumentException if {@code blockSize} cannot be used with
     * {@code fileOperations}.
     * @throws IOException if getting the length from the {@code fileOperations}
     * fails.
     */
    public StructuredFileOperations(final int blockSize,
        final FileOperations fileOperations) throws IOException
    {
        super();

        this.initializeProperties(META.getProperties());
        this.assertValidProperties();

        if(fileOperations == null)
        {
            throw new NullPointerException("fileOperations");
        }

        this._blockSize = blockSize;
        this.fileOperations = fileOperations;

        final int minBufferedBlocks = this.getMinBufferedBlocks();
        final int readAhead = this.getReadAhead();

        this.minimumBuffer = this.getMemoryManager().
            allocateBytes(minBufferedBlocks * blockSize);

        this.cache = this.getMemoryManager().
            allocateBytes(blockSize * readAhead);

        this.dirtyCache = this.getMemoryManager().
            allocateBoolean(readAhead);

        this.cacheIndex = -1L;
        this.cacheMaxIndex = -1;

        this.assertValidFileLength();
    }

    /**
     * Gets the {@code FileOperations} implementation operations are performed
     * with.
     *
     * @return the {@code FileOperations} implementation operations are
     * performed with.
     */
    public FileOperations getFileOperations()
    {
        return this.fileOperations;
    }

    /**
     * Flushes internal caches.
     * <p>Must be called at least once after finishing work with an instance to
     * write out cached changes.</p>
     *
     * @throws IOError for unrecoverable I/O errors.
     */
    public void flush() throws IOException
    {
        if(this.cacheIndex >= 0L)
        {
            int writeOffset = 0;
            int writeLength = 0;
            final long blockCount = this.getBlockCount();
            final int readAhead = this.getReadAhead();
            final int toWrite = blockCount - this.cacheIndex < readAhead ?
                (int) (blockCount - this.cacheIndex) : readAhead;

            for(int i = 0; i < toWrite; i++)
            {
                if(!this.dirtyCache[i])
                {
                    if (writeLength > 0)
                    {
                        this.getFileOperations().setFilePointer(
                            (this.cacheIndex + writeOffset) * getBlockSize());

                        this.getFileOperations().write(this.cache, writeOffset *
                            this.getBlockSize(), writeLength);

                    }
                    writeOffset = i + 1;
                    writeLength = 0;
                }
                else
                {
                    this.dirtyCache[i] = false;
                    writeLength += this.getBlockSize();
                }
            }

            if(writeLength > 0)
            {
                this.getFileOperations().setFilePointer(
                    (this.cacheIndex + writeOffset) * this.getBlockSize());

                this.getFileOperations().write(this.cache,
                    writeOffset * this.getBlockSize(), writeLength);

            }
        }
    }

    /**
     * Checks arguments provided to the {@code readBlock} and {@code writeBlock}
     * methods.
     *
     * @throws NullPointerException if {@code buf} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code block} is negative,
     * greater than or equal to {@code getBlockCount()}, or {@code off} is
     * negative, greater than or equal to {@code getBlockSize()}, or
     * {@code index} is negative, greater than or equal to the length of
     * {@code buf}, or {@code length} is negative or greater than the
     * length of {@code buf} minus {@code index} or greater than
     * {@code getBlockSize() minus {@code off}.
     */
    private void assertValidArguments(final long block, final int off,
        final byte[] buf, final int index, final int length) throws
        NullPointerException, IndexOutOfBoundsException, IOException
    {

        final long blockCount = this.getBlockCount();

        if(buf == null)
        {
            throw new NullPointerException("buf");
        }
        if(block < 0 || block >= blockCount)
        {
            throw new ArrayIndexOutOfBoundsException((int) block);
        }
        if(off < 0 || off >= this.getBlockSize())
        {
            throw new ArrayIndexOutOfBoundsException((int) off);
        }
        if(index < 0 || index >= buf.length)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if(length < 0L || length > buf.length - index ||
            length > this.getBlockSize() - off)
        {

            throw new ArrayIndexOutOfBoundsException(length);
        }
    }

    /**
     * Checks the length of the provided {@code FileOperations} implementation
     * against property {@code blockSize}.
     *
     * @throws IllegalArgumentException if the combination of property
     * {@code blockSize} and {@code getFileOperations().getLength()} is invalid.
     * @throws IOException if the getting the length fails.
     */
    private void assertValidFileLength() throws IOException
    {
        if(this.getFileOperations() != null)
        {
            if(this.getFileOperations().getLength() %
                this.getBlockSize() != 0L)
            {

                throw new IllegalArgumentException(Long.toString(
                    this.getFileOperations().getLength() %
                    this.getBlockSize()));

            }
        }
    }

    /**
     * Checks configured properties.
     *
     * @throws PropertyException for illegal property values.
     */
    private void assertValidProperties()
    {
        final int minBufferedBlocks = this.getMinBufferedBlocks();
        final int readAhead = this.getReadAhead();
        final int blockSize = this.getBlockSize();
        final int monitoringThreshold = this.getMonitoringThreshold();

        // minBufferedBlocks must be a positive integer.
        if(!(minBufferedBlocks > 0))
        {
            throw new PropertyException("minBufferedBlocks",
                Integer.toString(minBufferedBlocks));

        }

        // readAhead must be a positive integer.
        if(!(readAhead > 0))
        {
            throw new PropertyException("readAhead",
                Integer.toString(readAhead));

        }

        // blockSize must be a positive integer.
        if(!(blockSize > 0))
        {
            throw new PropertyException("blockSize",
                Integer.toString(blockSize));

        }

        // monitoringThreshold must be a positive integer.
        if(!(monitoringThreshold > 0))
        {
            throw new PropertyException("monitoringThreshold",
                Integer.toString(monitoringThreshold));

        }
    }

    /**
     * Notifies all registered {@code StructuredFileListener}s about inserted
     * blocks.
     *
     * @param index the index new blocks were inserted.
     * @param insertedBlocks the number of blocks which were inserted at
     * {@code index}.
     *
     * @throws IOException if reading or writing fails.
     */
    private void fireBlocksInserted(final long index,
        final long insertedBlocks) throws IOException
    {
        final Object[] listeners = this.fileListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == StructuredFileListener.class)
            {
                ((StructuredFileListener) listeners[i + 1]).
                    blocksInserted(index, insertedBlocks);

            }
        }
    }

    /**
     * Notifies all registered {@code StructuredFileListener}s about deleted
     * blocks.
     *
     * @param index the index blocks were deleted at.
     * @param deletedBlocks the number of blocks which were deleted starting at
     * {@code index}.
     *
     * @throws IOException if reading or writing fails.
     */
    private void fireBlocksDeleted(final long index,
        final long deletedBlocks) throws IOException
    {
        final Object[] listeners = this.fileListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == StructuredFileListener.class)
            {
                ((StructuredFileListener) listeners[i + 1]).
                    blocksDeleted(index, deletedBlocks);

            }
        }
    }

    private void readAhead(final long index,
        final long blockCount) throws IOException
    {
        final int readAhead = this.getReadAhead();
        if(this.cacheIndex < 0L || index < this.cacheIndex ||
            index > this.cacheIndex + this.cacheMaxIndex)
        {

            final int toRead = blockCount - index < readAhead ?
                (int) (blockCount - index) : readAhead;

            final int cacheByte = this.getBlockSize() * toRead;
            int read = 0;
            int total = 0;

            this.flush();
            this.getFileOperations().
                setFilePointer(index * this.getBlockSize());

            this.cacheMaxIndex = toRead - 1;
            do
            {
                read = this.getFileOperations().read(this.cache, total,
                    cacheByte - total);

                if(read == -1)
                {
                    throw new EOFException();
                }
                else
                {
                    total += read;
                }

            } while(total < cacheByte);
            this.cacheIndex = index;
            Arrays.fill(this.dirtyCache, false);
        }
    }

    private byte[] newTemporaryBuffer(final int requested) throws IOException
    {
        final byte[] tmp;
        final long length = this.getFileOperations().getLength();

        if(requested <= 0 || requested > length)
        {
            throw new IllegalArgumentException(Integer.toString(requested));
        }

        return requested <= this.minimumBuffer.length ||
            this.getMemoryManager().getAvailableBytes() < requested
            ? this.minimumBuffer
            : this.getMemoryManager().allocateBytes(requested);

    }

    //------------------------------------------------StructuredFileOperations--

}
