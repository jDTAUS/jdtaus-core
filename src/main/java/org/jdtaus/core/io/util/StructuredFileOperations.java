/*
 *  jDTAUS Core Utilities
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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

import java.io.IOException;
import java.math.BigDecimal;
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
import org.jdtaus.core.messages.DeletesBlocksMessage;
import org.jdtaus.core.messages.InsertsBlocksMessage;
import org.jdtaus.core.io.FileOperations;
import org.jdtaus.core.io.StructuredFile;
import org.jdtaus.core.io.StructuredFileListener;
import org.jdtaus.core.lang.spi.MemoryManager;
import org.jdtaus.core.monitor.spi.Task;
import org.jdtaus.core.monitor.spi.TaskMonitor;

/**
 * {@code StructuredFile} implementation based on {@code FileOperations}.
 * <p>Pre {@code FlushableFileOperations} and its implementations this
 * implementation performed read-ahead caching. This behaviour got changed
 * in favour of {@code ReadAheadFileOperations} and
 * {@code CoalescingFileOperations} which are generalized replacements for any
 * cacheing formerly performed by this implementation. Since this class does
 * not implement any cacheing anymore, the {@link #flush()} method will write
 * out pending changes of an underlying {@code FlushableFileOperations}
 * implementation, if any, by calling the corresponding {@code flush()} method
 * of that {@code FlushableFileOperations} instance.</p>
 * <p>This implementation uses task monitoring for the {@code deleteBlocks()}
 * and {@code insertBlocks()} methods. Task monitoring is controlled by
 * property {@code monitoringThreshold} holding the number of bytes which
 * need to minimally be copied to enable any task monitoring during the
 * copy operation (defaults to 5242880 - 5MB).</p>
 *
 * <p><b>Note:</b><br>
 * This implementation is not thread-safe and concurrent changes to the
 * underlying {@code FileOperations} implementation are not supported.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see CoalescingFileOperations
 * @see ReadAheadFileOperations
 */
public final class StructuredFileOperations implements StructuredFile
{
    //--Fields------------------------------------------------------------------

    /** Pre-allocated temporary buffer. */
    private byte[] minimumBuffer;

    /** Caches the value of property blockCount. */
    private long cachedBlockCount = NO_CACHED_BLOCKCOUNT;

    private static final long NO_CACHED_BLOCKCOUNT = Long.MIN_VALUE;

    /** List for {@code StructuredFileListener}s. */
    private final EventListenerList fileListeners = new EventListenerList();

    /** Value of property {@code blockSize} as a {@code BigDecimal}. */
    private final BigDecimal decimalBlockSize;

    //------------------------------------------------------------------Fields--
    //--Implementation----------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausImplementation
    // This section is managed by jdtaus-container-mojo.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(StructuredFileOperations.class.getName());
// </editor-fold>//GEN-END:jdtausImplementation

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /**
     * Initializes the properties of the instance.
     *
     * @param meta the property values to initialize the instance with.
     *
     * @throws NullPointerException if {@code meta} is {@code null}.
     */
    private void initializeProperties(final Properties meta)
    {
        Property p;

        if(meta == null)
        {
            throw new NullPointerException("meta");
        }

        p = meta.getProperty("monitoringThreshold");
        this.pMonitoringThreshold = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("minBufferedBlocks");
        this.pMinBufferedBlocks = ((java.lang.Integer) p.getValue()).intValue();


        p = meta.getProperty("blockSize");
        this.pBlockSize = ((java.lang.Integer) p.getValue()).intValue();

    }
// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /** Configured <code>TaskMonitor</code> implementation. */
    private transient TaskMonitor dTaskMonitor;

    /**
     * Gets the configured <code>TaskMonitor</code> implementation.
     *
     * @return the configured <code>TaskMonitor</code> implementation.
     */
    private TaskMonitor getTaskMonitor()
    {
        TaskMonitor ret = null;
        if(this.dTaskMonitor != null)
        {
            ret = this.dTaskMonitor;
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
                this.dTaskMonitor = ret;
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
    private transient MemoryManager dMemoryManager;

    /**
     * Gets the configured <code>MemoryManager</code> implementation.
     *
     * @return the configured <code>MemoryManager</code> implementation.
     */
    private MemoryManager getMemoryManager()
    {
        MemoryManager ret = null;
        if(this.dMemoryManager != null)
        {
            ret = this.dMemoryManager;
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
                this.dMemoryManager = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Property {@code monitoringThreshold}.
     * @serial
     */
    private int pMonitoringThreshold;

    /**
     * Gets the value of property <code>monitoringThreshold</code>.
     *
     * @return the value of property <code>monitoringThreshold</code>.
     */
    public int getMonitoringThreshold()
    {
        return this.pMonitoringThreshold;
    }

    /**
     * Property {@code minBufferedBlocks}.
     * @serial
     */
    private int pMinBufferedBlocks;

    /**
     * Gets the value of property <code>minBufferedBlocks</code>.
     *
     * @return the value of property <code>minBufferedBlocks</code>.
     */
    private int getMinBufferedBlocks()
    {
        return this.pMinBufferedBlocks;
    }

    /**
     * Property {@code blockSize}.
     * @serial
     */
    private int pBlockSize;

    /**
     * Gets the value of property <code>blockSize</code>.
     *
     * @return the value of property <code>blockSize</code>.
     */
    public int getBlockSize()
    {
        return this.pBlockSize;
    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--StructuredFile----------------------------------------------------------

    public long getBlockCount() throws IOException
    {
        this.assertNotClosed();

        if ( this.cachedBlockCount == NO_CACHED_BLOCKCOUNT )
        {
            this.cachedBlockCount = new BigDecimal(
                this.getFileOperations().getLength() ).divide(
                this.decimalBlockSize, BigDecimal.ROUND_UNNECESSARY ).
                longValue();

        // TODO JDK 1.5 longValueExact()
        }

        return this.cachedBlockCount;
    }

    public void deleteBlocks( final long index,
                               final long count ) throws IOException
    {
        final long blockCount = this.getBlockCount();

        // Preconditions.
        if ( index < 0L || index > blockCount - count )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) index );
        }
        if ( count <= 0 || count > blockCount - index )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) count );
        }

        this.assertNotClosed();

        this.deleteBlocksImpl( index, count, blockCount );
    }

    private void deleteBlocksImpl( final long index, final long count,
                                    final long blockCount ) throws IOException
    {
        final long block = index + count;
        final Task task = new Task();
        long toMoveByte = ( blockCount - block ) * this.getBlockSize();
        long readPos = block * this.getBlockSize();
        long writePos = index * this.getBlockSize();
        long progress = 0L;
        long progressDivisor = 1L;
        long maxProgress = toMoveByte;

        // Clear the cached block count.
        this.cachedBlockCount = NO_CACHED_BLOCKCOUNT;

        // No blocks are following the ones to remove.
        if ( toMoveByte == 0L )
        {
            this.getFileOperations().setLength( this.getFileOperations().
                                                getLength() - count *
                                                this.getBlockSize() );

            this.fireBlocksDeleted( index, count );
            return;
        }

        final byte[] buf = this.newTemporaryBuffer(
            toMoveByte > Integer.MAX_VALUE
            ? Integer.MAX_VALUE
            : ( int ) toMoveByte );

        while ( maxProgress > Integer.MAX_VALUE )
        {
            maxProgress /= 2L;
            progressDivisor *= 2L;
        }

        task.setIndeterminate( false );
        task.setCancelable( false );
        task.setMinimum( 0 );
        task.setMaximum( ( int ) maxProgress );
        task.setProgress( ( int ) progress );
        task.setDescription( new DeletesBlocksMessage() );

        final boolean monitoring = toMoveByte > this.getMonitoringThreshold();
        if ( monitoring )
        {
            this.getTaskMonitor().monitor( task );
        }

        try
        {
            // Move following blocks to the position of the first block to
            // remove.
            while ( toMoveByte > 0L )
            {
                this.getFileOperations().setFilePointer( readPos );
                final int len = toMoveByte <= buf.length
                    ? ( int ) toMoveByte
                    : buf.length;

                int read = 0;
                int total = 0;
                do
                {
                    read = this.getFileOperations().
                        read( buf, total, len - total );

                    assert read != FileOperations.EOF :
                        "Unexpected end of file.";

                    total += read;

                }
                while ( total < len );

                // Move the block count blocks to the beginning.
                this.getFileOperations().setFilePointer( writePos );
                this.getFileOperations().write( buf, 0, len );

                readPos += len;
                writePos += len;
                toMoveByte -= len;
                progress += len;

                task.setProgress( ( int ) ( progress / progressDivisor ) );
            }

            // Truncate the file.
            this.getFileOperations().setLength( this.getFileOperations().
                                                getLength() - count *
                                                this.getBlockSize() );

            this.fireBlocksDeleted( index, count );
        }
        finally
        {
            if ( monitoring )
            {
                this.getTaskMonitor().finish( task );
            }
        }
    }

    public void insertBlocks( final long index,
                               final long count ) throws IOException
    {
        final long blockCount = this.getBlockCount();

        // Preconditions.
        if ( index < 0L || index > blockCount )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) index );
        }
        if ( count <= 0L || count > Long.MAX_VALUE - blockCount )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) count );
        }

        this.assertNotClosed();

        this.insertBlocksImpl( index, count, blockCount );
    }

    private void insertBlocksImpl( final long index, final long count,
                                    final long blockCount ) throws IOException
    {
        final Task task = new Task();
        long toMoveByte = ( blockCount - index ) * this.getBlockSize();
        long readPos = blockCount * this.getBlockSize();
        long writePos = readPos + count * this.getBlockSize();
        long progress = 0L;
        long progressDivisor = 1L;
        long maxProgress = toMoveByte;

        // Clear the cached block count.
        this.cachedBlockCount = NO_CACHED_BLOCKCOUNT;

        // Increase the length of the file.
        this.getFileOperations().setLength( this.getFileOperations().
                                            getLength() + this.getBlockSize() *
                                            count );

        // New blocks are inserted at the end of the file.
        if ( toMoveByte <= 0L )
        {
            this.fireBlocksInserted( index, count );
            return;
        }

        final byte[] buf = this.newTemporaryBuffer(
            toMoveByte > Integer.MAX_VALUE
            ? Integer.MAX_VALUE
            : ( int ) toMoveByte );

        while ( maxProgress > Integer.MAX_VALUE )
        {
            maxProgress /= 2L;
            progressDivisor *= 2L;
        }

        task.setIndeterminate( false );
        task.setCancelable( false );
        task.setMinimum( 0 );
        task.setMaximum( ( int ) maxProgress );
        task.setProgress( ( int ) progress );
        task.setDescription( new InsertsBlocksMessage() );

        final boolean monitoring = toMoveByte > this.getMonitoringThreshold();
        if ( monitoring )
        {
            this.getTaskMonitor().monitor( task );
        }

        try
        {
            // Move all blocks from index inclusive count blocks to the end of
            // the file.
            while ( toMoveByte > 0L )
            {
                final int moveLen = buf.length >= toMoveByte
                    ? ( int ) toMoveByte
                    : buf.length;

                readPos -= moveLen;
                writePos -= moveLen;
                this.getFileOperations().setFilePointer( readPos );
                int read = 0;
                int total = 0;

                do
                {
                    read = this.getFileOperations().
                        read( buf, total, moveLen - total );

                    assert read != FileOperations.EOF :
                        "Unexpected end of file.";

                    total += read;

                }
                while ( total < moveLen );

                // Move the block count blocks to the end.
                this.getFileOperations().setFilePointer( writePos );
                this.getFileOperations().write( buf, 0, moveLen );

                toMoveByte -= moveLen;
                progress += moveLen;

                task.setProgress( ( int ) ( progress / progressDivisor ) );
            }

            this.fireBlocksInserted( index, count );
        }
        finally
        {
            if ( monitoring )
            {
                this.getTaskMonitor().finish( task );
            }
        }
    }

    public void readBlock( final long block, final int off,
                            final byte[] buf ) throws IOException
    {
        this.readBlock( block, off, buf, 0, buf.length );
    }

    public void readBlock( final long block, final int off, final byte[] buf,
                            final int index, final int length )
        throws IOException
    {
        this.assertValidArguments( block, off, buf, index, length );
        this.assertNotClosed();

        int totalRead = 0;
        int toRead = length;

        this.getFileOperations().setFilePointer(
            block * this.getBlockSize() + off );

        do
        {
            final int read = this.getFileOperations().
                read( buf, index + totalRead, toRead );

            assert read != FileOperations.EOF :
                "Unexpected end of file.";

            totalRead += read;
            toRead -= read;

        }
        while ( totalRead < length );
    }

    public void writeBlock( final long block, final int off,
                             final byte[] buf ) throws IOException
    {
        this.writeBlock( block, off, buf, 0, buf.length );
    }

    public void writeBlock( final long block, final int off,
                             final byte[] buf,
                             final int index, final int length )
        throws IOException
    {
        this.assertValidArguments( block, off, buf, index, length );
        this.assertNotClosed();

        this.getFileOperations().setFilePointer(
            block * this.getBlockSize() + off );

        this.getFileOperations().write( buf, index, length );
    }

    /**
     * {@inheritDoc}
     * Flushes the instance and closes the {@code FileOperations} implementation
     * backing the instance.
     *
     * @throws IOException if closing the {@code FileOperations} implementation
     * backing the instance fails, or if the instance already is closed.
     */
    public void close() throws IOException
    {
        this.assertNotClosed();

        this.flush();
        this.getFileOperations().close();
        this.closed = true;
    }

    public void addStructuredFileListener(
        final StructuredFileListener listener )
    {
        this.fileListeners.add( StructuredFileListener.class, listener );
    }

    public void removeStructuredFileListener(
        final StructuredFileListener listener )
    {
        this.fileListeners.remove( StructuredFileListener.class, listener );
    }

    public StructuredFileListener[] getStructuredFileListeners()
    {
        return ( StructuredFileListener[] ) this.fileListeners.getListeners(
            StructuredFileListener.class );

    }

    //----------------------------------------------------------StructuredFile--
    //--StructuredFileOperations------------------------------------------------

    /** {@code FileOperations} backing the instance. */
    private FileOperations fileOperations;

    /** Flags the instance as beeing closed. */
    private boolean closed;

    /**
     * Creates a new {@code StructuredFileOperations} instance taking the size
     * of one block in byte and the {@code FileOperations} operations are to be
     * performed with.
     *
     * @param blockSize Number of bytes per block.
     * @param fileOperations {@code FileOperations} implementation to operate
     * on.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws PropertyException if {@code blockSize} is negative or zero.
     * @throws IllegalArgumentException if {@code blockSize} is incompatible
     * with the length of {@code fileOperations}.
     * @throws IOException if getting the length from the {@code fileOperations}
     * fails.
     */
    public StructuredFileOperations( final int blockSize,
                                      final FileOperations fileOperations )
        throws IOException
    {
        super();

        this.initializeProperties( META.getProperties() );
        this.pBlockSize = blockSize;
        this.assertValidProperties();

        if ( fileOperations == null )
        {
            throw new NullPointerException( "fileOperations" );
        }

        this.fileOperations = fileOperations;

        final int minBufferedBlocks = this.getMinBufferedBlocks();

        this.minimumBuffer = this.getMemoryManager().
            allocateBytes( minBufferedBlocks * blockSize );

        this.assertValidFileLength();

        this.decimalBlockSize = new BigDecimal( blockSize );
    }

    /**
     * Creates a new {@code StructuredFileOperations} instance taking the size
     * of one block in byte, task monitoring configuration and the
     * {@code FileOperations} operations are to be performed with.
     *
     * @param blockSize Number of bytes per block.
     * @param monitoringThreshold the mininum number of bytes to copy to start
     * any task monitoring.
     * @param fileOperations {@code FileOperations} implementation to operate
     * on.
     *
     * @throws NullPointerException if {@code fileOperations} is {@code null}.
     * @throws PropertyException if either {@code blockSize} or
     * {@code monitoringTeshold} is negative or zero.
     * @throws IllegalArgumentException if {@code blockSize} is incompatible
     * with the length of {@code fileOperations}.
     * @throws IOException if getting the length from the {@code fileOperations}
     * fails.
     */
    public StructuredFileOperations( final int blockSize,
                                      final int monitoringThreshold,
                                      final FileOperations fileOperations )
        throws IOException
    {
        super();

        this.initializeProperties( META.getProperties() );
        this.pBlockSize = blockSize;
        this.pMonitoringThreshold = monitoringThreshold;
        this.assertValidProperties();

        if ( fileOperations == null )
        {
            throw new NullPointerException( "fileOperations" );
        }

        this.fileOperations = fileOperations;

        final int minBufferedBlocks = this.getMinBufferedBlocks();

        this.minimumBuffer = this.getMemoryManager().
            allocateBytes( minBufferedBlocks * blockSize );

        this.assertValidFileLength();

        this.decimalBlockSize = new BigDecimal( blockSize );
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
     * Calls the {@code flush()} method of an underlying
     * {@code FlushableFileOperations} instance, if any.
     *
     * @throws IOException if reading or writing fails.
     */
    public void flush() throws IOException
    {
        this.assertNotClosed();

        if ( this.getFileOperations() instanceof FlushableFileOperations )
        {
            ( ( FlushableFileOperations ) this.getFileOperations() ).flush();
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
    private void assertValidArguments( final long block, final int off,
                                        final byte[] buf, final int index,
                                        final int length ) throws
        NullPointerException, IndexOutOfBoundsException, IOException
    {
        final long blockCount = this.getBlockCount();

        if ( buf == null )
        {
            throw new NullPointerException( "buf" );
        }
        if ( block < 0 || block >= blockCount )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) block );
        }
        if ( off < 0 || off >= this.getBlockSize() )
        {
            throw new ArrayIndexOutOfBoundsException( ( int ) off );
        }
        if ( index < 0 || index >= buf.length )
        {
            throw new ArrayIndexOutOfBoundsException( index );
        }
        if ( length < 0L || length > buf.length - index ||
            length > this.getBlockSize() - off )
        {
            throw new ArrayIndexOutOfBoundsException( length );
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
        if ( this.getFileOperations() != null )
        {
            if ( this.getFileOperations().getLength() %
                this.getBlockSize() != 0L )
            {
                throw new IllegalArgumentException( Long.toString(
                                                    this.getFileOperations().
                                                    getLength() %
                                                    this.getBlockSize() ) );

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
        final int blockSize = this.getBlockSize();
        final int monitoringThreshold = this.getMonitoringThreshold();

        // minBufferedBlocks must be a positive integer.
        if ( !( minBufferedBlocks > 0 ) )
        {
            throw new PropertyException(
                "minBufferedBlocks",
                Integer.toString( minBufferedBlocks ) );

        }

        // blockSize must be a positive integer.
        if ( !( blockSize > 0 ) )
        {
            throw new PropertyException( "blockSize",
                                         Integer.toString( blockSize ) );

        }

        // monitoringThreshold must be a positive integer.
        if ( !( monitoringThreshold > 0 ) )
        {
            throw new PropertyException(
                "monitoringThreshold",
                Integer.toString( monitoringThreshold ) );

        }
    }

    /**
     * Checks that the instance is not closed.
     *
     * @throws IOException if the instance is closed.
     */
    private void assertNotClosed() throws IOException
    {
        if ( this.closed )
        {
            throw new IOException( StructuredFileOperationsBundle.getInstance().
                                   getAlreadyClosedText( Locale.getDefault() ) );

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
    private void fireBlocksInserted(
        final long index, final long insertedBlocks ) throws IOException
    {
        final Object[] listeners = this.fileListeners.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[i] == StructuredFileListener.class )
            {
                ( ( StructuredFileListener ) listeners[i + 1] ).blocksInserted(
                    index, insertedBlocks );

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
    private void fireBlocksDeleted(
        final long index, final long deletedBlocks ) throws IOException
    {
        final Object[] listeners = this.fileListeners.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[i] == StructuredFileListener.class )
            {
                ( ( StructuredFileListener ) listeners[i + 1] ).blocksDeleted(
                    index, deletedBlocks );

            }
        }
    }

    private byte[] newTemporaryBuffer( final int requested ) throws IOException
    {
        final byte[] tmp;
        final long length = this.getFileOperations().getLength();

        if ( requested <= 0 || requested > length )
        {
            throw new IllegalArgumentException( Integer.toString( requested ) );
        }

        return requested <= this.minimumBuffer.length ||
            this.getMemoryManager().getAvailableBytes() < requested
            ? this.minimumBuffer
            : this.getMemoryManager().allocateBytes( requested );

    }

    //------------------------------------------------StructuredFileOperations--
}
